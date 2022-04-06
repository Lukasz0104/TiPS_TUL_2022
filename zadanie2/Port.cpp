#include <algorithm>
#include <fstream>
#include <iterator>
#include <iomanip>
#include <iostream>
#include <stdexcept>

#include "checksum.h"
#include "definitions.h"
#include "Port.h"

namespace TiPS::zadanie2
{
	Port::Port(LPCSTR port)
	{
		this->portHandle = CreateFile(
			port,
			GENERIC_READ | GENERIC_WRITE,
			0,
			nullptr,
			OPEN_EXISTING,
			0,
			nullptr);

		if (this->portHandle == INVALID_HANDLE_VALUE)
		{
			throw std::runtime_error("Nie udalo sie otworzyc portu");
		}

		settings.DCBlength = sizeof(DCB); // ustawienie rozmiaru struktury
		settings.BaudRate = CBR_9600;	  // ustawienie prędkosci transmisji
		settings.Parity = NOPARITY;		  // brak bitu parzystosci
		settings.StopBits = ONESTOPBIT;	  // ustawienie bitu stopu
		settings.ByteSize = 8;			  // liczba bitow w transmitowanym bajcie
		settings.fParity = TRUE;		  // wykonuj testy poprawnosci i zglaszaj bledy

		SetCommState(portHandle, &settings); // konfiguracja urządzenie komunikacyjnego

		portTimings.ReadIntervalTimeout = 3000;		   //
		portTimings.ReadTotalTimeoutMultiplier = 3000; //
		portTimings.ReadTotalTimeoutConstant = 3000;   //
		portTimings.WriteTotalTimeoutMultiplier = 100; //
		portTimings.WriteTotalTimeoutConstant = 100;   //

		SetCommTimeouts(portHandle, &portTimings);
		ClearCommError(portHandle, &portError, &portResources);
	}

	Port::~Port()
	{
		CloseHandle(portHandle);
	}

	void Port::send_message(const char *inFile)
	{
		BYTE b = 0;
		BYTE dataBlockNumber = 1;
		BYTE complementDataBlockNumber = 254;
		BYTE *buffer = new BYTE[128];
		std::ifstream file(inFile, std::ios::binary);
		file.seekg(0);

		while (b != NAK && b != C)
		{
			std::cout << "Czekam na znak " << YELLOW << "C" << RESET << " lub " << RED << "NAK" << RESET << "..." << std::endl;
			ReadFile(portHandle, &b, 1, nullptr, nullptr);
		}

		bool CRC = (b == C);

		while (true)
		{
			std::cout << "Wysylam naglowek:" << std::endl;
			std::cout << "| SOH | 0x"
					  << std::hex << std::setw(2) << std::setfill('0') << int(dataBlockNumber)
					  << " | 0x" << std::hex << std::setw(2) << std::setfill('0') << int(complementDataBlockNumber)
					  << " |" << std::endl;

			WriteFile(portHandle, &SOH, 1, nullptr, nullptr);
			WriteFile(portHandle, &dataBlockNumber, 1, nullptr, nullptr);
			WriteFile(portHandle, &complementDataBlockNumber, 1, nullptr, nullptr);

			file.read((char *)buffer, 128);

			if (file.eof()) // koniec pliku
			{
				std::cout << "Osiagnieto koniec pliku, wypelniam pozostale bajty znakami SUB.\n";
				int count = file.gcount();					  // liczba odczytanych znaków
				std::fill(buffer + count, buffer + 128, SUB); // wypełnienie znakami SUB
			}

			std::cout << "Wysylam blok danych" << std::endl;
			WriteFile(portHandle, buffer, 128, nullptr, nullptr);

			std::cout << "Wysylam sume kontrolna" << std::endl;
			if (CRC)
			{
				uint16_t calculatedCRC = crc(buffer);
				WriteFile(portHandle, &calculatedCRC, 2, nullptr, nullptr);
			}
			else
			{
				uint8_t calculatedChecksum = checksum(buffer);
				WriteFile(portHandle, &calculatedChecksum, 1, nullptr, nullptr);
			}

			std::cout << "Czekam na odpowiedz...\n";
			ReadFile(portHandle, &b, 1, nullptr, nullptr);

			if (b == ACK)
			{
				std::cout << "Odebralem znak " << GREEN << "ACK" << RESET << std::endl;
				dataBlockNumber++;
				complementDataBlockNumber = 255 - dataBlockNumber;

				if (file.eof())
				{
					std::cout << "Przeslalem wszystkie bloki, wysylam znak " << GREEN << "EOT" << RESET << ".\n";
					do
					{
						std::cout << "Czekam na znak " << GREEN << "ACK" << RESET << "...\n";
						WriteFile(portHandle, &EOT, 1, nullptr, nullptr);
					} while (b != ACK);
					std::cout << "Odebralem znak " << GREEN << "ACK" << RESET << ".\n";
					break;
				}
			}
			else
			{
				std::cout << "Odebralem znak" << RED << "NAK" << RESET << ".\n";
				file.seekg(-128, std::ios::cur); // cofnięcie się w pliku o 128 bajtów
			}
			std::cout << BLOCK_SEPARATOR;
		}

		file.close();
		delete[] buffer;
	}

