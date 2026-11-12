# 📋 Podroben opis primerov uporabe – $troško

Dokument vsebuje podrobne opise vseh **še neimplementiranih** primerov uporabe (PU) iz [diagrama primerov uporabe](../README.md#-diagram-primerov-uporabe).

| ID | Primer uporabe | Akter | Status |
|---|---|---|---|
| PU1 | [Registracija](#pu1--registracija) | Neregistriran uporabnik | 📝 |
| PU2 | [Upravljanje kategorij in budgetov](#pu2--upravljanje-kategorij-in-budgetov-mesečni-budget) | Registriran uporabnik | ✅ kategorije · 🚧 mesečni budget* |
| PU3 | Vnos stroška | Registriran uporabnik | ✅ – ni podrobno opisan** |
| PU4 | [Vnos prihodka](#pu4--vnos-prihodka) | Registriran uporabnik | 🚧 |
| PU5 | [Pregled porabe](#pu5--pregled-porabe) | Registriran uporabnik | 🚧 |
| PU6 | [Preverjanje budgeta](#pu6--preverjanje-budgeta) | – (vključen v PU2 in PU3) | 🚧 |
| PU7 | [Prikaz opozorila](#pu7--prikaz-opozorila) | – (razširja PU5 in PU6) | 🚧 |
| PU8 | [Upravljanje ponavljajočih se stroškov](#pu8--upravljanje-ponavljajočih-se-stroškov) | Registriran uporabnik | 📝 |
| PU9 | [Upravljanje varčevalnih ciljev](#pu9--upravljanje-varčevalnih-ciljev) | Registriran uporabnik | 📝 |
| PU10 | [Upravljanje uporabnikov](#pu10--upravljanje-uporabnikov) | Administrator | 📝 |

*Upravljanje kategorij in budgetov kategorij je že implementirano, zato je podrobno opisan le še neimplementiran del – nastavitev mesečnega budgeta.<br>
** Vnos stroška je že implementiran; v načrtovani različici bo dopolnjen z vključenim PU6 *Preverjanje budgeta*, ki je opisan spodaj.

> **Prijava** ni samostojen primer uporabe, temveč predpogoj za vse primere uporabe registriranega uporabnika in administratorja. Ker je administrator specializacija registriranega uporabnika, lahko izvaja tudi vse njegove primere uporabe.

---

## PU1 – Registracija

| **Primer uporabe:** Registracija | **ID:** PU1 |
|---|---|

**Cilj:**
Neregistriran uporabnik želi ustvariti uporabniški račun, da bo lahko spremljal svoje finance in bodo njegovi podatki shranjeni ločeno od drugih uporabnikov.

**Akterji:**
Neregistriran uporabnik

**Predpogoji:**
- Aplikacija je zagnana in povezana s podatkovno bazo.
- Uporabnik ni prijavljen.

**Stanje sistema po PU:**
Stanje se spremeni – v bazi je ustvarjen nov uporabniški račun z vlogo *registriran uporabnik*.

**Scenarij:**
1. Uporabnik na začetni strani izbere *Registracija*.
2. Sistem prikaže obrazec: ime, e-poštni naslov, geslo in ponovitev gesla.
3. Uporabnik izpolni obrazec in potrdi.
4. Sistem preveri, ali so podatki veljavni in ali e-poštni naslov še ni v uporabi.
5. Sistem shrani nov račun (geslo shrani zgoščeno).
6. Sistem uporabnika samodejno prijavi in ga preusmeri na pregled porabe.

**Alternativni tokovi:**
- **1a.** Uporabnik že ima račun – izbere *Prijava*; PU se zaključi.
- **3a.** Uporabnik prekliče registracijo – sistem ga vrne na začetno stran, nič se ne shrani.
- **4a.** E-poštni naslov ni veljavne oblike – sistem označi polje in zahteva popravek; nadaljuje se pri koraku 3.
- **4b.** Geslo je krajše od 8 znakov ali se gesli ne ujemata – sistem izpiše napako; nadaljuje se pri koraku 3.
- **4c.** Račun s tem e-poštnim naslovom že obstaja – sistem izpiše *»Račun s tem e-poštnim naslovom že obstaja.«* in ponudi prijavo.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Registracija trenutno ni mogoča. Poskusite znova.«*; vneseni podatki (razen gesla) ostanejo v obrazcu.

---

## PU2 – Upravljanje kategorij in budgetov (mesečni budget)

| **Primer uporabe:** Upravljanje kategorij in budgetov | **ID:** PU2 |
|---|---|

**Cilj:**
Uporabnik želi poleg budgetov posameznih kategorij določiti tudi skupni mesečni budget, da lahko sproti spremlja, koliko mu v mesecu še ostane.

**Akterji:**
Registriran uporabnik

**Predpogoji:**
- Uporabnik je prijavljen.

**Stanje sistema po PU:**
Stanje se spremeni – za izbrani mesec je shranjen (ali posodobljen) mesečni budget uporabnika.

**Scenarij:**
1. Uporabnik odpre stran *Kategorije in budgeti*.
2. Sistem prikaže uporabnikove kategorije z budgeti, vsoto budgetov kategorij in trenutno nastavljen mesečni budget za tekoči mesec (če obstaja).
3. Uporabnik izbere *Nastavi mesečni budget*, vnese znesek in potrdi.
4. Sistem preveri veljavnost zneska.
5. Sistem shrani mesečni budget za izbrani mesec.
6. Sistem izvede PU6 *Preverjanje budgeta* («include») z novim budgetom.
7. Sistem na pregledu porabe prikaže nov preostanek (mesečni budget − poraba).

**Alternativni tokovi:**
- **1a.** Uporabnik želi dodati, urediti ali izbrisati kategorijo ali njen budget – izvede se že implementirani del PU2. Ob vsaki spremembi budgeta kategorije sistem prav tako izvede PU6 *Preverjanje budgeta*.
- **2a.** Mesečni budget za mesec še ni nastavljen – sistem kot predlog prikaže znesek iz prejšnjega meseca ali vsoto budgetov kategorij.
- **3a.** Uporabnik izbere drug (prihodnji) mesec – sistem prikaže podatke za ta mesec; nadaljuje se pri koraku 3.
- **3b.** Uporabnik prekliče – nič se ne spremeni.
- **4a.** Znesek je negativen ali prazen – sistem izpiše napako in zahteva popravek; nadaljuje se pri koraku 3.
- **4b.** Mesečni budget je manjši od vsote budgetov kategorij – sistem prikaže opozorilo in vpraša, ali uporabnik vseeno želi shraniti; ob potrditvi se nadaljuje pri koraku 5.
- **4c.** Mesečni budget je manjši od že porabljenega zneska v mesecu – sistem opozori, da je budget že presežen; ob potrditvi se nadaljuje pri koraku 5.
- **6a.** Poraba že dosega 80 % ali več novega budgeta – v okviru PU6 se sproži PU7 *Prikaz opozorila*.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Budgeta ni bilo mogoče shraniti. Poskusite znova.«*; prejšnja vrednost ostane nespremenjena.
- Seja uporabnika je potekla – sistem ga preusmeri na prijavo; po ponovni prijavi se vrne na stran *Kategorije in budgeti*.

---

## PU4 – Vnos prihodka

| **Primer uporabe:** Vnos prihodka | **ID:** PU4 |
|---|---|

**Cilj:**
Uporabnik želi zabeležiti prejeti denar (štipendijo, plačilo študentskega dela, žepnino …), da ima pregled nad tem, koliko denarja ima na voljo v mesecu.

**Akterji:**
Registriran uporabnik

**Predpogoji:**
- Uporabnik je prijavljen.

**Stanje sistema po PU:**
Stanje se spremeni – v bazi je shranjen nov prihodek uporabnika, preostanek tekočega meseca se ustrezno poveča.

**Scenarij:**
1. Uporabnik na strani s prihodki izbere možnost *Dodaj prihodek*.
2. Sistem prikaže obrazec s polji: znesek, datum (privzeto današnji), vir prihodka in kratek opis.
3. Uporabnik vnese podatke in potrdi vnos.
4. Sistem preveri veljavnost podatkov.
5. Sistem shrani prihodek in ga prikaže na seznamu prihodkov.
6. Sistem posodobi skupne prihodke in preostanek v tekočem mesecu.

**Alternativni tokovi:**
- **1a.** Uporabnik izbere obstoječi prihodek in možnost *Uredi* – sistem prikaže obrazec z obstoječimi podatki; nadaljuje se pri koraku 3.
- **1b.** Uporabnik izbere obstoječi prihodek in možnost *Izbriši* – sistem zahteva potrditev, prihodek izbriše in posodobi preostanek.
- **3a.** Uporabnik prekliče vnos – sistem zapre obrazec, nič se ne shrani.
- **4a.** Znesek je prazen, enak 0 ali negativen – sistem označi polje in izpiše *»Znesek mora biti večji od 0.«*; nadaljuje se pri koraku 3.
- **4b.** Datum ni veljaven ali manjka – sistem označi polje in zahteva popravek; nadaljuje se pri koraku 3.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Prihodka ni bilo mogoče shraniti. Poskusite znova.«*; vneseni podatki ostanejo v obrazcu.
- Seja uporabnika je potekla – sistem ga preusmeri na prijavo.

---

## PU5 – Pregled porabe

| **Primer uporabe:** Pregled porabe | **ID:** PU5 |
|---|---|

**Cilj:**
Uporabnik želi na enem mestu videti, koliko je v mesecu že porabil, koliko mu še ostane in kako je poraba razporejena po kategorijah.

**Akterji:**
Registriran uporabnik

**Predpogoji:**
- Uporabnik je prijavljen.

**Stanje sistema po PU:**
Stanje se ne spremeni – PU samo prikazuje podatke.

**Scenarij:**
1. Uporabnik odpre stran *Pregled*.
2. Sistem pridobi uporabnikove stroške, prihodke, mesečni budget in budgete kategorij za tekoči mesec.
3. Sistem izračuna skupno porabo, skupne prihodke in preostanek.
4. Sistem prikaže povzetek: porabljeno, prihodki, mesečni budget in preostanek.
5. Sistem prikaže razporeditev porabe po kategorijah (znesek, delež celotne porabe in delež porabljenega budgeta kategorije).
6. Sistem barvno označi kategorije, ki se približujejo budgetu ali so ga presegle.

**Alternativni tokovi:**
- **1a.** Uporabnik izbere drug mesec – sistem prikaže podatke za izbrani mesec; nadaljuje se pri koraku 2.
- **2a.** V mesecu ni nobenega stroška – sistem prikaže sporočilo *»V tem mesecu še ni stroškov.«* in povzetek z ničelno porabo.
- **2b.** Uporabnik ima neprebrana opozorila – sistem pred povzetkom izvede razširitev PU7 *Prikaz opozorila* («extend»); po zaprtju opozoril se nadaljuje pri koraku 3.
- **3a.** Mesečni budget ni nastavljen – sistem namesto preostanka prikaže razliko med prihodki in porabo ter povezavo na PU2.
- **5a.** Uporabnik klikne na kategorijo – sistem prikaže seznam stroškov te kategorije v izbranem mesecu.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Podatkov ni bilo mogoče naložiti.«* in ponudi gumb *Poskusi znova*.

---

## PU6 – Preverjanje budgeta

| **Primer uporabe:** Preverjanje budgeta | **ID:** PU6 |
|---|---|

**Cilj:**
Sistem po vsakem novem strošku ali spremembi budgeta ugotovi, ali se je poraba v kategoriji ali v celotnem mesecu približala budgetu ali ga presegla, da lahko uporabnika pravočasno opozori.

**Akterji:**
Registriran uporabnik (posredno, prek PU3 *Vnos stroška* ali PU2 *Upravljanje kategorij in budgetov*)

**Predpogoji:**
- PU6 se vedno izvede kot vključen del PU3 ali PU2 («include»). Izvede se tudi po samodejnem vnosu ponavljajočega se stroška (glej PU8).
- Strošek ali sprememba budgeta je že uspešno shranjena.

**Stanje sistema po PU:**
Stanje se ne spremeni (izvede se samo izračun). Če je dosežen prag opozorila, se sproži PU7.

**Scenarij:**
1. Sistem prejme podatke o pravkar shranjenem strošku (uporabnik, kategorija, znesek, datum).
2. Sistem izračuna uporabnikovo skupno porabo v kategoriji za mesec, v katerega spada strošek.
3. Sistem porabo primerja z budgetom kategorije.
4. Sistem izračuna uporabnikovo skupno porabo v mesecu in jo primerja z mesečnim budgetom.
5. Poraba je pod 80 % obeh budgetov – sistem zaključi preverjanje brez opozorila.

**Alternativni tokovi:**
- **1a.** PU6 je bil sprožen iz PU2 (sprememba budgeta) – sistem prejme spremenjeno kategorijo oz. mesec in nov budget; nadaljuje se pri koraku 2 (ob spremembi samo mesečnega budgeta pri koraku 4).
- **3a.** Kategorija nima nastavljenega budgeta – sistem preskoči primerjavo za kategorijo; nadaljuje se pri koraku 4.
- **4a.** Mesečni budget ni nastavljen – sistem preskoči primerjavo; nadaljuje se pri koraku 5.
- **5a.** Poraba v kategoriji ali mesecu doseže 80 % ali več budgeta – sistem sproži razširitev PU7 *Prikaz opozorila* (pogoj extend točke).

**Izjeme:**
- Pri izračunu pride do napake (npr. baza ni dosegljiva) – sistem napako zabeleži v dnevnik, strošek ostane shranjen, opozorilo se ne prikaže; preverjanje se ponovi ob naslednjem odprtju pregleda porabe.

---

## PU7 – Prikaz opozorila

| **Primer uporabe:** Prikaz opozorila | **ID:** PU7 |
|---|---|

**Cilj:**
Uporabnik želi biti pravočasno obveščen, ko se njegova poraba približa budgetu ali ga preseže, da lahko prilagodi nadaljnjo porabo.

**Akterji:**
Registriran uporabnik (prejemnik opozorila)

**Predpogoji:**
- PU7 razširja dva primera uporabe («extend»):
  - **PU6 *Preverjanje budgeta*** – pogoj razširitve: poraba v kategoriji ali v mesecu je dosegla 80 % ali več budgeta (izvedejo se koraki 1–6),
  - **PU5 *Pregled porabe*** – pogoj razširitve: uporabnik ima neprebrana opozorila (izvedejo se samo koraki 4–6 za vsa neprebrana opozorila).

**Stanje sistema po PU:**
Stanje se spremeni – opozorilo je shranjeno v uporabnikov seznam opozoril (z oznako *prebrano/neprebrano*).

**Scenarij:**
1. Sistem ugotovi vrsto opozorila: *približevanje* (80–100 %) ali *preseganje* (nad 100 %).
2. Sistem ustvari opozorilo z imenom kategorije (ali *mesečni budget*), porabljenim zneskom, budgetom in odstotkom porabe.
3. Sistem opozorilo shrani.
4. Sistem uporabniku prikaže obvestilo – rumeno za približevanje, rdeče za preseganje.
5. Uporabnik opozorilo prebere in ga zapre.
6. Sistem opozorilo označi kot prebrano.

**Alternativni tokovi:**
- **2a.** Za isto kategorijo in isti prag je bilo opozorilo v tem mesecu že prikazano – sistem novega opozorila ne ustvari (prepreči ponavljanje); PU se zaključi.
- **4a.** Uporabnik v tistem trenutku ni prijavljen (npr. strošek je bil vnesen samodejno) – opozorilo ostane neprebrano in se prikaže, ko uporabnik naslednjič odpre PU5 *Pregled porabe*.
- **5a.** Uporabnik klikne na opozorilo – sistem odpre PU5 *Pregled porabe* za ustrezno kategorijo.
- **5b.** Uporabnik opozorila ne zapre – opozorilo ostane med neprebranimi.

**Izjeme:**
- Opozorila ni mogoče shraniti – sistem ga vseeno prikaže v trenutni seji in napako zabeleži v dnevnik.

---

## PU8 – Upravljanje ponavljajočih se stroškov

| **Primer uporabe:** Upravljanje ponavljajočih se stroškov | **ID:** PU8 |
|---|---|

**Cilj:**
Uporabnik želi stroške, ki se ponavljajo vsak mesec (najemnina, telefon, Netflix, Spotify …), vnesti samo enkrat, da jih sistem nato vsak mesec samodejno zabeleži.

**Akterji:**
Registriran uporabnik

**Predpogoji:**
- Uporabnik je prijavljen.
- Uporabnik ima vsaj eno kategorijo.

**Stanje sistema po PU:**
Stanje se spremeni – ponavljajoči se strošek je dodan, spremenjen, začasno ustavljen ali izbrisan. Na dan zapadlosti sistem iz njega samodejno ustvari nov strošek.

**Scenarij:**
1. Uporabnik odpre seznam ponavljajočih se stroškov.
2. Sistem prikaže uporabnikove ponavljajoče se stroške z zneskom, kategorijo, dnevom v mesecu in stanjem (aktiven/ustavljen).
3. Uporabnik izbere *Dodaj ponavljajoči se strošek*.
4. Sistem prikaže obrazec: opis, znesek, kategorija, dan v mesecu (1–31), začetni mesec in neobvezni končni mesec.
5. Uporabnik izpolni obrazec in potrdi.
6. Sistem preveri veljavnost podatkov.
7. Sistem shrani ponavljajoči se strošek in ga prikaže na seznamu.
8. Na dan zapadlosti sistem samodejno ustvari nov strošek (z oznako *samodejno vnesen*) in zanj izvede PU6 *Preverjanje budgeta*.

**Alternativni tokovi:**
- **3a.** Uporabnik izbere obstoječi strošek in *Uredi* – sistem prikaže izpolnjen obrazec; spremembe veljajo od naslednjega vnosa naprej; nadaljuje se pri koraku 5.
- **3b.** Uporabnik izbere *Ustavi* / *Nadaljuj* – sistem spremeni stanje; ustavljeni stroški se ne vnašajo samodejno.
- **3c.** Uporabnik izbere *Izbriši* – sistem zahteva potrditev in izbriše ponavljajoči se strošek; že ustvarjeni stroški ostanejo.
- **5a.** Uporabnik prekliče – nič se ne shrani.
- **6a.** Znesek ni večji od 0, kategorija ni izbrana ali dan ni med 1 in 31 – sistem označi napačna polja; nadaljuje se pri koraku 5.
- **6b.** Končni mesec je pred začetnim – sistem izpiše napako; nadaljuje se pri koraku 5.
- **7a.** Začetni mesec je tekoči in je dan v mesecu že minil – sistem vpraša, ali naj strošek takoj vnese tudi za tekoči mesec.
- **8a.** Dan v mesecu je 29, 30 ali 31, mesec pa je krajši – sistem strošek vnese zadnji dan meseca.
- **8b.** Strošek za tekoči mesec že obstaja – sistem ga ne ustvari ponovno (preprečevanje podvajanja).
- **8c.** Strežnik na dan zapadlosti ni tekel – ob naslednjem zagonu sistem vnese vse zamujene stroške z izvirnim datumom zapadlosti.

**Izjeme:**
- Strežnik ali baza nista dosegljiva ob shranjevanju – sistem izpiše *»Sprememb ni bilo mogoče shraniti.«*; podatki ostanejo v obrazcu.
- Napaka pri samodejnem vnosu – sistem napako zabeleži, nadaljuje z naslednjim stroškom in neuspešen vnos poskusi znova ob naslednji izvedbi.

---

## PU9 – Upravljanje varčevalnih ciljev

| **Primer uporabe:** Upravljanje varčevalnih ciljev | **ID:** PU9 |
|---|---|

**Cilj:**
Uporabnik želi določiti cilj varčevanja (npr. *300 € za poletje*) in spremljati, koliko je že privarčeval in koliko mu še manjka.

**Akterji:**
Registriran uporabnik

**Predpogoji:**
- Uporabnik je prijavljen.

**Stanje sistema po PU:**
Stanje se spremeni – varčevalni cilj je ustvarjen, posodobljen, dopolnjen z vplačilom ali izbrisan.

**Scenarij:**
1. Uporabnik odpre stran *Varčevalni cilji*.
2. Sistem prikaže uporabnikove cilje z napredkom (privarčevano / ciljni znesek, odstotek, rok).
3. Uporabnik izbere *Nov cilj*.
4. Sistem prikaže obrazec: ime cilja, ciljni znesek in neobvezni rok.
5. Uporabnik izpolni obrazec in potrdi.
6. Sistem preveri podatke, shrani cilj in izračuna, koliko bi moral uporabnik privarčevati na mesec, da cilj doseže do roka.
7. Sistem prikaže nov cilj na seznamu.

**Alternativni tokovi:**
- **3a.** Uporabnik pri obstoječem cilju izbere *Dodaj vplačilo* – vnese znesek, sistem ga prišteje k privarčevanemu in posodobi napredek.
- **3b.** Na koncu meseca ima uporabnik pozitiven preostanek – sistem predlaga, da del preostanka nameni izbranemu cilju; uporabnik predlog sprejme ali zavrne.
- **3c.** Uporabnik izbere *Uredi* ali *Izbriši* – sistem prikaže obrazec oz. zahteva potrditev brisanja.
- **5a.** Uporabnik prekliče – nič se ne shrani.
- **6a.** Ciljni znesek ni večji od 0 ali je rok v preteklosti – sistem izpiše napako; nadaljuje se pri koraku 5.
- **6b.** Privarčevani znesek doseže ciljnega – sistem cilj označi kot *dosežen* in uporabniku čestita.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Cilja ni bilo mogoče shraniti.«*; podatki ostanejo v obrazcu.

---

## PU10 – Upravljanje uporabnikov

| **Primer uporabe:** Upravljanje uporabnikov | **ID:** PU10 |
|---|---|

**Cilj:**
Administrator želi imeti pregled nad uporabniškimi računi in jih po potrebi blokirati, odblokirati ali izbrisati (npr. ob zlorabi ali na željo uporabnika).

**Akterji:**
Administrator

**Predpogoji:**
- Administrator je prijavljen z računom, ki ima vlogo *administrator*.

**Stanje sistema po PU:**
Stanje se spremeni, če administrator račun blokira, odblokira ali izbriše. Pri samem pregledu seznama se stanje ne spremeni.

**Scenarij:**
1. Administrator odpre stran *Uporabniki*.
2. Sistem prikaže seznam uporabniških računov: ime, e-poštni naslov, datum registracije, vloga in stanje (aktiven/blokiran). Finančni podatki uporabnikov niso prikazani.
3. Administrator poišče uporabnika po imenu ali e-poštnem naslovu.
4. Administrator pri izbranem računu izbere *Blokiraj*.
5. Sistem zahteva potrditev.
6. Administrator potrdi.
7. Sistem račun označi kot blokiran in ga odjavi iz vseh aktivnih sej; podatki uporabnika ostanejo shranjeni.

**Alternativni tokovi:**
- **3a.** Iskanje ne vrne nobenega rezultata – sistem izpiše *»Ni najdenih uporabnikov.«*.
- **4a.** Administrator izbere *Odblokiraj* pri blokiranem računu – sistem račun ponovno aktivira; nadaljuje se pri koraku 5.
- **4b.** Administrator izbere *Izbriši* – sistem opozori, da bodo trajno izbrisani tudi vsi podatki uporabnika (stroški, prihodki, kategorije, cilji); po potrditvi račun in podatke izbriše.
- **4c.** Administrator poskuša blokirati ali izbrisati svoj račun – sistem dejanje zavrne z obvestilom *»Svojega računa ne morete blokirati ali izbrisati.«*.
- **6a.** Administrator prekliče – nič se ne spremeni.

**Izjeme:**
- Strežnik ali baza nista dosegljiva – sistem izpiše *»Dejanja ni bilo mogoče izvesti. Poskusite znova.«*; stanje računa ostane nespremenjeno.
- Uporabnik brez vloge administratorja poskuša odpreti stran *Uporabniki* – sistem dostop zavrne (*403 – Dostop zavrnjen*) in ga preusmeri na pregled porabe.