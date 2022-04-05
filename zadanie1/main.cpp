#include "functions.h"
#include <iostream>
#include <bitset>

using namespace TiPS::zadanie1;

char menu();

int main()
{
	char inp;

	do
	{
		inp = menu();

		if (inp == '1')
		{
			encode_file();
			std::cout << "Zapisano zakodowana zawartosc pliku 'wiadomosc.txt' do pliku 'zakodowana.txt'\n";
		}
		else if (inp == '2')
		{
			decode_file();
			std::cout << "Odkowano zawartosc pliku 'zakodowana.txt' i zapisano odkowana wiadomosc w pliku 'odkodowana.txt'.\n";
		}
	} while (inp != '0');
}

char menu()
{
	std::cout << "0. Koniec programu.\n";
	std::cout << "1. Zakoduj zawartosc pliku wiadomosc.txt\n";
	std::cout << "2. Odkoduj zakodowaną wiadomosc\n";
	std::cout << "Wybierz operacje: ";

	char ch;
	std::cin >> ch;
	if (ch >= '0' && ch <= '2')
	{
		return ch;
	}
	std::cout << "\e[91mNiepoprawny wybor!\e[0m\n";
	return menu();
}