	void Port::receive_message(const char *outFile, bool CRC)
	{
		BYTE b = 0;
		BYTE dataBlockNumber = 0;
		BYTE complementDataBlockNumber = 255;
		BYTE *buffer = new BYTE[128];
		std::ofstream file(outFile, std::ios::binary);
		file.seekp(0);

		if (!file.is_open())
		{
			throw new std::runtime_error("Nie udalo sie otworzyc pliku do zapisu");
		}

		while (b != SOH)
		{
			if (CRC)
			{
				std::cout << "Wysylam znak " << YELLOW << "C" << RESET << "\n";
				WriteFile(portHandle, &C, 1, nullptr, nullptr);
			}
			else
			{
				std::cout << "Wysylam znak " << RED << "NAK" << RESET << "\n";
				WriteFile(portHandle, &NAK, 1, nullptr, nullptr);
			}

			std::cout << "Czekam na znak SOH od nadajnika..." << std::endl;
			ReadFile(portHandle, &b, 1, nullptr, nullptr);
		}

		do
		{
			ReadFile(portHandle, &dataBlockNumber, 1, nullptr, nullptr);
			ReadFile(portHandle, &complementDataBlockNumber, 1, nullptr, nullptr);

			std::cout << "Odebralem naglowek:\n";
			std::cout << "| SOH | 0x"
					  << std::hex << std::setw(2) << std::setfill('0') << int(dataBlockNumber)
					  << " | 0x" << std::hex << std::setw(2) << std::setfill('0') << int(complementDataBlockNumber)
					  << " |" << std::endl;

			std::cout << "Odbieram blok danych\n";
			ReadFile(portHandle, buffer, 128, nullptr, nullptr); // odczytujemy 128-bajtowy blok danych
			bool check_passed = false;							 // flaga do sprawdzenia, czy odebrany block danych jest poprawny

			std::cout << "Sprawdzam odebrana sume kontrolna...\n";
			if (CRC)
			{
				uint16_t calculatedCRC = crc(buffer);
				uint16_t receivedCRC = 0;
				ReadFile(portHandle, &receivedCRC, 2, nullptr, nullptr);

				check_passed = (calculatedCRC == receivedCRC);
			}
			else
			{
				BYTE calculatedChecksum = checksum(buffer);
				BYTE receivedChecksum = 0;
				ReadFile(portHandle, &receivedChecksum, 1, nullptr, nullptr);

				check_passed = (calculatedChecksum == receivedChecksum);
			}

			if (check_passed)
			{
				std::cout << "Sumy kontrolne sie zgadzaja, wysylam znak " << GREEN << "ACK" << RESET << "\n";
				WriteFile(portHandle, &ACK, 1, nullptr, nullptr);

				if (buffer[127] == SUB) // niepełny blok danych
				{
					std::reverse_iterator<BYTE *> end(buffer + 127);
					std::reverse_iterator<BYTE *> start(buffer);

					auto pos = std::find_if(end, start,
											[](BYTE b)
											{
												return b != SUB;
											});

					int count = (buffer + 128) - pos.base();
					file.write((const char *)buffer, 128 - count);
				}
				else // odebrano pełny blok danych
				{
					file.write((const char *)buffer, 128);
				}
			}
			else
			{
				std::cout << "Sumy kontrolne sie nie zgadzaja, wysylam znak " << RED << "NAK" << RESET << "\n";
				WriteFile(portHandle, &NAK, 1, nullptr, nullptr);
			}

			std::cout << BLOCK_SEPARATOR;

			ReadFile(portHandle, &b, 1, nullptr, nullptr);

		} while (b != EOT);

		std::cout << "Odebralem znak " << GREEN << "EOT" << RESET << ". Wysylam znak " << GREEN << "ACK" << RESET << "\n";

		WriteFile(portHandle, &ACK, 1, nullptr, nullptr);

		file.close();
		delete[] buffer;
	}
}