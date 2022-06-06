# Kodowanie Huffmana

## Budowanie drzewa
Dla każdego znaku w wiadomości tworzymy węzeł z wartością równą liczbie wystąpień i dodajemy go do kolejki priorytetowej. Następnie, dopóki w kolejce jest więcej niż 1 węzeł, wybieramy dwa węzły o najmniejszych wartościach i tworzymy nowy węzeł, taki że:
- potomkami są wybrane wcześniej węzły,
- wartość jest równa sumie wartości wybranych węzłów.

## Kodowanie
Dla każdego znaku odnajdujemy w drzewie ścieżkę do węzła z danym znakiem. Gdy wybieramy lewego potomka, zapisujemy 1, gdy wybieramy prawego potomka wybieramy 0. Gdy zostaną zakodowane wszystkie znaki, zwracamy tablicę bajtów.

## Nagłówek
Aby możliwe było zdekodowanie wiadomości, należy również zapisać nagłówek zawierający informacje o strukturze drzewa. Nagłówek składa się z:
- długości tekstu (4 bajty),
- liczby węzłów (4 bajty),
- liczby znaków (2 bajty).

Następnie zapisywane jest drzewo w kolejności `POST ORDER`. Dla węzłów-liści zapisywana jest wartość 1, a następnie znak przechowywany w węźle. Dla węzłów, które nie są liścmi, zapisywana jest wartość 0.
Na końcu zapisywana jest zakodowana wiadomość.

## Dekodowanie
Ze strumienia bajtów odczytywana jest długość tekstu, liczba węzłów (`n`) i liczba wszystkich znaków (`c`). Następnie odczytywane jest `n + c` bajtów przechowujących strukturę drzewa. Po odtworzeniu struktury drzewa, odkodowywane są kolejne znaki.

---
[Opis kodowania Huffmana](https://engineering.purdue.edu/ece264/17au/hw/HW13?alt=huffman)
