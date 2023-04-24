# PuzzleSolver
Projekt z początku hobbystyczny mający na celu rozwiązywanie różnego rodzaju zagadek logicznych, a w ostatnim czasie także **źródło algorytmów będących przedmiotem badań w pracy magisterskiej**

Zagadki logiczne zawarte w aplikacji:
1. Nonogramy
* Obecna wersja pozwala na rozwiązywanie nonogramów heurystykami na, które składa się łącznie 9 podprocedur (po 9 dla wierszy i kolumn).
* Oprócz samej logiki opartej na podprocedurach, zaimplementowana została metoda prób i błędów ("rekurencja jednopoziomowa"). Metoda ta sprawdza czy rozwiązanie z krokiem
naprzód jest niepoprawne dla jednej z decyzji ("X" lub "O", pole "wyiksowane"/"puste" lub "pokolorowane").
* Możliwe wybieranie nonogramów z użyciem filtrów i sortowania w widoku
* Możliwe tworzenie nonogramów w sekcji CREATE z wyborem podstawowych cech, a następnie po sprawdzeniu poprawności zapisania do pliku w formacie .json
2. Sudoku
* Możliwe wybieranie zagadki typu sudoku z użyciem filtrów i sortowania w widoku
* Utworzony widok dla zagadki typu sudoku
* Możliwe tworzenie sudoku, sprawdzanie poprawności tylko na podstawie ilości wypełnionych komórek(>18?)
* Prosty solver oparty na wybraniu 1 z 4 typów akcji i zakresu wykonania akcji
3. Akari
* Możliwe wybieranie zagadki typu akari z użyciem filtrów i sortowania w widoku
* Utworzony widok dla zagadki typu akari
4. Hitori
* Możliwe wybieranie zagadki typu hitori z użyciem filtrów i sortowania w widoku
* Utworzony widok dla zagadki typu hitori
5. Slitherlink
* Możliwe wybieranie zagadki typu slitherlink z użyciem filtrów i sortowania w widoku
* Utworzony widok dla zagadki typu slitherlink
6. Architect
* Możliwe wybieranie zagadki typu architect z użyciem filtrów i sortowania w widoku
* Utworzony widok dla zagadki typu architect

Do wykonania w przyszłości:
* Algorytmy heurystyczne lub inne do rozwiązywania każdej z wymienionych 6 typów zagadek, lub także innych
* Dopracowywanie interfejsu graficznego aplikacji
* Funkcje tworzenia, sprawdzania poprawności i zapisywanie zagadek do plików .json o określonej strukturze
