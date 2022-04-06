#include <iostream>
#include <stdexcept>

#include "Port.h"

using namespace TiPS::zadanie2;

int main()
{
	bool useCRC = true; // TODO add menu
	try
	{
		Port p("COM3");
		p.receive_message("received.txt", useCRC); 
	}
	catch (std::runtime_error &ex)
	{
		std::cout << ex.what() << std::endl;
	}

	return 0;
}
