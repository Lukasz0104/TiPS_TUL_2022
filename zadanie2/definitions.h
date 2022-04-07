#ifndef __DEFINITIONS_H__
#define __DEFINITIONS_H__

#include <windows.h>

namespace TiPS::zadanie2
{
	constexpr const BYTE SOH = 0x01; // Start of Header
	constexpr const BYTE EOT = 0x04; // End of Transmission
	constexpr const BYTE ACK = 0x06; // Acknowledge
	constexpr const BYTE NAK = 0x15; // Not Acknowledge
	constexpr const BYTE CAN = 0x18; // Cancel
	constexpr const BYTE SUB = 0x1A; // Substitute character - znak, którym wypełniany jest niepełny blok
	constexpr const BYTE C = 0x43;   // ASCII 'C'

	constexpr const char *RED = "\x1B[91m";
	constexpr const char *GREEN = "\x1B[92m";
	constexpr const char *YELLOW = "\x1B[93m";
	constexpr const char *RESET = "\x1B[0m";

	constexpr const char *BLOCK_SEPARATOR = "--------------------\n";
}

#endif // __DEFINITIONS_H__
