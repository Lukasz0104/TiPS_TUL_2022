#include <iostream>
#include <stdexcept>

#include "definitions.h"
#include "Port.h"

using namespace TiPS::zadanie2;

bool prompt_for_crc();

int main()
{
	bool useCRC = prompt_for_crc();

	try
	{
		Port p("COM3");
		p.receive_message("received.txt", useCRC);
	}
	catch (std::runtime_error &ex)
	{
		std::cout << RED << ex.what() << RESET << std::endl;
	}

	return 0;
}

bool prompt_for_crc()
{
	std::cout << "Wybierz sposob obliczania sumy kontrolnej:\n";
	std::cout << "1. Suma algebraiczna\n";
	std::cout << "2. CRC\n";
	std::cout << "Wybor: ";

	char choice;
	std::cin >> choice;
	while (choice != '1' && choice != '2')
	{
		std::cout << RED << "Niepoprawny wybor!" << RESET << "\n";
		std::cin >> choice;
	}

	std::cout << BLOCK_SEPARATOR;

	return (choice == '2');
}
