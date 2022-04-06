# Protokół Xmodem

## Opis protokołu
- transmisja jest inicjowana przez odbiornik, który wysyła znak `NAK` w odstępach co 10s przez 60s
- transferowany plik dzieli się na bloki o długości 128 bajtów, bloki są transmitowane jeden po drugim po sprawdzeniu poprawności poprzedniego bloku
- każdy blok jest zaopatrywany w nagłowek, który składa się z:
  - znaku `SOH`
  - numeru bloku (1 bajt)
  - dopełnienie numeru bloku do 255 (255 - numer bloku)
- po przesłaniu nagłówka następuje przesłanie bloku danych, a następnie sumy kontrolnej
- po odebraniu bloku danych odbiornik oblicza sumę kontrolną i porównuje ją z sumą obliczoną przez nadajnik, jeżeli:
  - sumy kontrolne są zgodne, wysyłany jest znak `ACK`, który jest sygnałem do przesłania kolejnego bloki
  - sumy kontrolne są różne, wysyłany jest znak `NAK` i nadajnik ponawia transmisję tego bloku danych
- po przesłaniu ostatniego bajtu danych nadajnik wysyła znak `EOT` dopóki nie otrzyma w odpowiedzi znaku `ACK`

[Opis protokołu XMODEM](http://web.mit.edu/6.115/www/amulet/xmodem.htm)

## Definicje znaków
- `SOH` = 0x01
- `EOT` = 0x04
- `ACK` = 0x06
- `NAK` = 0x15
- `CAN` = 0x18
- `C` = 0x43

## Rozszerzenie `CRC`
- transmisja rozpoczyna się przesłaniem znaku `C` przez odbiornik
- suma kontrolna składa się z 2 bajtów
- nagłowek pakietu rozpoczyna się znakiem `C`

## Korzystanie z programu
1. Należy zapewnić wirtualne połączenie pomiędzy portami COM2 (nadajnik) i COM3 (odbiornik). Można wykorzystać do tego program dostępny na: https://freevirtualserialports.com/
2. Utworzenie pliku `wiadomosc.txt` z wiadomością do przesłania
3. Uruchomienie programu sender i wybranie trybu działania (Suma algebraiczna / CRC)
4. Uruchomienie programu receiver i wybranie trybu działania (Suma algebraiczna / CRC)
