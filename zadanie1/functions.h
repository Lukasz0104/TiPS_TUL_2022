#ifndef __FUNCTIONS_H__
#define __FUNCTIONS_H__

#include <cstdint>

#define NOT_FOUND 0xFF

// Macierz generująca słowa kluczowe.
const uint16_t generator_matrix[]{
	0b1000000001001110u,
	0b0100000000100111u,
	0b0010000010001111u,
	0b0001000011011011u,
	0b0000100011110001u,
	0b0000010011100100u,
	0b0000001001110010u,
	0b0000000100111001u};

// Macierz kontroli parzystości.
const uint16_t H_matrix[]{
	0b0011110010000000u,
	0b1001111001000000u,
	0b0100111100100000u,
	0b0001101100010000u,
	0b1011000100001000u,
	0b1110010000000100u,
	0b1111001000000010u,
	0b0111100100000001u};

// Oblicza iloczyn macierzy H z przekazanym słowem kodowym.
uint8_t check(uint16_t codeword);

// Oblicza iloczyn słowa z macierzą generującą.
uint16_t encode_byte(uint8_t word);

// Zwraca odkowany bajt.
uint8_t decode_word(uint16_t codeword);

// Wyszukuje i poprawia maksymalnie 2 błędy w słowie kodowym.
uint16_t correct(uint16_t codeword);

// Zwraca kolumnę z macierzy H.
uint8_t get_column(int index);

// Zwraca pozycję kolumny z macierzy H lub pozycje kolumn, których suma jest równa szukanej kolumnie.
uint8_t find_column_indices(uint8_t column);

// Koduje zawartość pliku wiadomosc.txt i zapisuje w pliku zakodowana.txt
void encode_file();

// Koryguje błędy z pliku zakodowana.txt i zapisuje odkodowaną wiadomość w pliku odkodowana.txt
void decode_file();

#endif // __FUNCTIONS_H__