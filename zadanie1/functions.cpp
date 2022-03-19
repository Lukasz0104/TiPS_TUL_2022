#include "functions.h"
#include <fstream>

uint8_t check(uint16_t codeword)
{
	uint8_t result = 0; // represent 8x1 matrix as bits in 8 bit number

	for (int i = 0; i < 8; i++)
	{
		result <<= 1;
		uint8_t x = 0;

		for (int j = 0; j < 16; j++)
		{
			uint16_t a = codeword >> (15 - j);		// get jth bit of the codeword
			uint16_t b = (H_matrix[i]) >> (15 - j); // get jth bit in ith row of H matrix
			uint8_t c = a & b & 1;					// calculate product
			x ^= c;									// add mod 2
		}
		result |= x;
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
			uint8_t a = word >> (7 - j);
			uint16_t b = generator_matrix[j];
			uint8_t c = b >> (15 - i);
			uint8_t d = a & c & 1;
			x = x ^ d;
		}
		result |= x;
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
			if ((get_column(i) ^ get_column(j)) == column)
			{
				return (i << 4) | j;
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

	while (input_file.read((char *)&byte, 1))
	{
		uint16_t encoded = encode_byte(byte);
		output_file << char(encoded >> 8) << char(encoded & 0xff);
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
		input_file.read((char *)&byte, 1);
		word = uint16_t(byte) << 8;
		input_file.read((char *)&byte, 1);
		word = word | byte;

		uint16_t corrected = correct(word);
		char decoded = decode_word(corrected);

		out_file << decoded;
	} while (input_file.peek() != EOF);

	input_file.close();
	out_file.close();
}
