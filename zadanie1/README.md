# Kody wykrywające i korygujące błędy
## Kodowanie
Zakodowanie polega na pomnożeniu bajtu (reprezentowanego jako wektor wierszowy) z macierzą generującą G. Otrzymujemy w ten sposób wektor wierszowy, którego elementy odpowiadają kolejnym bitom w słowie kodowym.

## Dekodowanie
Obliczany jest iloczyn macierzy kontroli parzystości H ze słowem kodowym. Jeżeli wynik jest wektorem zerowym, oznacza to brak błędu i zwracany jest starszy bajt słowa kodowego. W przeciwnym przypadku poszukujemy kolumny równej wynikowemu wektorowi lub dwóch kolumn, których suma jest równa otrzymanemu wektorowi. Numer kolumny bądź kolumn wyznacza miejsce/miejsca, na których zostały zmienione bity.

## Macierz G
Macierz generująca jest postaci 
![equation](http://www.sciweavers.org/tex2img.php?eq=G%20%3D%20%20%5Cbegin%7Bbmatrix%7D%20I_k%20%7C%20P%20%5Cend%7Bbmatrix%7D&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0),
gdzie:
- `k` - liczba bitów informacji,
- `n` - długość słowa kodowego,
- `I` - macierz jednostkowa stopnia k,
- `P` - macierz wymiaru k x (n - k).

## Macierz H
Macierz kontroli parzystości H jest postaci:
![equation](http://www.sciweavers.org/tex2img.php?eq=H%20=%20%20%5Cbegin%7Bbmatrix%7D%20P%5ET%20%7C%20I_%7Bn-k%7D%20%5Cend%7Bbmatrix%7D&bc=White&fc=Black&im=jpg&fs=12&ff=arev&edit=0)

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
