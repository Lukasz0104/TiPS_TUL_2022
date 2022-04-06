#ifndef __PORT_H__
#define __PORT_H__

#include <windows.h>

namespace TiPS::zadanie2
{
	class Port
	{
	private:
		HANDLE portHandle;			 //
		DCB settings{0};			 //
		COMSTAT portResources{};	 //
		DWORD portError{};			 //
		COMMTIMEOUTS portTimings{0}; //

	public:
		Port(LPCSTR port);
		~Port();
		void send_message(const char *inFile);
		void receive_message(const char *outFile, bool useCRC = false);
	};
}

#endif // __PORT_H__