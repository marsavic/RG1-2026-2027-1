# Računarska grafika 1


## Nameštanje

- Potreban vam je JDK 25 (ili noviji) koji sadrži JavaFX biblioteke.
  - Najlakše je da koristite BellSoft Liberica JDK (Full), koji, za razliku od većine drugih JDK-ova, dolazi sa ugrađenim JavaFX modulima. Radi na Windows-u, Linux-u i macOS-u (uključujući Apple Silicon).
    - U IntelliJ-u: File > Project Structure... > Project > SDK dropdown > Download JDK... > Version: 25, Vendor: BellSoft Liberica JDK (Full).
    - Alternativno, sami preuzmite JDK sa [https://bell-sw.com/pages/downloads/](https://bell-sw.com/pages/downloads/). Izaberite vaš OS, verziju 25 (ili noviju), i Full JDK (jedino Full JDK uključuje JavaFX). Kada instalirate/raspakujete JDK, namestite u IDE-u da projekat koristi baš taj JDK.
    - U Eclipse-u (potrebna je verzija 2026-03 ili novija, starije ne podržavaju Javu 25) JDK ne može da se preuzme iz IDE-a, pa ga preuzmite kao u prethodnom koraku, a zatim ga dodajte: Window > Preferences > Java > Installed JREs > Add... > Standard VM > JRE home: folder u koji ste instalirali JDK > Finish, i štiklirajte ga.
  - Ako vidite grešku `package javafx.scene.paint does not exist` (u Eclipse-u `The import javafx cannot be resolved`) ili `JavaFX runtime components are missing`, vaš projekat ne koristi JDK sa JavaFX-om. Vratite se na prethodni korak.
- U Eclipse-u napravite projekat od foldera ovog repozitorijuma: File > New > Java Project > isključite *Use default location* i za *Location* izaberite folder repozitorijuma > za JRE izaberite Liberica JDK > isključite *Create module-info.java file* > Finish.
- Biblioteke koje koristimo (DrawingFX2 i mars-bits) nalaze se u `lib` folderu. Uključite u projekat sve JAR fajlove iz `lib` foldera (ako ih IDE nije već sam uključio).
  - U IntelliJ-u: označite sve JAR fajlove u `lib` folderu > desni klik > Add as Library... > OK.
  - U Eclipse-u: desni klik na projekat > Properties > Java Build Path > Libraries > Classpath > Add JARs... > izaberite sve JAR fajlove iz `lib`.
- U nekim slučajevima JavaFX neće koristiti GPU za iscrtavanje i sve će biti pomalo laggy (dešava se npr. uz Linux i integrisani GPU). U tom slučaju dodajte VM argument `-Dprism.forceGPU=true`.


## Pokretanje

- Svaki primer je klasa sa `main` metodom. Pokrenite je (u IntelliJ-u: desni klik na klasu > Run; u Eclipse-u: desni klik na klasu > Run As > Java Application). Za proveru da li je sve dobro namešteno, pokrenite `HelloWorld`.
- Vrednosti polja označenih anotacijama `@GadgetDouble`, `@GadgetInteger`, `@GadgetAnimation`, ... možete menjati dok program radi, u panelu pored crteža.


## Šta-gde

- Kodovi sa predavanja i vežbi su organizovani po temama, u paketima `topic1_...`, `topic2_...`, itd.
- Slike koje koristimo su u `src/images`.
- JAR fajlovi biblioteka sadrže i izvorni kod, pa u IDE-u možete pogledati dokumentaciju i implementaciju bilo koje klase ili metode (u IntelliJ-u: Ctrl+klik na ime; u Eclipse-u: Ctrl+klik ili F3, a ako se prikaže *Source not found*, kliknite *Attach Source...* i izaberite isti JAR fajl).
