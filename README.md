# 💸 $troško – tracker za stroške

> Spletna aplikacija za pregled nad osebnimi financami – namenjena študentom in mladim, ki prvič samostojno upravljajo s svojim denarjem.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-336791?logo=postgresql)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-6.0-3178C6?logo=typescript)
![Vite](https://img.shields.io/badge/Vite-8-646CFF?logo=vite)

---

## 📑 Kazalo

1. [Člani skupine](#-člani-skupine)
2. [Vizija projekta](#-vizija-projekta)
   - [Opis rešitve](#opis-rešitve)
   - [Ključne funkcionalnosti](#ključne-funkcionalnosti)
   - [Obseg rešitve in morebitne omejitve](#obseg-rešitve-in-morebitne-omejitve)
3. [Besednjak](#-besednjak)
4. [Diagram primerov uporabe](#-diagram-primerov-uporabe)
   - [Uporabniške zgodbe](#uporabniške-zgodbe)
5. [Razredni diagram](#-razredni-diagram)
   - [Vloga in namen razredov](#vloga-in-namen-razredov)
   - [Ključne metode](#ključne-metode)
6. [Uporabljene tehnologije](#-uporabljene-tehnologije)
7. [Struktura projekta](#-struktura-projekta)
8. [Navodila za nameščanje in zagon](#-navodila-za-nameščanje-in-zagon)
9. [REST API](#-rest-api)
10. [Navodila za razvijalce](#-navodila-za-razvijalce)
11. [Standardi kodiranja](#-standardi-kodiranja)

---

## 👥 Člani skupine

| Ime in priimek | GitHub |
|---|---|
| Staša Despotović | [@stasadespotovic1](https://github.com/stasadespotovic1) |
| Nikola Mojsov | [@mojsov](https://github.com/mojsov) |
| Lea Bogoevska | [@leabogoevska26](https://github.com/leabogoevska26) |

---

## 🔭 Vizija projekta

Študenti in mladi, ki se prvič sami preživljajo, pogosto ne vedo, kam jim gre denar. Večjih stroškov, kot sta najemnina in naročnina za telefon, se zavedajo, drobni vsakodnevni stroški (kava, malica, prevoz, aplikacije) pa se neopazno naberejo. Ob koncu meseca jih zato pogosto preseneti, da jim je zmanjkalo denarja.

**$troško** želi ta problem rešiti s preprosto spletno aplikacijo, ki uporabniku v vsakem trenutku pokaže, **koliko je v tekočem mesecu že porabil, koliko mu še ostane in za kaj je denar porabil**. Aplikacija ga pravočasno opozori, ko se približa budgetu, in ga spodbuja k varčevanju. Naš cilj je, da uporabnik z nekaj sekundami vnosa na dan pridobi popoln pregled nad svojimi financami in sprejema premišljene odločitve, namesto da težave odkrije šele na koncu meseca.

### Opis rešitve

Nov obiskovalec si v aplikaciji najprej **ustvari uporabniški račun**. Po prijavi vidi in ureja **samo svoje podatke**, zato lahko aplikacijo hkrati uporablja več uporabnikov.

Registriran uporabnik si ustvari svoje **kategorije** (hrana, prevoz, zabava, naročnine …) in jim po želji določi mesečni **budget**. Nastavi lahko tudi splošni **mesečni budget**. Nato sproti vnaša **stroške** in **prihodke**. Aplikacija iz vnesenih podatkov izračuna porabo po kategorijah in **preostanek** do konca meseca ter ju pregledno prikaže. Ob vsakem novem strošku sistem preveri, ali se poraba približuje budgetu, in uporabnika po potrebi **opozori**.

Stroške, ki se ponavljajo vsak mesec (najemnina, Netflix, Spotify …), uporabnik vnese le enkrat, sistem pa jih nato **samodejno** zabeleži ob pravem dnevu. Z **varčevalnimi cilji** lahko uporabnik spremlja, koliko je že privarčeval za večji nakup ali poletje.

**Administrator** skrbi za delovanje sistema: pregleduje uporabniške račune, jih lahko blokira, odblokira ali izbriše. Do finančnih podatkov uporabnikov nima dostopa.

#### Ključne funkcionalnosti

| Funkcionalnost | Opis | PU | Status |
|---|---|---|---|
| Registracija | Ustvarjanje uporabniškega računa z imenom, e-pošto in geslom | PU1 | ✅ |
| Prijava in odjava | Prijava z e-pošto in geslom; vsak uporabnik vidi samo svoje podatke | – (predpogoj za PU2–PU10) | ✅ |
| Kategorije | Ustvarjanje, urejanje in brisanje lastnih kategorij (hrana, prevoz, zabava, naročnine …) | PU2 | ✅ |
| Budget po kategorijah | Vsaka kategorija ima lahko svoj mesečni budget | PU2 | ✅ |
| Mesečni budget | Splošen mesečni budget in prikaz, koliko ga v tekočem mesecu še ostane | PU2 | ✅ |
| Vnos stroškov | Vsak zapis ima znesek, datum, kratek opis in kategorijo | PU3 | ✅ |
| Vnos prihodkov | Štipendija, študentsko delo, žepnina … | PU4 | 🚧 |
| Pregled porabe | Poraba v izbranem mesecu po kategorijah, delež, presežen budget in primerjava s prejšnjim mesecem | PU5 | ✅ poraba po kategorijah · 🚧 prihodki v pregledu |
| Preverjanje budgeta in opozorila | Ob vsakem novem strošku ali spremembi budgeta sistem preveri porabo in uporabnika opozori ob približevanju (80 %) ali preseganju budgeta | PU6, PU7 | ✅ |
| Ponavljajoči se stroški | Najemnina, telefon, Netflix, Spotify … se vnesejo enkrat in se samodejno upoštevajo vsak mesec | PU8 | 📝 |
| Varčevalni cilji | Npr. *300 € za poletje* – spremljanje napredka in koliko ostane za cilj na koncu meseca | PU9 | 📝 |
| Upravljanje uporabnikov | Administrator pregleduje, blokira, odblokira in briše uporabniške račune | PU10 | 📝 |

<sub>✅ implementirano · 🚧 v razvoju · 📝 načrtovano</sub>

### Obseg rešitve in morebitne omejitve

**V obsegu rešitve je:**

- registracija, prijava in odjava; ločeni podatki za vsakega uporabnika,
- ročni vnos, urejanje in brisanje stroškov, prihodkov in kategorij,
- nastavitev budgeta za posamezno kategorijo in splošnega mesečnega budgeta,
- mesečni pregled porabe, preostanka in razporeditve po kategorijah,
- opozorila v aplikaciji ob približevanju ali preseganju budgeta,
- mesečno ponavljajoči se stroški s samodejnim vnosom,
- varčevalni cilji z ročnimi vplačili in spremljanjem napredka,
- administracija uporabniških računov (pregled, blokiranje, brisanje).

**Omejitve (izven obsega te različice):**

- **Ni povezave z bankami ali karticami** – vsi stroški in prihodki se vnašajo ročno. Natančnost pregleda je zato odvisna od rednega vnašanja.
- **Ni deljenih računov ali stroškov** – vsak uporabnik ima svoje ločene finance; skupni stroški (npr. med cimri) niso podprti.
- **Prijava samo z e-pošto in geslom** – prijava prek Googla, Facebooka ipd. ni podprta.
- **Administrator nima vpogleda v finance uporabnikov** – upravlja samo račune, ne pa stroškov, prihodkov ali kategorij uporabnikov.
- **Ena valuta (EUR)** – pretvorbe med valutami niso podprte.
- **Ponavljanje samo na mesečni ravni** – tedenski ali letni ponavljajoči se stroški niso podprti.
- **Opozorila samo znotraj aplikacije** – ni e-poštnih ali push obvestil.
- **Samo spletna aplikacija** – ni namenske mobilne aplikacije (je pa vmesnik uporaben tudi v mobilnem brskalniku).
- **Brez investicij, kreditov in davkov** – aplikacija je namenjena spremljanju vsakodnevne porabe, ne celovitemu finančnemu načrtovanju.
- Uvoz/izvoz podatkov (CSV, PDF poročila) za zdaj ni načrtovan.

---

## 📚 Besednjak

| Izraz | Razlaga |
|---|---|
| **Neregistriran uporabnik** | Obiskovalec aplikacije, ki še nima uporabniškega računa. Lahko se samo registrira. |
| **Registriran uporabnik** | Uporabnik z uporabniškim računom, ki po prijavi spremlja svoje stroške, prihodke, budgete in varčevalne cilje. |
| **Administrator** | Registriran uporabnik s posebnimi pravicami, ki upravlja uporabniške račune. Do financ drugih uporabnikov nima dostopa. |
| **Uporabniški račun** | Podatki, s katerimi se uporabnik prijavi (e-pošta, geslo, ime). Vsi stroški, prihodki in kategorije pripadajo natanko enemu računu. |
| **Blokiran račun** | Uporabniški račun, ki ga je administrator onemogočil; prijava z njim ni mogoča, podatki pa ostanejo shranjeni. |
| **Strošek** (odhodek) | Posamezen izdatek uporabnika. Ima znesek, datum, kratek opis in natanko eno kategorijo. |
| **Prihodek** | Denar, ki ga uporabnik prejme (štipendija, plačilo študentskega dela, žepnina …). Ima znesek, datum, vir in opis. |
| **Vir prihodka** | Vrsta prihodka, npr. štipendija, študentsko delo, žepnina ali drugo. |
| **Kategorija** | Skupina, v katero uporabnik razvršča stroške (npr. hrana, prevoz, zabava, naročnine). Kategorije si uporabnik ustvari sam. |
| **Budget kategorije** | Največji znesek, ki ga uporabnik namerava v enem mesecu porabiti v določeni kategoriji. |
| **Mesečni budget** | Skupni znesek, ki ga uporabnik namerava porabiti v celotnem mesecu, ne glede na kategorijo. |
| **Poraba** | Vsota vseh stroškov v izbranem obdobju (mesecu) – skupno ali za posamezno kategorijo. |
| **Preostanek** | Znesek, ki uporabniku še ostane v mesecu: mesečni budget − poraba (če budget ni nastavljen: prihodki − poraba). |
| **Prag opozorila** | Delež porabljenega budgeta, pri katerem sistem uporabnika opozori – 80 % (približevanje) in 100 % (preseganje). |
| **Opozorilo** | Obvestilo, ki ga sistem prikaže, ko poraba v kategoriji ali mesecu doseže prag opozorila. |
| **Ponavljajoči se strošek** | Predloga za strošek, ki se ponovi vsak mesec na isti dan (najemnina, telefon, Netflix …). Iz nje sistem samodejno ustvari običajen strošek. |
| **Samodejni vnos** | Postopek, s katerim sistem na dan zapadlosti iz ponavljajočega se stroška ustvari nov strošek brez posredovanja uporabnika. |
| **Varčevalni cilj** | Znesek, ki ga želi uporabnik privarčevati za določen namen, po želji do določenega roka (npr. *300 € za poletje*). |
| **Vplačilo v cilj** | Znesek, ki ga uporabnik nameni varčevalnemu cilju in poveča privarčevani znesek. |
| **Napredek cilja** | Razmerje med privarčevanim in ciljnim zneskom, prikazano v odstotkih. |

---

## 🧩 Diagram primerov uporabe

Diagram prikazuje načrtovano različico aplikacije z vsemi ključnimi funkcionalnostmi.

![Diagram primerov uporabe](DPU_canvas.drawio.png)

<sub>Izvorna datoteka za urejanje v [draw.io](https://app.diagrams.net/): [`DPU.drawio`](DPU.drawio) · Razlaga povezav: [`docs/diagram-primerov-uporabe.md`](docs/diagram-primerov-uporabe.md)</sub>

**Akterji**

| Akter | Opis |
|---|---|
| **Neregistriran uporabnik** | Obiskovalec brez računa, ki se lahko registrira. |
| **Registriran uporabnik** | Študent ali mlada oseba, ki po prijavi uporablja aplikacijo za spremljanje svojih financ. |
| **Administrator** | Specializacija registriranega uporabnika (generalizacija akterjev): lahko izvaja vse primere uporabe registriranega uporabnika, poleg tega pa upravlja uporabniške račune. |

**Primeri uporabe**

| ID | Primer uporabe | Akter | Status |
|---|---|---|---|
| PU1 | Registracija | Neregistriran uporabnik | ✅ |
| PU2 | Upravljanje kategorij in budgetov | Registriran uporabnik | ✅ |
| PU3 | Vnos stroška | Registriran uporabnik | ✅ |
| PU4 | Vnos prihodka | Registriran uporabnik | 🚧 |
| PU5 | Pregled porabe | Registriran uporabnik | ✅ poraba po kategorijah · 🚧 prihodki v pregledu |
| PU6 | Preverjanje budgeta | – (vključen v PU2 in PU3) | ✅ |
| PU7 | Prikaz opozorila | – (razširja PU5 in PU6) | ✅ |
| PU8 | Upravljanje ponavljajočih se stroškov | Registriran uporabnik | 📝 |
| PU9 | Upravljanje varčevalnih ciljev | Registriran uporabnik | 📝 |
| PU10 | Upravljanje uporabnikov | Administrator | 📝 |

> **Opomba:** Prijava ni samostojen primer uporabe, saj sama po sebi ni cilj uporabnika, temveč **predpogoj** za vse primere uporabe registriranega uporabnika in administratorja.

📄 **Podroben opis primerov uporabe:** [`primeri_uporabe.md`](primeri_uporabe.md)

### Uporabniške zgodbe

📋 Uporabniške zgodbe s sprejemnimi kriteriji, podrobnostmi implementacije in odgovornimi člani: [`docs/user-stories.md`](docs/user-stories.md)

---

## 🧱 Razredni diagram

Razredni diagram prikazuje strukturo celotne načrtovane aplikacije – vse funkcionalnosti iz diagrama primerov uporabe (PU1–PU10), ne le tistih, ki so že implementirane.

![Razredni diagram](razredni-diagram.drawio.png)

<sub>Izvorna datoteka za urejanje v [draw.io](https://app.diagrams.net/): [`razredni-diagram.drawio`](razredni-diagram.drawio)</sub>

Razredi so po vlogi označeni z barvami: **modra** – uporabniki in prijava, **rumena** – domenski razredi (podatki uporabnika), **zelena** – naštevni tipi (`«enumeration»`), **vijolična** – storitve s poslovno logiko (`«service»`), **siva** – rezultat pregleda porabe. Diagram uporablja generalizacijo (`Administrator`, `Strosek`, `Prihodek`), kompozicijo (deli, ki brez uporabnika oz. cilja ne obstajajo), asociacije s kardinalnostmi ter odvisnosti `«use»`, `«instantiate»` in `«call»`, ki prikazujejo, katere razrede storitve uporabljajo in ustvarjajo.

### Vloga in namen razredov

| Razred | Vloga in namen | PU |
|---|---|---|
| `Uporabnik` | Registriran uporabnik z imenom, e-pošto in zgoščenim geslom. Je lastnik vseh svojih podatkov (kategorij, transakcij, budgetov, ciljev, opozoril, sej), zato vsak uporabnik vidi samo svoje finance. | PU1–PU9 |
| `Administrator` | Specializacija uporabnika (dedovanje) s pravicami za upravljanje uporabniških računov; do financ uporabnikov nima dostopa. | PU10 |
| `Seja` | Prijavna seja z naključnim žetonom in časom poteka; omogoča, da uporabnik ostane prijavljen, in odjavo. | prijava |
| `AvtentikacijaService` | Registracija, prijava, odjava in preverjanje veljavnosti seje. | PU1, prijava |
| `Transakcija` *(abstraktni)* | Skupni nadrazred za premike denarja z zneskom, datumom in opisom. | PU3, PU4 |
| `Strosek` | Izdatek uporabnika; lahko spada v kategorijo in je lahko samodejno vnesen iz ponavljajočega se stroška. | PU3 |
| `Prihodek` | Prejeti denar z virom (`VirPrihodka`). | PU4 |
| `VirPrihodka` | Naštevni tip virov prihodka: štipendija, študentsko delo, žepnina, drugo. | PU4 |
| `Kategorija` | Skupina stroškov (hrana, prevoz …) z neobveznim mesečnim budgetom. | PU2 |
| `MesecniBudzet` | Splošni budget uporabnika za določen mesec. | PU2 |
| `PreverjanjeBudgetaService` | Po vsakem novem strošku ali spremembi budgeta primerja porabo z budgeti in ustvari ali uskladi opozorila. | PU6 |
| `Opozorilo` | Shranjeno opozorilo o porabi budgeta kategorije ali mesečnega budgeta za določen mesec; ostane, dokler ga uporabnik ne prebere. | PU7 |
| `StopnjaOpozorila` | Naštevni tip stopenj opozorila: približevanje (80 %) in preseganje (100 %). | PU7 |
| `PregledPorabeService` | Izračuna mesečni pregled porabe za izbranega uporabnika in mesec. | PU5 |
| `PregledPorabe` | Rezultat pregleda: skupna poraba, primerjava s prejšnjim mesecem, prihodki, preostanek in neprebrana opozorila. | PU5 |
| `PostavkaPregleda` | Ena vrstica pregleda: poraba kategorije, delež, primerjava s prejšnjim mesecem in ali je budget presežen. | PU5 |
| `PonavljajociStrosek` | Predloga za strošek, ki se ponovi vsak mesec na isti dan; lahko je aktivna ali ustavljena. | PU8 |
| `SamodejniVnosService` | Na dan zapadlosti iz ponavljajočih se stroškov samodejno ustvari stroške in zanje sproži preverjanje budgeta. | PU8 |
| `VarcevalniCilj` | Cilj varčevanja s ciljnim zneskom in rokom ter spremljanjem napredka. | PU9 |
| `VplaciloVCilj` | Posamezno vplačilo, ki poveča privarčevani znesek cilja. | PU9 |

### Ključne metode

| Funkcionalnost | Metoda | Naloga |
|---|---|---|
| Registracija in prijava | `AvtentikacijaService.registriraj(ime, email, geslo): Seja` | Preveri podatke in edinstvenost e-pošte, shrani uporabnika z zgoščenim geslom in ga samodejno prijavi. |
| | `AvtentikacijaService.prijavi(email, geslo): Seja` | Z `Uporabnik.preveriGeslo(geslo)` preveri geslo in ustvari novo sejo. |
| | `AvtentikacijaService.preveriSejo(token): Uporabnik` / `Seja.jeVeljavna()` | Pred vsako zahtevo ugotovi, kateri uporabnik je prijavljen in ali seja še velja. |
| | `AvtentikacijaService.odjavi(token)` | Razveljavi sejo. |
| Kategorije in budgeti | `Uporabnik.dodajKategorijo(kategorija)`, `Kategorija.posodobi(naziv, budzet)` | Dodajanje in urejanje lastnih kategorij in njihovih budgetov. |
| | `Uporabnik.nastaviMesecniBudzet(mesec, znesek)` | Nastavi splošni budget za izbrani mesec. |
| | `Kategorija.porabaVMesecu(mesec): double` | Vrne porabo kategorije v mesecu (osnova za preverjanje in pregled). |
| Vnos stroškov in prihodkov | `Uporabnik.dodajStrosek(strosek)`, `Uporabnik.dodajPrihodek(prihodek)` | Shrani novo transakcijo uporabnika; po stroških se sproži preverjanje budgeta. |
| | `Transakcija.getMesec(): YearMonth` | Določi mesec, v katerega transakcija spada. |
| Preverjanje budgeta in opozorila | `PreverjanjeBudgetaService.preveri(uporabnik, datum)` | Za mesec datuma izračuna porabo po kategorijah in skupno porabo ter ustvari, osveži ali izbriše opozorila. |
| | `PreverjanjeBudgetaService.dolociStopnjo(poraba, budzet): StopnjaOpozorila` | Glede na pragova 80 % in 100 % določi stopnjo opozorila (ali je ni). |
| | `Opozorilo.oznaciPrebrano()` | Uporabnik zapre opozorilo. |
| Pregled porabe | `PregledPorabeService.izracunajPregled(uporabnik, mesec): PregledPorabe` | Iz transakcij izbranega in prejšnjega meseca sestavi pregled s postavkami po kategorijah. |
| Ponavljajoči se stroški | `PonavljajociStrosek.jeZapadel(datum): boolean` | Ugotovi, ali je strošek na ta dan treba vnesti. |
| | `PonavljajociStrosek.ustvariStrosek(datum): Strosek` | Iz predloge ustvari nov strošek z oznako *samodejno vnesen*. |
| | `SamodejniVnosService.izvediSamodejniVnos(datum)` | Za vse zapadle aktivne predloge ustvari stroške in pokliče preverjanje budgeta. |
| | `PonavljajociStrosek.ustavi()` / `nadaljuj()` | Začasno ustavi ali ponovno vklopi samodejni vnos. |
| Varčevalni cilji | `VarcevalniCilj.dodajVplacilo(znesek): VplaciloVCilj` | Doda vplačilo in ob dosegu ciljnega zneska cilj označi kot dosežen. |
| | `VarcevalniCilj.napredek()`, `privarcevano()`, `mesecniZnesekDoRoka()` | Izračunajo odstotek napredka, privarčevani znesek in koliko je treba privarčevati na mesec do roka. |
| Upravljanje uporabnikov | `Administrator.pregledUporabnikov(iskanje): List<Uporabnik>` | Iskanje in pregled uporabniških računov. |
| | `Administrator.blokiraj(uporabnik)` / `odblokiraj(uporabnik)` / `izbrisi(uporabnik)` | Onemogoči ali ponovno omogoči prijavo oz. trajno izbriše račun z vsemi podatki. |

---

## 🛠 Uporabljene tehnologije

### Backend

| Tehnologija | Verzija | Namen |
|---|---|---|
| [Java (JDK)](https://adoptium.net/) | 21 | Programski jezik |
| [Spring Boot](https://spring.io/projects/spring-boot) | 4.1.1 | Ogrodje za REST API |
| Spring Web MVC | (Spring Boot 4.1.1) | REST kontrolerji |
| Spring Data JPA / Hibernate | (Spring Boot 4.1.1) | ORM in dostop do baze |
| [PostgreSQL JDBC driver](https://jdbc.postgresql.org/) | (Spring Boot 4.1.1) | Povezava s PostgreSQL |
| Spring Validation (Jakarta Bean Validation) | (Spring Boot 4.1.1) | Preverjanje vhodnih podatkov (`@Email`, `@Size` …) |
| Spring Security Crypto | (Spring Boot 4.1.1) | Zgoščevanje gesel z BCrypt |
| [Lombok](https://projectlombok.org/) | 1.18.48 | Zmanjšanje "boilerplate" kode (getterji, setterji …) |
| [springdoc-openapi](https://springdoc.org/) | 3.0.0 | Swagger UI in OpenAPI dokumentacija |
| [Maven](https://maven.apache.org/) (wrapper) | 3.10.0 | Upravljanje odvisnosti in gradnja |

### Frontend

| Tehnologija | Verzija | Namen |
|---|---|---|
| [React](https://react.dev/) | 19.3 | UI knjižnica |
| [TypeScript](https://www.typescriptlang.org/) | 6.0 | Tipiziran JavaScript |
| [Vite](https://vite.dev/) | 8.3 | Razvojni strežnik in gradnja |
| [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react) | 6.1 | React podpora za Vite |
| [ESLint](https://eslint.org/) + typescript-eslint | 10.13 / 8.71 | Statična analiza kode |

### Podatkovna baza in orodja

| Orodje | Verzija | Namen |
|---|---|---|
| [PostgreSQL](https://www.postgresql.org/) | 16 ali novejši | Relacijska podatkovna baza |
| [Node.js](https://nodejs.org/) | 20.19+ ali 22.12+ | Izvajanje frontenda |
| Git + GitHub | – | Verzioniranje kode |
| IntelliJ IDEA / VS Code | – | Razvojno okolje |

---

## 🗂 Struktura projekta

```
Strosko/
├── backend/                         # Spring Boot REST API
│   ├── .mvn/wrapper/                # Maven wrapper (ni potrebna lokalna namestitev Mavena)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/si/um/feri/backend/
│   │   │   │   ├── BackendApplication.java   # Vstopna točka aplikacije
│   │   │   │   ├── config/
│   │   │   │   │   └── WebConfig.java        # CORS, preverjanje prijave, BCrypt
│   │   │   │   ├── vao/                      # Entitete (modeli podatkov)
│   │   │   │   │   ├── Kategorija.java
│   │   │   │   │   ├── Opozorilo.java
│   │   │   │   │   ├── Seja.java
│   │   │   │   │   ├── StopnjaOpozorila.java
│   │   │   │   │   ├── Strosek.java
│   │   │   │   │   ├── Uporabnik.java
│   │   │   │   │   └── Vloga.java
│   │   │   │   ├── dao/                      # Repozitoriji (dostop do baze)
│   │   │   │   │   ├── KategorijaRepository.java
│   │   │   │   │   ├── OpozoriloRepository.java
│   │   │   │   │   ├── SejaRepository.java
│   │   │   │   │   ├── StrosekRepository.java
│   │   │   │   │   └── UporabnikRepository.java
│   │   │   │   ├── dto/                      # Podatki zahtev/odgovorov (prijava, opozorila, pregled …)
│   │   │   │   ├── service/
│   │   │   │   │   └── PreverjanjeBudgetaService.java  # Preverjanje budgeta in usklajevanje opozoril
│   │   │   │   └── rest/                     # REST kontrolerji (API končne točke)
│   │   │   │       ├── AuthInterceptor.java  # Preverjanje žetona prijavljenega uporabnika
│   │   │   │       ├── InfoController.java
│   │   │   │       ├── KategorijaController.java
│   │   │   │       ├── MesecniBudzetController.java
│   │   │   │       ├── NapakaHandler.java    # Enotna oblika sporočil o napakah
│   │   │   │       ├── OpozorilaController.java
│   │   │   │       ├── PregledController.java    # Pregled porabe po kategorijah
│   │   │   │       ├── StrosekController.java
│   │   │   │       └── UporabnikController.java  # Registracija, prijava, odjava
│   │   │   └── resources/
│   │   │       └── application.properties    # Konfiguracija (port, baza, JPA)
│   │   └── test/                             # Testi (npr. PreverjanjeBudgetaServiceTest)
│   ├── .env.example                 # Predloga za lokalne nastavitve (geslo baze)
│   ├── mvnw / mvnw.cmd              # Skripti za zagon Maven wrapperja
│   └── pom.xml                      # Maven odvisnosti in nastavitve gradnje
│
├── frontend/                        # React + TypeScript odjemalec
│   ├── public/                      # Statične datoteke (favicon, ikone)
│   ├── src/
│   │   ├── components/              # React komponente
│   │   │   ├── KategorijaForm.tsx   # Obrazec za dodajanje/urejanje kategorije
│   │   │   ├── KategorijaList.tsx   # Seznam kategorij
│   │   │   ├── MesecniBudzetForm.tsx # Nastavitev mesečnega budgeta
│   │   │   ├── OpozorilaBanner.tsx  # Prikaz opozoril o budgetu
│   │   │   ├── Pregled.tsx          # Glavni zaslon prijavljenega uporabnika
│   │   │   ├── PregledPorabe.tsx    # Pregled porabe po kategorijah za izbrani mesec
│   │   │   ├── PrijavaForm.tsx      # Obrazec za prijavo
│   │   │   ├── RegistracijaForm.tsx # Obrazec za registracijo
│   │   │   ├── StrosekForm.tsx      # Obrazec za dodajanje/urejanje stroška
│   │   │   └── StrosekList.tsx      # Seznam stroškov
│   │   ├── App.tsx                  # Glavna komponenta aplikacije
│   │   ├── api.ts                   # Klici na REST API (fetch)
│   │   ├── types.ts                 # TypeScript tipi (Strosek, Kategorija …)
│   │   ├── format.ts                # Pomožne funkcije za formatiranje (zneski, datumi)
│   │   ├── index.css                # Globalni stili
│   │   └── main.tsx                 # Vstopna točka React aplikacije
│   ├── index.html
│   ├── package.json                 # npm odvisnosti in skripte
│   ├── tsconfig*.json               # Nastavitve TypeScript prevajalnika
│   ├── eslint.config.js             # Nastavitve ESLint
│   └── vite.config.ts               # Nastavitve Vite
│
├── docs/                            # Podporna dokumentacija
│   ├── diagram-primerov-uporabe.md  # Razlaga povezav na diagramu primerov uporabe
│   └── user-stories.md              # Uporabniške zgodbe in sprejemni kriteriji
│
├── DPU.drawio                       # Diagram primerov uporabe (izvor za draw.io)
├── DPU_canvas.drawio.png            # Diagram primerov uporabe (slika)
├── razredni-diagram.drawio          # Razredni diagram (izvor za draw.io)
├── razredni-diagram.drawio.png      # Razredni diagram (slika)
├── primeri_uporabe.md               # Podroben opis primerov uporabe
└── README.md
```

### Razlaga map

**Backend** sledi slojni arhitekturi:

| Mapa | Vloga |
|---|---|
| `vao/` | *Value Access Objects* – JPA entitete, ki predstavljajo tabele v bazi (`Uporabnik`, `Seja`, `Kategorija`, `Strosek`, `Opozorilo`). |
| `dao/` | *Data Access Objects* – Spring Data JPA repozitoriji za CRUD operacije nad entitetami. |
| `dto/` | Podatki, ki jih REST API sprejme ali vrne, kadar se razlikujejo od entitet (npr. uporabnik brez gesla). |
| `service/` | Poslovna logika, ki jo uporablja več kontrolerjev (preverjanje budgeta in opozorila). |
| `rest/` | REST kontrolerji, ki sprejemajo HTTP zahteve, validirajo podatke in kličejo repozitorije. |
| `config/` | Spletna konfiguracija: CORS, preverjanje prijave in zgoščevanje gesel. |
| `resources/` | Konfiguracijske datoteke aplikacije. |

**Frontend** je razdeljen na:

| Mapa / datoteka | Vloga |
|---|---|
| `components/` | Ponovno uporabne React komponente (obrazci, seznami). |
| `api.ts` | Edino mesto, kjer frontend komunicira z backendom. |
| `types.ts` | Skupni tipi, ki odražajo podatkovni model backenda. |

---

## 🚀 Navodila za nameščanje in zagon

### 1. Predpogoji

Pred začetkom namestite:

| Orodje | Preverjanje namestitve |
|---|---|
| **Java JDK 21** ([Temurin](https://adoptium.net/)) | `java -version` |
| **PostgreSQL 16+** ([prenos](https://www.postgresql.org/download/)) | `psql --version` |
| **Node.js 20.19+ / 22.12+** in npm ([prenos](https://nodejs.org/)) | `node -v` in `npm -v` |
| **Git** | `git --version` |

> 💡 Maven ni potrebno nameščati posebej – projekt vsebuje Maven wrapper (`mvnw`).
> Poskrbite, da je spremenljivka okolja `JAVA_HOME` nastavljena na JDK 21.

### 2. Kloniranje repozitorija

```bash
git clone https://github.com/stasadespotovic1/Strosko.git
cd Strosko
```

### 3. Vzpostavitev podatkovne baze

1. Zaženite PostgreSQL strežnik (privzeto na portu `5432`).
2. Ustvarite bazo `stroski`:

   ```bash
   psql -U postgres -c "CREATE DATABASE stroski;"
   ```

   ali preko pgAdmin: *Databases → Create → Database…* → ime `stroski`.

3. Tabel ni treba ustvarjati ročno – Hibernate jih ob zagonu ustvari/posodobi samodejno (`spring.jpa.hibernate.ddl-auto=update`).

### 4. Okoljske spremenljivke

Nastavitve backenda so v [`backend/src/main/resources/application.properties`](backend/src/main/resources/application.properties). **Gesla za bazo ne vpisujte v repozitorij.** Backend ga prebere iz lokalne datoteke `backend/.env`, ki je v `.gitignore`:

```bash
cd backend
cp .env.example .env          # Windows (PowerShell): Copy-Item .env.example .env
```

V `backend/.env` vpišite svoje podatke (vrstice oblike `KLJUC=vrednost`, brez narekovajev):

```
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=vase_geslo
```

| Spremenljivka | Privzeta vrednost | Opis |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/stroski` | Naslov baze |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Uporabnik baze |
| `SPRING_DATASOURCE_PASSWORD` | – (obvezno) | Geslo uporabnika baze |
| `SERVER_PORT` | `8100` | Port backenda |

> Datoteka `.env` se išče v mapi, iz katere zaženete backend, zato ga zaženite iz mape `backend/` (v IntelliJ IDEA nastavite *Working directory* na `backend`). Namesto `.env` lahko vrednosti nastavite tudi kot običajne okoljske spremenljivke.

### 5. Zagon backenda

```bash
cd backend
./mvnw spring-boot:run        # Linux / macOS
mvnw.cmd spring-boot:run      # Windows
```

Backend teče na **http://localhost:8100/api/v1**.

| Povezava | Opis |
|---|---|
| http://localhost:8100/api/v1/info | Preverjanje, ali backend deluje |
| http://localhost:8100/api/v1/swagger-ui/index.html | Swagger UI – interaktivna dokumentacija API-ja |

### 6. Zagon frontenda

V novem terminalu:

```bash
cd frontend
npm install       # samo prvič oz. ob spremembi odvisnosti
npm run dev
```

Frontend teče na **http://localhost:5173**.

> Naslov backenda je nastavljen v [`frontend/src/api.ts`](frontend/src/api.ts) (konstanta `BASE`).

### Uporabne npm skripte

| Ukaz | Opis |
|---|---|
| `npm run dev` | Zažene razvojni strežnik z "hot reload" |
| `npm run build` | Preveri tipe in zgradi produkcijsko verzijo v `dist/` |
| `npm run preview` | Lokalno prikaže produkcijsko verzijo |
| `npm run lint` | Preveri kodo z ESLint |

### Težave pri zagonu

| Težava | Rešitev |
|---|---|
| `Connection refused` na `localhost:5432` | PostgreSQL ne teče – zaženite storitev. |
| `database "stroski" does not exist` | Ustvarite bazo (korak 3). |
| `password authentication failed` ali `Could not resolve placeholder 'SPRING_DATASOURCE_PASSWORD'` | Preverite, da `backend/.env` obstaja (in ni shranjena kot `.env.txt`) ter vsebuje pravilno geslo. |
| Napaka pri dodajanju stolpca `uporabnik_id` ob zagonu | Baza ima stroške ali kategorije iz različice brez uporabnikov. Bazo enkrat ponovno ustvarite: `DROP DATABASE stroski;` in `CREATE DATABASE stroski;`. |
| `Port 8100 already in use` | Ustavite drug proces ali nastavite `SERVER_PORT`. |
| Napake CORS v brskalniku | Preverite, da backend teče in da se `BASE` v `api.ts` ujema s portom. |

---

## 🔌 REST API

Osnovni URL: `http://localhost:8100/api/v1`

Vse končne točke razen `/info`, `/auth/registracija` in `/auth/prijava` zahtevajo prijavo: odjemalec pošlje žeton, ki ga vrne registracija ali prijava, v glavi `Authorization: Bearer <žeton>`. Brez veljavnega žetona strežnik vrne `401`. Kategorije in stroški se vedno nanašajo samo na prijavljenega uporabnika; tuj zapis vrne `404`.

| Metoda | Končna točka | Opis |
|---|---|---|
| `GET` | `/info` | Informacija o delovanju strežnika |
| `POST` | `/auth/registracija` | Nov uporabniški račun (vrne žeton – uporabnik je takoj prijavljen) |
| `POST` | `/auth/prijava` | Prijava z e-pošto in geslom (vrne žeton) |
| `POST` | `/auth/odjava` | Odjava (žeton postane neveljaven) |
| `GET` | `/auth/jaz` | Podatki prijavljenega uporabnika |
| `GET` | `/kategorije` | Seznam kategorij prijavljenega uporabnika |
| `GET` | `/kategorije/{id}` | Ena kategorija |
| `POST` | `/kategorije` | Nova kategorija |
| `PUT` | `/kategorije/{id}` | Posodobitev kategorije |
| `DELETE` | `/kategorije/{id}` | Brisanje kategorije |
| `GET` | `/stroski` | Seznam stroškov prijavljenega uporabnika |
| `GET` | `/stroski/iskanje?q=` | Iskanje stroškov po opisu ali kategoriji |
| `GET` | `/stroski/{id}` | En strošek |
| `POST` | `/stroski` | Nov strošek |
| `PUT` | `/stroski/{id}` | Posodobitev stroška |
| `DELETE` | `/stroski/{id}` | Brisanje stroška |
| `GET` | `/mesecni-budzet` | Trenutni mesečni budget |
| `PUT` | `/mesecni-budzet` | Nastavitev ali odstranitev mesečnega budgeta |
| `GET` | `/opozorila` | Neprebrana opozorila (`?vsa=true` tudi prebrana) |
| `POST` | `/opozorila/preveri` | Ponovno preverjanje porabe v tekočem mesecu |
| `PUT` | `/opozorila/{id}/prebrano` | Označi opozorilo kot prebrano |
| `PUT` | `/opozorila/prebrano` | Označi vsa opozorila kot prebrana |
| `GET` | `/pregled?mesec=LLLL-MM` | Pregled porabe po kategorijah za izbrani mesec |

<details>
<summary>Primer zahteve – registracija</summary>

```http
POST /api/v1/auth/registracija
Content-Type: application/json

{
  "ime": "Ana",
  "email": "ana@primer.si",
  "geslo": "geslo1234"
}
```

Odgovor `201 Created`:

```json
{
  "token": "c07Pfwni5FVYeR5eYEZ7…",
  "uporabnik": { "id": 1, "ime": "Ana", "email": "ana@primer.si", "vloga": "UPORABNIK" }
}
```

</details>

<details>
<summary>Primer zahteve – nov strošek</summary>

```http
POST /api/v1/stroski
Authorization: Bearer c07Pfwni5FVYeR5eYEZ7…
Content-Type: application/json

{
  "opis": "Kosilo v menzi",
  "znesek": 4.50,
  "datum": "2026-11-04",
  "kategorija": { "id": 1 }
}
```

</details>

Podroben opis vseh modelov in odgovorov je na voljo v **Swagger UI**.

---

## 👩‍💻 Navodila za razvijalce

### Delo z GitHub repozitorijem

Uporabljamo poenostavljen **feature-branch workflow**:

```
main  ←── Pull Request ←── feature/ponavljajoci-stroski
                       ←── fix/napaka-pri-brisanju
```

1. **Posodobite `main`:**

   ```bash
   git checkout main
   git pull origin main
   ```

2. **Ustvarite novo vejo** za vsako nalogo:

   ```bash
   git checkout -b feature/kratek-opis
   ```

   | Predpona | Uporaba |
   |---|---|
   | `feature/` | Nova funkcionalnost |
   | `fix/` | Popravek napake |
   | `docs/` | Dokumentacija |
   | `refactor/` | Preoblikovanje kode brez spremembe delovanja |

3. **Pogosto commitajte** z jasnimi sporočili (glej spodaj).

4. **Potisnite vejo** in odprite **Pull Request** v `main`:

   ```bash
   git push -u origin feature/kratek-opis
   ```

5. Vsaj **en član ekipe pregleda** PR, preden se ga združi.
6. Po združitvi vejo izbrišete.

> ⚠️ Neposrednega pushanja v `main` ne izvajamo.

### Sporočila commitov

Uporabljamo obliko [Conventional Commits](https://www.conventionalcommits.org/):

```
<tip>: <kratek opis v velelniku>
```

| Tip | Primer |
|---|---|
| `feat` | `feat: dodaj varčevalne cilje` |
| `fix` | `fix: popravi izračun preostanka budgeta` |
| `docs` | `docs: posodobi navodila za zagon` |
| `refactor` | `refactor: loči validacijo v servis` |
| `style` | `style: uredi zamike v StrosekForm` |
| `test` | `test: dodaj teste za KategorijaController` |
| `chore` | `chore: posodobi odvisnosti` |

### Kaj ne sodi v repozitorij

- gesla in drugi skrivni podatki (uporabite `backend/.env` ali okoljske spremenljivke),
- generirane datoteke: `backend/target/`, `frontend/node_modules/`, `frontend/dist/`,
- nastavitve IDE (`.idea/`, `.vscode/`),
- arhivi in osebne datoteke (`*.zip` …).

### Preverjanje pred Pull Requestom

```bash
# backend
cd backend && ./mvnw test

# frontend
cd frontend && npm run lint && npm run build
```

---

## 📏 Standardi kodiranja

### Splošno

- Imena domenskih pojmov so v **slovenščini** (`Strosek`, `Kategorija`, `znesek`, `budzet`), tehnični pojmi pa ostanejo v angleščini (`Controller`, `Repository`, `Form`, `List`).
- Brez šumnikov v imenih razredov, spremenljivk in datotek (`Strosek`, ne `Strošek`).
- Zamik: **4 presledki** v Javi, **2 presledka** v TypeScriptu.
- Koda mora biti brez opozoril prevajalnika in lintra.
- Komentarji naj pojasnjujejo *zakaj*, ne *kaj*.

### Backend (Java / Spring)

| Pravilo | Primer |
|---|---|
| Razredi v `PascalCase` | `StrosekController` |
| Metode in spremenljivke v `camelCase` | `nastaviKategorijo()` |
| Konstante v `UPPER_SNAKE_CASE` | `MAX_ZNESEK` |
| Paketi v malih črkah | `si.um.feri.backend.rest` |
| Entitete v `vao`, repozitoriji v `dao`, kontrolerji v `rest` | – |
| Repozitorij se imenuje `<Entiteta>Repository` | `KategorijaRepository` |
| Kontroler se imenuje `<Entiteta>Controller` | `KategorijaController` |
| Odvisnosti vbrizgamo preko konstruktorja | `private final StrosekRepository dao;` |
| Za getterje/setterje uporabimo Lombok | `@Data`, `@NoArgsConstructor` |
| REST poti so samostalniki v množini | `/stroski`, `/kategorije` |
| Pravilne HTTP statusne kode | `201 Created`, `404 Not Found`, `400 Bad Request` |

### Frontend (React / TypeScript)

| Pravilo | Primer |
|---|---|
| Samo funkcijske komponente in hooki | `function StrosekList() { … }` |
| Ena komponenta na datoteko, ime v `PascalCase` | `StrosekForm.tsx` |
| Funkcije in spremenljivke v `camelCase` | `getStroski`, `createKategorija` |
| Vsi klici na API so v `api.ts` | – |
| Tipi so v `types.ts`, brez uporabe `any` | `Strosek`, `StrosekInput` |
| Uporaba `async/await` namesto `.then()` | – |
| Koda mora prestati `npm run lint` | – |

---

<p align="center">Projekt pri predmetu <b>Razvoj informacijskih sistemov (RIS)</b> · FERI, Univerza v Mariboru</p>