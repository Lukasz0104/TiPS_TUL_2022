#include "functions.h"
#include <fstream>

namespace TiPS::zadanie1
{
	uint8_t check(uint16_t codeword)
	{
		uint8_t result = 0; // reprezentacja wektora 1x8 jako bity w liczbie 8-bitowej

		for (int i = 0; i < 8; i++)
		{
			result <<= 1;
			uint8_t x = 0;

			for (int j = 0; j < 16; j++)
			{
				uint16_t a = codeword >> (15 - j);		// j-ty bit słowa kodowego
				uint16_t b = (H_matrix[i]) >> (15 - j); // j-ty bit w i-tym wierszu macierzy H
				uint8_t c = a & b & 1;					// oblicz iloczyn
				x ^= c;									// dodaj modulo 2
			}
			result |= x; // ustaw i-ty element w wynikowym wektorze
		}

		return result;
	}

	uint16_t encode_byte(uint8_t word)
	{
		uint16_t result = 0;
		for (int i = 0; i < 16; i++)
		{
			result <<= 1;
			uint8_t x = 0;

			for (int j = 0; j < 8; j++)
			{
				uint8_t a = word >> (7 - j);	  // j-ty bit słowa do zakodowania
				uint16_t b = generator_matrix[j]; // j-ty wiersz macierzy G
				uint8_t c = b >> (15 - i);		  // i-ty bit w j-tym wierszu macierzy G
				uint8_t d = a & c & 1;			  // oblicz iloczyn
				x = x ^ d;						  // dodaj modulo 2
			}
			result |= x; // ustaw i-ty element w słowie kodowym
		}

		return result;
	}

	uint8_t decode_word(uint16_t codeword)
	{
		return uint8_t(codeword >> 8);
	}

	uint8_t get_column(int index)
	{
		uint8_t result = 0;

		for (int i = 0; i < 8; i++)
		{
			result <<= 1;
			result |= (((H_matrix[i]) >> (15 - index)) & 1);
		}
		return result;
	}

	uint8_t find_column_indices(uint8_t column)
	{
		for (int i = 0; i < 16; i++)
		{
			if (get_column(i) == column)
			{
				return i;
			}

			for (int j = 0; j < 16; j++)
			{
				if (i == j)
					continue;
				if ((get_column(i) ^ get_column(j)) == column) // sprawdzenie, czy suma kolumn jest równa szukanej kolumnie
				{
					return (i << 4) | j; // zakodowanie numerów kolumn, każdy na 4 bitach
				}
			}
		}
		return NOT_FOUND;
	}

	uint16_t correct(uint16_t codeword)
	{
		uint8_t product = check(codeword);

		if (product == 0) // brak błedów
		{
			return codeword;
		}

		uint8_t index = find_column_indices(product);

		if (index < 0x10u) // 1 błąd
		{
			codeword ^= (1 << (15 - index));
		}
		else // 2 błędy
		{
			int first_error_position = index & 0b1111;
			int second_error_position = index >> 4;

			codeword ^= (1 << (15 - first_error_position));
			codeword ^= (1 << (15 - second_error_position));
		}

		return codeword;
	}

	void encode_file()
	{
		std::ifstream input_file("wiadomosc.txt", std::ios::binary);
		std::ofstream output_file("zakodowana.txt", std::ios::binary);

		uint8_t byte;

		while (input_file.read((char *)&byte, 1)) // odczytujemy po 1 bajcie
		{
			uint16_t encoded = encode_byte(byte);					   // kodujemy
			output_file << char(encoded >> 8) << char(encoded & 0xff); // zapisujemy osobno każdy z bajtów słowa kodowego
		}

		input_file.close();
		output_file.close();
	}

	void decode_file()
	{
		std::ifstream input_file("zakodowana.txt", std::ios::binary);
		std::ofstream out_file("odkodowana.txt", std::ios::binary);

		input_file.seekg(0, std::ios::beg);

		uint16_t word;
		uint8_t byte;

		do
		{
			input_file.read((char *)&byte, 1); // odczytujemy pierwszy bajt
			word = uint16_t(byte) << 8;
			input_file.read((char *)&byte, 1); // odczytujemy drugi bajt
			word = word | byte;

			uint16_t corrected = correct(word);
			char decoded = decode_word(corrected);

			out_file << decoded; // zapisujemy odkowany bajt
		} while (input_file.peek() != EOF);

		input_file.close();
		out_file.close();
	}
}
