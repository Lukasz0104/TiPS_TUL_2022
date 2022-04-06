#include <iostream>

#include "Port.h"

using namespace TiPS::zadanie2;

int main()
{
	try
	{
		Port p("COM2");
		p.send_message("message.txt");
	}
	catch (std::runtime_error &ex)
	{
		std::cout << ex.what() << std::endl;
	}

	return 0;
}
