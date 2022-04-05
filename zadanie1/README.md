# Kody wykrywające i korygujące błędy
## Kodowanie
Zakodowanie polega na pomnożeniu bajtu (reprezentowanego jako wektor wierszowy) z macierzą generującą G. Otrzymujemy w ten sposób wektor wierszowy, którego elementy odpowiadają kolejnym bitom w słowie kodowym.

## Dekodowanie
Obliczany jest iloczyn macierzy kontroli parzystości H ze słowem kodowym. Jeżeli wynik jest wektorem zerowym, oznacza to brak błędu i zwracany jest starszy bajt słowa kodowego. W przeciwnym przypadku poszukujemy kolumny równej wynikowemu wektorowi lub dwóch kolumn, których suma jest równa otrzymanemu wektorowi. Numer kolumny bądź kolumn wyznacza miejsce/miejsca, na których zostały zmienione bity.

## Macierz G
Macierz generująca jest postaci 
![equation](https://latex.codecogs.com/png.image?\dpi{120}\bg{white}G&space;=&space;&space;\begin{bmatrix}&space;I_k&space;|&space;P&space;\end{bmatrix}),
gdzie:
- `k` - liczba bitów informacji,
- `n` - długość słowa kodowego,
- `I` - macierz jednostkowa stopnia k,
- `P` - macierz wymiaru k x (n - k).

## Macierz H
Macierz kontroli parzystości H jest postaci
![equation](https://latex.codecogs.com/png.image?\dpi{120}\bg{white}H&space;=&space;&space;\begin{bmatrix}&space;P^T&space;|&space;I_{n&space;-&space;k}&space;\end{bmatrix}&space;)

Własności macierzy H:
- nie występuje kolumna składająca się tylko z 0,
- kolumy się nie powtarzają,
- aby korygować błędy dwubitowe kolumny muszą być liniowo niezależne.

---
## Korzystanie z programu
1. Zapisanie wiadomości w pliku `wiadomosc.txt`
2. Uruchomienie programu i wybranie opcji 1 (`Zakoduj zawartosc pliku wiadomosc.txt`)
3. Wprowadzenie zmian w pliku `zakodowana.txt`, np. zamiana znaku `a` na `b` (zmiana 1 bitu), lub zamiana znaku `a` na `g` (zmiana 2 bitów). **Ważne jest aby zapisać plik z odpowiednim kodowaniem znaków, gdyż w przeciwnym przypadku spowoduje to błędne odkowanie wiadomości!**
4. Wybranie opcji 2 w programie (`Odkoduj zakodowaną wiadomosc`) i sprawdzenie poprawności odkodowania.
