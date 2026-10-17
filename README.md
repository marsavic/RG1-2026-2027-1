# Računarska grafika 1


## Nameštanje

Uputstva korak po korak, sa slikama (preuzimanje repozitorijuma, nameštanje projekta, pokretanje i preuzimanje novih kodova posle svakog časa):

- [IntelliJ IDEA](uputstva/IntelliJ.md)
- [Eclipse](uputstva/Eclipse.md)

Ukratko: potreban vam je JDK 25 (ili noviji) koji sadrži JavaFX. Najlakše je koristiti BellSoft Liberica JDK (Full), koji, za razliku od većine drugih JDK-ova, dolazi sa JavaFX-om ([bell-sw.com/pages/downloads](https://bell-sw.com/pages/downloads/)). Biblioteke koje koristimo (DrawingFX2 i mars-bits) su JAR fajlovi u folderu `lib`.


## Pokretanje

- Svaki primer je klasa sa `main` metodom. Za proveru da li je sve dobro namešteno, pokrenite `HelloWorld`.
- Vrednosti polja označenih anotacijama `@GadgetDouble`, `@GadgetInteger`, `@GadgetAnimation`, ... možete menjati dok program radi, u panelu pored crteža.


## Šta-gde

- Kodovi sa predavanja i vežbi su organizovani po temama, u paketima `topic1_...`, `topic2_...`, itd.
- Slike koje koristimo su u `src/images`.
- JAR fajlovi biblioteka sadrže i izvorni kod, pa u IDE-u možete pogledati dokumentaciju i implementaciju bilo koje klase ili metode (Ctrl+klik na ime).
