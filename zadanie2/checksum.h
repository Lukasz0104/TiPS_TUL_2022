#ifndef __CHECKSUM_H__
#define __CHECKSUM_H__

#include <windows.h>
#include <cstdint>

namespace TiPS::zadanie2
{
	uint8_t checksum(BYTE const *buffer);
	uint16_t crc(BYTE const *buffer);
}
#endif // __CHECKSUM_H__