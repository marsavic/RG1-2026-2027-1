# Računarska grafika 1


## Nameštanje

- Potreban vam je JDK 25 (ili noviji) koji sadrži JavaFX biblioteke.
  - Najlakše je da koristite BellSoft Liberica JDK (Full), koji, za razliku od većine drugih JDK-ova, dolazi sa ugrađenim JavaFX modulima. Radi na Windows-u, Linux-u i macOS-u (uključujući Apple Silicon).
    - U IntelliJ-u: File > Project Structure... > Project > SDK dropdown > Download JDK... > Version: 25, Vendor: BellSoft Liberica JDK (Full).
    - Alternativno, sami preuzmite JDK sa [https://bell-sw.com/pages/downloads/](https://bell-sw.com/pages/downloads/). Izaberite vaš OS, verziju 25 (ili noviju), i Full JDK (jedino Full JDK uključuje JavaFX). Kada instalirate/raspakujete JDK, namestite u IDE-u da projekat koristi baš taj JDK.
  - Ako vidite grešku `package javafx.scene.paint does not exist` (ili `JavaFX runtime components are missing`), vaš projekat ne koristi JDK sa JavaFX-om. Vratite se na prethodni korak.
- Biblioteke koje koristimo (DrawingFX2 i mars-bits) nalaze se u `lib` folderu i već su uključene u projekat. Ako se to ne desi automatski, uključite sve JAR fajlove iz `lib` foldera.
- U nekim slučajevima JavaFX neće koristiti GPU za iscrtavanje i sve će biti pomalo laggy (dešava se npr. uz Linux i integrisani GPU). U tom slučaju dodajte VM argument `-Dprism.forceGPU=true`.


## Pokretanje

- Svaki primer je klasa sa `main` metodom. Pokrenite je (u IntelliJ-u: desni klik na klasu > Run). Za proveru da li je sve dobro namešteno, pokrenite `HelloWorld`.
- Vrednosti polja označenih anotacijama `@GadgetDouble`, `@GadgetInteger`, `@GadgetAnimation`, ... možete menjati dok program radi, u panelu pored crteža.


## Šta-gde

- Kodovi sa predavanja i vežbi su organizovani po temama, u paketima `topic1_...`, `topic2_...`, itd.
- Slike koje koristimo su u `src/images`.
- Pregled metoda za crtanje je u `src/Metodi klase View.txt`.
- JAR fajlovi biblioteka sadrže i izvorni kod, pa u IDE-u možete pogledati dokumentaciju i implementaciju bilo koje klase ili metode (u IntelliJ-u: Ctrl+klik na ime).
