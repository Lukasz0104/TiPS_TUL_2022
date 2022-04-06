#include "checksum.h"

namespace TiPS::zadanie2
{
	uint8_t checksum(BYTE const *buffer)
	{
		uint8_t sum = 0;
		for (int i = 0; i < 128; i++)
		{
			sum += uint8_t(buffer[i]);
		}
		return sum;
	}

	uint16_t crc(BYTE const *buffer)
	{
		uint16_t crc = 0;
		for (int i = 0; i < 128; i++)
		{
			crc ^= (buffer[i]) << 8;
			for (int j = 0; j < 8; j++)
			{
				if (crc & 0x8000)
				{
					crc = (crc << 1) ^ 0x1021;
				}
				else
				{
					crc <<= 1;
				}
			}
		}
		return crc;
	}
}