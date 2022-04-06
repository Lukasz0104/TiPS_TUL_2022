#ifndef __DEFINITIONS_H__
#define __DEFINITIONS_H__

#include <windows.h>

namespace TiPS::zadanie2
{
	const BYTE SOH = 0x01; // Start of Header
	const BYTE EOT = 0x04; // End of Transmission
	const BYTE ACK = 0x06; // Acknowledge
	const BYTE NAK = 0x15; // Not Acknowledge
	const BYTE CAN = 0x18; // Cancel
	const BYTE SUB = 0x1A; // Substitute character - znak, którym wypełniany jest niepełny blok
	const BYTE C = 0x43;   // ASCII 'C'

	const char* RED = "\x1B[91m";
	const char* GREEN = "\x1B[92m";
	const char* YELLOW = "\x1B[93m";
	const char* RESET = "\x1B[0m";

	const char* BLOCK_SEPARATOR = "--------------------\n";
}

#endif // __DEFINITIONS_H__
