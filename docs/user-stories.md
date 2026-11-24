# 📋 Uporabniške zgodbe

Dokument vsebuje uporabniške zgodbe za trenutno iteracijo projekta. Vsaka zgodba ustreza enemu ali več primerom uporabe (PU) iz [README.md](../README.md) in [`primeri_uporabe.md`](../primeri_uporabe.md). Vsaka zgodba je vnesena tudi kot GitHub Issue z vsaj dvema sub-issues (nalogama).

| ID | Uporabniška zgodba | Primer uporabe | Odgovoren | Prioriteta | Ocena (SP) | GitHub |
|---|---|---|---|---|---|---|
| US-01 | Registracija | PU1 | Staša Despotović | visoka | 5 | _issue_ |
| US-02 | Prijava in odjava | predpogoj za PU2–PU10 | Staša Despotović | visoka | 3 | _issue_ |
| US-03 | Preverjanje budgeta in opozorila | PU6, PU7 (in mesečni budget iz PU2) | Nikola Mojsov | visoka | 8 | _issue_ |
| US-04 | Pregled porabe po kategorijah | PU5 | Lea Bogoevska | visoka | 5 | _issue_ |

<sub>Ocena zahtevnosti je v *story points* (Fibonaccijevo zaporedje 1, 2, 3, 5, 8).</sub>

**Skladnost z INVEST:** zgodbe so med seboj neodvisne in jih lahko razvijamo vzporedno, vsaka prinaša vrednost uporabniku, je dovolj majhna za eno iteracijo in je preverljiva s sprejemnimi kriteriji. Podrobnosti so predmet pogajanj (*Negotiable*) in jih lahko ekipa prilagodi.

**Definition of Ready** – z implementacijo začnemo, ko:

- [x] je uporabniška zgodba jasno definirana (oblika *Kot … želim … da …*),
- [x] so sprejemni kriteriji jasni, merljivi in vključujejo scenarij napake,
- [x] ekipa razume namen in kontekst (povezava s PU),
- [x] so zapisane podrobnosti implementacije.

Po implementaciji vsak član preveri, ali funkcionalnost deluje v skladu s sprejemnimi kriteriji svoje zgodbe.

---

## US-01 – Registracija

| ID | Odgovoren | Prioriteta | Ocena | Primer uporabe |
|---|---|---|---|---|
| US-01 | Staša Despotović ([@stasadespotovic1](https://github.com/stasadespotovic1)) | visoka | 5 SP | [PU1 Registracija](../primeri_uporabe.md#pu1--registracija) |

> **Kot** neregistriran uporabnik **želim** ustvariti uporabniški račun z imenom, e-poštnim naslovom in geslom, **da** lahko spremljam svoje finance in so moji podatki shranjeni ločeno od drugih uporabnikov.

### Sprejemni kriteriji

1. **Uspešna registracija**
   - **Given:** uporabnik ni prijavljen in je odprl obrazec *Registracija*.
   - **When:** vnese ime, veljaven e-poštni naslov, ki še ni v uporabi, geslo z vsaj 8 znaki in enako ponovitev gesla ter klikne *Ustvari račun*.
   - **Then:** sistem ustvari nov račun, uporabnika samodejno prijavi in prikaže pregled stroškov s praznim seznamom stroškov in kategorij, v glavi pa njegovo ime.

2. **E-poštni naslov je že v uporabi** *(napaka)*
   - **Given:** obstaja račun z e-poštnim naslovom `ana@primer.si`.
   - **When:** uporabnik se poskuša registrirati z naslovom `ana@primer.si` ali `ANA@primer.si`.
   - **Then:** sistem izpiše *»Račun s tem e-poštnim naslovom že obstaja.«* in ponudi povezavo *Prijava*; nov račun se ne ustvari.

3. **Neveljaven e-poštni naslov** *(neveljaven vnos)*
   - **Given:** uporabnik je na obrazcu *Registracija*.
   - **When:** vnese e-poštni naslov brez znaka `@` ali brez domene (npr. `ana.primer.si`) in potrdi.
   - **Then:** sistem izpiše *»E-poštni naslov ni veljavne oblike.«*; račun se ne ustvari.

4. **Prekratko geslo ali gesli se ne ujemata** *(neveljaven vnos)*
   - **Given:** uporabnik je na obrazcu *Registracija*.
   - **When:** vnese geslo s 7 ali manj znaki **ali** se ponovitev gesla razlikuje od gesla in potrdi.
   - **Then:** sistem izpiše *»Geslo mora imeti vsaj 8 znakov.«* oziroma *»Gesli se ne ujemata.«*; račun se ne ustvari.

5. **Strežnik ni dosegljiv** *(napaka)*
   - **Given:** strežnik ali baza nista dosegljiva.
   - **When:** uporabnik izpolni veljaven obrazec in potrdi.
   - **Then:** sistem izpiše *»Strežnik trenutno ni dosegljiv. Poskusite znova.«*; ime in e-poštni naslov ostaneta v obrazcu, polji za geslo se izpraznita.

6. **Preklic registracije**
   - **Given:** uporabnik je delno izpolnil obrazec *Registracija*.
   - **When:** klikne *Prekliči*.
   - **Then:** sistem prikaže obrazec *Prijava*; račun se ne ustvari.

### Podrobnosti implementacije

- **Backend**
  - Entiteta `Uporabnik` (`vao/Uporabnik.java`): `id`, `ime`, `email` (unikaten), `geslo`, `vloga` (`UPORABNIK` / `ADMINISTRATOR`, privzeto `UPORABNIK`), `blokiran`.
  - Geslo se shrani **zgoščeno z BCrypt** (`spring-security-crypto`, bean `PasswordEncoder` v `config/WebConfig.java`). Polje `geslo` ima `@JsonIgnore` in se nikoli ne vrne odjemalcu.
  - `POST /api/v1/auth/registracija` (`rest/UporabnikController.java`) sprejme `RegistracijaRequest(ime, email, geslo)`, validiran z `@NotBlank`, `@Email`, `@Size(min = 8)`.
  - E-poštni naslov se pred shranjevanjem normalizira (`trim` + male črke), zato `ANA@primer.si` in `ana@primer.si` veljata za isti naslov.
  - Odgovori: `201 Created` + žeton in podatki o uporabniku (samodejna prijava, PU1 korak 6); `409 Conflict`, če naslov že obstaja; `400 Bad Request` z berljivim sporočilom pri napaki validacije (`rest/NapakaHandler.java`).
  - Uporabnik nikoli ne dobi entitete neposredno, ampak `UporabnikDto(id, ime, email, vloga)`.
- **Frontend**
  - Komponenta `components/RegistracijaForm.tsx`: polja ime, e-pošta, geslo, ponovitev gesla.
  - Preverjanje na odjemalcu (oblika e-pošte, dolžina gesla, ujemanje gesel) pred klicem na strežnik; ponovitev gesla se na strežnik ne pošilja.
  - Pri napaki strežnika se ohranita ime in e-pošta, gesli se pobrišeta (izjema v PU1).
- **Povezava s PU1:** alternativni tokovi 1a, 3a, 4a, 4b, 4c in izjema so pokriti s kriteriji 2–6.

### Naloge (sub-issues)

| Naloga | Opis | Odgovoren |
|---|---|---|
| US-01-T1 Backend: entiteta Uporabnik in končna točka za registracijo | Entiteta `Uporabnik`, `UporabnikRepository`, DTO `RegistracijaRequest` z validacijo, zgoščevanje gesla z BCrypt, `POST /auth/registracija` (201 / 400 / 409). | Staša Despotović |
| US-01-T2 Frontend: obrazec za registracijo | `RegistracijaForm.tsx` s preverjanjem vnosa, prikazom napak, ohranitvijo podatkov ob napaki in samodejno prijavo po uspešni registraciji. | Staša Despotović |

---

## US-02 – Prijava in odjava

| ID | Odgovoren | Prioriteta | Ocena | Primer uporabe |
|---|---|---|---|---|
| US-02 | Staša Despotović ([@stasadespotovic1](https://github.com/stasadespotovic1)) | visoka | 3 SP | predpogoj za PU2–PU10 (prijava ni samostojen PU) |

> **Kot** registriran uporabnik **želim** se prijaviti z e-poštnim naslovom in geslom ter se odjaviti, **da** dostopam samo do svojih stroškov in kategorij, drugi na istem računalniku pa po odjavi do njih ne morejo.

### Sprejemni kriteriji

1. **Uspešna prijava**
   - **Given:** uporabnik ima račun in ni prijavljen.
   - **When:** na obrazcu *Prijava* vnese pravilen e-poštni naslov in geslo ter klikne *Prijava*.
   - **Then:** sistem prikaže pregled njegovih stroškov in kategorij, v glavi pa njegovo ime in gumb *Odjava*. Stroški in kategorije drugih uporabnikov niso prikazani.

2. **Napačen e-poštni naslov ali geslo** *(napaka)*
   - **Given:** uporabnik ni prijavljen.
   - **When:** vnese napačno geslo ali e-poštni naslov, za katerega ne obstaja račun, in klikne *Prijava*.
   - **Then:** sistem v obeh primerih izpiše enako sporočilo *»Napačen e-poštni naslov ali geslo.«*; uporabnik ostane neprijavljen, e-poštni naslov ostane v obrazcu, polje za geslo se izprazni.

3. **Blokiran račun** *(napaka)*
   - **Given:** račun uporabnika je blokiran.
   - **When:** uporabnik vnese pravilen e-poštni naslov in geslo ter klikne *Prijava*.
   - **Then:** sistem izpiše *»Račun je blokiran.«*; uporabnik ostane neprijavljen.

4. **Prijava ostane po osvežitvi strani**
   - **Given:** uporabnik je prijavljen.
   - **When:** osveži stran ali zapre in ponovno odpre zavihek v roku 7 dni.
   - **Then:** sistem ne zahteva ponovne prijave in prikaže njegov pregled.

5. **Odjava**
   - **Given:** uporabnik je prijavljen.
   - **When:** klikne *Odjava* in nato osveži stran.
   - **Then:** sistem prikaže obrazec *Prijava*; stroški in kategorije prejšnjega uporabnika niso prikazani.

6. **Dostop brez prijave ali po potekli seji** *(napaka)*
   - **Given:** obiskovalec ni prijavljen **ali** je od prijave minilo več kot 7 dni.
   - **When:** odpre aplikacijo ali izvede katerokoli dejanje.
   - **Then:** sistem prikaže obrazec *Prijava* in ne prikaže nobenih stroškov ali kategorij.

### Podrobnosti implementacije

- **Pristop:** preprosta avtentikacija z žetonom, brez Spring Security. Gesla se preverjajo z BCrypt.
- **Backend**
  - Entiteta `Seja` (`vao/Seja.java`): `token` (32 naključnih bajtov iz `SecureRandom`, Base64), `uporabnik`, `poteka` (7 dni). Ločena tabela omogoča prijavo na več napravah in odjavo z brisanjem seje.
  - `POST /auth/prijava` – `200` + žeton; `401` z enakim sporočilom za napačen naslov in geslo (da ne razkrijemo, kateri računi obstajajo); `403` za blokiran račun.
  - `POST /auth/odjava` – izbriše trenutno sejo (`204`).
  - `GET /auth/jaz` – vrne podatke prijavljenega uporabnika; frontend ga kliče ob osvežitvi strani.
  - `rest/AuthInterceptor.java` (`HandlerInterceptor`) preveri glavo `Authorization: Bearer <token>` za vse poti razen `/auth/registracija`, `/auth/prijava`, `/info`, `/error` in Swagger. Neveljavna ali potekla seja ali blokiran račun → `401`. Prijavljenega uporabnika shrani v atribut zahteve `uporabnik`.
  - **Ločeni podatki:** `Strosek` in `Kategorija` imata obvezno povezavo na `Uporabnik`. Kontrolerja prejmeta prijavljenega uporabnika z `@RequestAttribute("uporabnik")` in uporabljata poizvedbe `findByUporabnikId` / `findByIdAndUporabnikId`; tuj zapis vrne `404`, tuja kategorija pri strošku `400`.
  - CORS je nastavljen enkrat v `config/WebConfig.java` (namesto `@CrossOrigin` na vsakem kontrolerju).
  - Geslo baze ni več v repozitoriju: bere se iz `backend/.env` (ni v gitu, predloga `backend/.env.example`).
- **Frontend**
  - `components/PrijavaForm.tsx` – obrazec in prikaz napake.
  - `api.ts` – žeton se hrani v `localStorage`; skupna funkcija `request` doda glavo `Authorization`, pri odgovoru `401` žeton pobriše in aplikacijo vrne na prijavo.
  - `App.tsx` – ob zagonu s shranjenim žetonom pokliče `getJaz()`; neprijavljenemu prikaže prijavo/registracijo, prijavljenemu `components/Pregled.tsx`.
- **Opombe**
  - Blokiranje računov bo omogočil PU10 (administrator). Do takrat se kriterij 3 preveri z ročno nastavitvijo `blokiran = true` v bazi.
  - Obstoječih stroškov in kategorij brez lastnika ni mogoče samodejno prenesti, zato je treba lokalno bazo `stroski` enkrat ponovno ustvariti.

### Naloge (sub-issues)

| Naloga | Opis | Odgovoren |
|---|---|---|
| US-02-T1 Backend: seje, prijava, odjava in zaščita podatkov | Entiteta `Seja`, končne točke `/auth/prijava`, `/auth/odjava`, `/auth/jaz`, `AuthInterceptor`, CORS v `WebConfig` ter omejitev stroškov in kategorij na prijavljenega uporabnika. | Staša Despotović |
| US-02-T2 Frontend: obrazec za prijavo in odjava | `PrijavaForm.tsx`, shranjevanje žetona, ohranitev prijave po osvežitvi, gumb *Odjava* v glavi in vrnitev na prijavo ob potekli seji. | Staša Despotović |

---

## US-03 – Preverjanje budgeta in opozorila

| ID | Odgovoren | Prioriteta | Ocena | Primer uporabe |
|---|---|---|---|---|
| US-03 | Nikola Mojsov ([@mojsov](https://github.com/mojsov)) | visoka | 8 SP | [PU6 Preverjanje budgeta](../primeri_uporabe.md#pu6--preverjanje-budgeta), [PU7 Prikaz opozorila](../primeri_uporabe.md#pu7--prikaz-opozorila) (z nastavitvijo mesečnega budgeta iz PU2) |

> **Kot** registriran uporabnik **želim**, da sistem ob vsakem novem strošku in ob vsaki spremembi budgeta preveri mojo porabo v kategoriji in v mesecu ter me opozori, ko poraba doseže 80 % ali 100 % budgeta, in opozorilo obdrži, dokler ga ne preberem, **da** lahko pravočasno zmanjšam porabo in mi denarja ne zmanjka pred koncem meseca.

**Pravila:**
- opozorilo *približevanje* velja pri porabi od 80 % do manj kot 100 % budgeta, opozorilo *preseganje* pa pri porabi 100 % budgeta ali več,
- preverja se budget vsake kategorije in splošni mesečni budget (skupna poraba v mesecu, tudi stroški brez kategorije),
- poraba se šteje za mesec, v katerega spada datum stroška, in samo za stroške prijavljenega uporabnika,
- za isti cilj (kategorijo ali mesečni budget), mesec in stopnjo je naenkrat največ eno opozorilo,
- opozorilo ostane neprebrano, dokler ga uporabnik ne zapre,
- **izven obsega:** opozorila po e-pošti ali push obvestila, opozorila ob samodejnih ponavljajočih se stroških (PU8), mesečni budget, ki bi bil različen za vsak mesec (velja en mesečni budget za vse mesece).

### Sprejemni kriteriji

1. **Opozorilo ob 80 % budgeta kategorije**
   - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 70,00 €.
   - **When:** uporabnik doda strošek 10,00 € v kategorijo *Hrana* z datumom v novembru 2026.
   - **Then:** sistem prikaže neprebrano opozorilo *»Poraba v kategoriji Hrana je dosegla 80 % budgeta (80,00 € od 100,00 €).«*

2. **Opozorilo ob preseganju budgeta kategorije**
   - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 90,00 €.
   - **When:** uporabnik doda strošek 15,00 € v kategorijo *Hrana* z datumom v novembru 2026.
   - **Then:** sistem prikaže opozorilo *»Budget kategorije Hrana je presežen za 5,00 € (105,00 € od 100,00 €).«*; strošek je kljub temu shranjen.

3. **Poraba je natanko enaka budgetu**
   - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 90,00 €.
   - **When:** uporabnik doda strošek 10,00 € v kategorijo *Hrana* z datumom v novembru 2026.
   - **Then:** sistem prikaže opozorilo *»Budget kategorije Hrana je porabljen v celoti (100,00 € od 100,00 €).«*

4. **Brez opozorila pod pragom**
   - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 50,00 €.
   - **When:** uporabnik doda strošek 10,00 € v kategorijo *Hrana* z datumom v novembru 2026.
   - **Then:** sistem ne prikaže nobenega opozorila (poraba je 60 %).

5. **Kategorija brez budgeta**
   - **Given:** kategorija *Zabava* nima nastavljenega budgeta.
   - **When:** uporabnik doda strošek 500,00 € v kategorijo *Zabava*.
   - **Then:** sistem za kategorijo *Zabava* ne prikaže nobenega opozorila.

6. **Opozorilo ob 80 % mesečnega budgeta**
   - **Given:** mesečni budget uporabnika je 500,00 €, v novembru 2026 pa ima stroške v več kategorijah in en strošek brez kategorije v skupni višini 380,00 €.
   - **When:** uporabnik doda strošek 20,00 € z datumom v novembru 2026.
   - **Then:** sistem prikaže opozorilo *»Poraba v mesecu je dosegla 80 % mesečnega budgeta (400,00 € od 500,00 €).«*

7. **Opozorilo ob preseganju mesečnega budgeta**
   - **Given:** mesečni budget uporabnika je 500,00 €, poraba v novembru 2026 pa 490,00 €.
   - **When:** uporabnik doda strošek 30,00 € z datumom v novembru 2026.
   - **Then:** sistem prikaže opozorilo *»Mesečni budget je presežen za 20,00 € (520,00 € od 500,00 €).«*

8. **Opozorilo se ne podvaja**
   - **Given:** za kategorijo *Hrana* (budget 100,00 €) je prikazano opozorilo o 80 % budgeta.
   - **When:** uporabnik doda še strošek 5,00 €, tako da je poraba 85 %.
   - **Then:** sistem še vedno prikazuje natanko eno opozorilo za kategorijo *Hrana*, ki kaže porabo 85,00 € od 100,00 €.

9. **Preseganje nadomesti opozorilo o približevanju**
   - **Given:** za kategorijo *Hrana* (budget 100,00 €) je prikazano neprebrano opozorilo o 90 % budgeta.
   - **When:** uporabnik doda strošek 15,00 €, tako da je poraba 105 %.
   - **Then:** sistem prikazuje samo opozorilo o preseganju budgeta; opozorilo o približevanju ni več med neprebranimi.

10. **Opozorilo ostane do branja**
    - **Given:** za kategorijo *Hrana* je prikazano neprebrano opozorilo.
    - **When:** uporabnik se odjavi, se znova prijavi in odpre stran s stroški.
    - **Then:** sistem opozorilo še vedno prikazuje na vrhu strani.

11. **Zapiranje opozorila**
    - **Given:** prikazani sta dve neprebrani opozorili.
    - **When:** uporabnik pri prvem klikne *zapri* in osveži stran.
    - **Then:** prvo opozorilo ni več prikazano, drugo pa je še vedno; ob izbiri *označi vse kot prebrano* sistem ne prikaže nobenega opozorila več.

12. **Prikaz stroškov iz opozorila**
    - **Given:** prikazano je opozorilo za kategorijo *Hrana*.
    - **When:** uporabnik klikne *prikaži stroške*.
    - **Then:** sistem v polje za iskanje vpiše *»Hrana«* in seznam prikaže samo stroške, ki ustrezajo temu iskanju (med njimi stroške kategorije *Hrana*).

13. **Opozorilo izgine po povišanju budgeta**
    - **Given:** kategorija *Hrana* ima budget 100,00 €, porabo 90,00 € in prikazano opozorilo o približevanju.
    - **When:** uporabnik budget kategorije poviša na 200,00 € in shrani.
    - **Then:** sistem opozorila za kategorijo *Hrana* ne prikazuje več (poraba je 45 %).

14. **Opozorilo se pojavi po znižanju budgeta**
    - **Given:** kategorija *Hrana* ima budget 200,00 € in porabo 90,00 € brez opozorila.
    - **When:** uporabnik budget kategorije zniža na 100,00 € in shrani.
    - **Then:** sistem prikaže opozorilo o približevanju budgetu (poraba je 90 %).

15. **Opozorilo izgine po brisanju stroška**
    - **Given:** kategorija *Hrana* ima budget 100,00 €, porabo 105,00 € in prikazano opozorilo o preseganju.
    - **When:** uporabnik izbriše strošek v višini 50,00 €.
    - **Then:** sistem opozorila o preseganju ne prikazuje več (poraba je 55 %).

16. **Upoštevanje meseca stroška**
    - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 70,00 €.
    - **When:** uporabnik doda strošek 50,00 € v kategorijo *Hrana* z datumom 20. 10. 2026.
    - **Then:** sistem ne prikaže opozorila za november 2026.

17. **Neveljaven znesek stroška** *(neveljaven vnos)*
    - **Given:** kategorija *Hrana* ima budget 100,00 € in v novembru 2026 porabo 70,00 €.
    - **When:** uporabnik poskusi shraniti strošek z zneskom 0 ali negativnim zneskom.
    - **Then:** strošek ni shranjen, sistem izpiše *»Znesek mora biti večji od 0.«*; opozorila se ne spremenijo.

18. **Neveljaven mesečni budget** *(neveljaven vnos)*
    - **Given:** uporabnik je na obrazcu za mesečni budget.
    - **When:** vnese 0 ali negativen znesek in shrani.
    - **Then:** mesečni budget ni spremenjen, sistem izpiše *»Mesečni budget mora biti večji od 0.«*

19. **Upoštevani so samo stroški prijavljenega uporabnika**
    - **Given:** drug uporabnik ima v kategoriji z istim imenom *Hrana* stroške v višini 500,00 €, prijavljeni uporabnik pa ima budget 100,00 € in porabo 10,00 €.
    - **When:** prijavljeni uporabnik doda strošek 5,00 € v svojo kategorijo *Hrana*.
    - **Then:** sistem ne prikaže nobenega opozorila.

### Podrobnosti implementacije

- **Podatkovni model**
  - Nova entiteta `vao/Opozorilo` (shranjeno opozorilo): `id`, `uporabnik`, `kategorija` (`null` pri mesečnem budgetu), `leto`, `mesec`, `stopnja` (enum `StopnjaOpozorila`: `PRIBLIZEVANJE`, `PRESEGANJE`), `poraba`, `budzet`, `delez`, `prebrano`, `ustvarjeno`. Ob brisanju uporabnika ali kategorije se opozorila izbrišejo (`@OnDelete(CASCADE)`).
  - `Uporabnik` dobi polje `mesecniBudzet` (`Double`, `null` = ni nastavljen).
  - Nov repozitorij `dao/OpozoriloRepository`; `StrosekRepository` dobi poizvedbi `porabaPoKategorijah` (vsota po kategoriji za obdobje) in `skupnaPoraba` (vsota vseh stroškov v obdobju).
- **Preverjanje budgeta (PU6)** – `service/PreverjanjeBudgetaService`
  - `preveri(uporabnik, datum)` izračuna porabo za mesec datuma in za vsak cilj (vsako kategorijo in mesečni budget) uskladi shranjena opozorila:
    - najvišja dosežena stopnja brez obstoječega opozorila: ustvari novo (neprebrano),
    - opozorilo, ki ga poraba ne dosega več (povišan budget, izbrisan strošek, odstranjen budget): izbriše,
    - opozorilo iste stopnje že obstaja: samo osveži zneske (brez podvajanja),
    - ob preseganju se opozorilo o približevanju označi kot prebrano.
  - Pragova sta konstanti (`PRAG_PRIBLIZEVANJE = 0.8`, `PRAG_PRESEGANJE = 1.0`); pri primerjavi se upošteva majhna toleranca zaradi decimalnega seštevanja.
  - Klic se izvede po uspešnem shranjevanju: `POST`/`PUT`/`DELETE` v `StrosekController` (mesec stroška, pri prestavitvi v drug mesec tudi prejšnji mesec), `POST`/`PUT` v `KategorijaController` (tekoči mesec) in `PUT /mesecni-budzet`. Napaka pri preverjanju se zabeleži, strošek pa ostane shranjen (izjema PU6).
  - `StrosekController` zavrne strošek z zneskom ≤ 0 ali brez datuma (`400 Bad Request`).
- **REST API** (vse zahteve zahtevajo prijavo, podatki so omejeni na prijavljenega uporabnika)

  | Metoda | Pot | Opis | Odgovori |
  |---|---|---|---|
  | `GET` | `/opozorila` | Neprebrana opozorila (najnovejša prva); z `?vsa=true` tudi prebrana | `200` |
  | `POST` | `/opozorila/preveri` | Ponovno preveri porabo v tekočem mesecu (ob odprtju aplikacije) | `204` |
  | `PUT` | `/opozorila/{id}/prebrano` | Označi opozorilo kot prebrano | `204`, `404` |
  | `PUT` | `/opozorila/prebrano` | Označi vsa opozorila kot prebrana | `204` |
  | `GET` | `/mesecni-budzet` | Trenutni mesečni budget (`{"budzet": 500.0}` ali `null`) | `200` |
  | `PUT` | `/mesecni-budzet` | Nastavi ali (z `null`) odstrani mesečni budget | `200`, `400` |

- **Frontend**
  - `types.ts`, `api.ts`: tipa `Opozorilo` in `MesecniBudzet` ter funkcije `getOpozorila`, `preveriOpozorila`, `oznaciPrebrano`, `oznaciVsePrebrano`, `getMesecniBudzet`, `setMesecniBudzet`.
  - `components/OpozorilaBanner.tsx`: seznam neprebranih opozoril (rumeno za približevanje, rdeče za preseganje) z gumbi *zapri*, *označi vse kot prebrano* in *prikaži stroške* (nastavi iskanje po imenu kategorije).
  - `components/MesecniBudzetForm.tsx`: nastavitev mesečnega budgeta in prikaz porabe v tekočem mesecu.
  - `Pregled.tsx`: ob odprtju pokliče `POST /opozorila/preveri` in naloži opozorila, po vsaki spremembi stroška, kategorije ali mesečnega budgeta pa jih znova naloži.
- **Opombe**
  - Opozorila so shranjena (za razliko od izračuna ob vsakem klicu), zato ostanejo, dokler jih uporabnik ne prebere (PU7, tok 4a). Zapis zneskov v opozorilu se osvežuje ob vsakem preverjanju.
  - Mesečni budget je ena vrednost za vse mesece; podrobnejše upravljanje (zgodovina po mesecih) je del PU2 in ni del te zgodbe.

### Naloge (sub-issues)

| Naloga | Opis | Odgovoren |
|---|---|---|
| US-03-T1 Backend: entiteta Opozorilo, repozitorij in poizvedbe porabe | `Opozorilo`, `StopnjaOpozorila`, `OpozoriloRepository`, poizvedbi `porabaPoKategorijah` in `skupnaPoraba` v `StrosekRepository`. | Nikola Mojsov |
| US-03-T2 Backend: PreverjanjeBudgetaService in klici po spremembah | Usklajevanje opozoril (ustvari, osveži, izbriši, brez podvajanja), klici iz `StrosekController` in `KategorijaController`, validacija zneska in datuma stroška. | Nikola Mojsov |
| US-03-T3 Backend: REST API za opozorila in mesečni budget | `OpozorilaController`, `MesecniBudzetController`, polje `mesecniBudzet` pri `Uporabnik`, validacija. | Nikola Mojsov |
| US-03-T4 Frontend: prikaz in upravljanje opozoril | `OpozorilaBanner` (zapri, označi vse, prikaži stroške), klici API in osveževanje v `Pregled.tsx`. | Nikola Mojsov |
| US-03-T5 Frontend: nastavitev mesečnega budgeta in testi | `MesecniBudzetForm`, prikaz porabe v mesecu, enotni testi `PreverjanjeBudgetaServiceTest`, preverjanje kriterijev. | Nikola Mojsov |

### Preverjanje po implementaciji

| Kriterij | Kako preveriti |
|---|---|
| 1–5 | Ustvari kategorijo *Hrana* z budgetom 100 € in dodajaj stroške po scenarijih; enotni testi preverjajo meji 80 % in 100 %. |
| 6, 7 | Nastavi mesečni budget 500 €, dodaj stroške v več kategorij in enega brez kategorije. |
| 8, 9 | Dodajaj stroške znotraj istega praga in nato čez prag; v bazi (`opozorilo`) mora biti največ eno opozorilo na stopnjo in cilj. |
| 10, 11 | Osveži stran / se odjavi in prijavi; klikni *zapri* in *označi vse kot prebrano*. |
| 12 | Klikni *prikaži stroške* pri opozorilu. |
| 13–15 | Spremeni budget kategorije in izbriši strošek; opozorila morajo ustrezno izginiti ali se pojaviti. |
| 16 | Dodaj strošek z datumom v prejšnjem mesecu. |
| 17, 18 | Znesek 0 ali −5 pri stroških in mesečnem budgetu; pričakuj `400` in sporočilo. |
| 19 | Registriraj dva računa z enako imenovano kategorijo; stroški drugega računa ne smejo vplivati na opozorila. |

---

## US-04 – Pregled porabe po kategorijah

| ID | Odgovoren | Prioriteta | Ocena | Primer uporabe |
|---|---|---|---|---|
| US-04 | Lea Bogoevska ([@leabogoevska26](https://github.com/leabogoevska26)) | visoka | 5 SP | [PU5 Pregled porabe](../primeri_uporabe.md#pu5--pregled-porabe) |

> **Kot** registriran uporabnik **želim** za izbrani mesec videti, koliko sem porabil skupaj in v posamezni kategoriji, kolikšen delež porabe predstavlja vsaka kategorija ter kako se poraba primerja s prejšnjim mesecem, **da** vem, za kaj gre moj denar, in lahko pravočasno zmanjšam porabo tam, kjer je največja.

**Pravila:**
- privzeto je izbran tekoči mesec; uporabnik lahko izbere katerikoli drug mesec,
- upoštevajo se samo stroški prijavljenega uporabnika z datumom v izbranem mesecu,
- v pregledu so vse kategorije uporabnika (tudi tiste brez stroškov v mesecu), urejene od največje do najmanjše porabe; stroški brez kategorije so prikazani v vrstici *Brez kategorije*,
- delež kategorije je njena poraba glede na skupno porabo v mesecu, zaokrožen na eno decimalko,
- kategorija je označena kot presežena, ko je poraba **večja** od njenega budgeta,
- **izven obsega:** prihodki, mesečni budget in preostanek v pregledu (odvisni od PU4 in PU2), grafični prikaz, klik na kategorijo za seznam njenih stroškov (PU5, tok 5a).

### Sprejemni kriteriji

1. **Pregled za tekoči mesec**
   - **Given:** uporabnik ima v novembru 2026 stroške 60,00 € v kategoriji *Hrana* in 40,00 € v kategoriji *Prevoz*.
   - **When:** odpre aplikacijo v novembru 2026.
   - **Then:** sistem v pregledu porabe prikaže *Skupaj: 100,00 €*, vrstico *Hrana* s porabo 60,00 € in deležem 60 % ter vrstico *Prevoz* s porabo 40,00 € in deležem 40 %; *Hrana* je prikazana pred *Prevozom*.

2. **Izbira drugega meseca**
   - **Given:** uporabnik ima v oktobru 2026 en strošek 25,00 € v kategoriji *Hrana*, v novembru 2026 pa druge stroške.
   - **When:** v polju *Mesec* izbere oktober 2026.
   - **Then:** sistem prikaže pregled samo za oktober 2026: *Skupaj: 25,00 €* in *Hrana* 25,00 € (100 %).

3. **Primerjava s prejšnjim mesecem**
   - **Given:** uporabnik je v oktobru 2026 porabil skupaj 80,00 €, v novembru 2026 pa 100,00 €; v kategoriji *Hrana* je oktobra porabil 50,00 €.
   - **When:** odpre pregled za november 2026.
   - **Then:** sistem ob skupnem znesku izpiše *»za 20,00 € več kot prejšnji mesec«*, v vrstici *Hrana* pa v stolpcu *Prejšnji mesec* prikaže 50,00 €.

4. **Presežen budget kategorije**
   - **Given:** kategorija *Hrana* ima budget 50,00 €, uporabnik pa je v novembru 2026 v njej porabil 60,00 €.
   - **When:** odpre pregled za november 2026.
   - **Then:** sistem porabo kategorije *Hrana* prikaže rdeče, v stolpcu *Budžet* pa *50,00 € (preseženo)*.

5. **Stroški brez kategorije in kategorije brez budgeta**
   - **Given:** uporabnik ima v novembru 2026 strošek 15,00 € brez kategorije in kategorijo *Zabava* brez budgeta.
   - **When:** odpre pregled za november 2026.
   - **Then:** sistem prikaže vrstico *Brez kategorije* s porabo 15,00 €, pri kategoriji *Zabava* pa v stolpcu *Budžet* prikaže »—«.

6. **Mesec brez stroškov**
   - **Given:** uporabnik ima kategorijo *Hrana*, v septembru 2026 pa nima nobenega stroška.
   - **When:** izbere september 2026.
   - **Then:** sistem prikaže *Skupaj: 0,00 €* in kategorijo *Hrana* s porabo 0,00 € in deležem 0 %.

7. **Uporabnik brez kategorij in stroškov**
   - **Given:** uporabnik se je pravkar registriral in nima nobene kategorije ali stroška.
   - **When:** odpre pregled porabe.
   - **Then:** sistem izpiše *»Ni stroškov za izbrani mesec.«*

8. **Pregled se osveži po spremembi stroška**
   - **Given:** uporabnik ima odprt pregled za november 2026 s porabo *Hrana* 60,00 €.
   - **When:** doda nov strošek 10,00 € v kategorijo *Hrana* z datumom v novembru 2026.
   - **Then:** sistem brez osvežitve strani prikaže porabo *Hrana* 70,00 €.

9. **Upoštevani so samo stroški prijavljenega uporabnika**
   - **Given:** drug uporabnik ima v novembru 2026 stroške v višini 500,00 €, prijavljeni uporabnik pa 100,00 €.
   - **When:** prijavljeni uporabnik odpre pregled za november 2026.
   - **Then:** sistem prikaže *Skupaj: 100,00 €*.

10. **Mesec ni izbran** *(neveljaven vnos)*
    - **Given:** uporabnik ima odprt pregled porabe.
    - **When:** izprazni polje *Mesec*.
    - **Then:** sistem izpiše *»Izberite mesec za prikaz pregleda.«* in ne prikaže tabele.

11. **Strežnik ni dosegljiv** *(napaka)*
    - **Given:** strežnik ali baza nista dosegljiva.
    - **When:** uporabnik odpre pregled porabe ali izbere drug mesec.
    - **Then:** sistem izpiše *»Strežnik trenutno ni dosegljiv. Poskusite znova.«*; preostali deli aplikacije ostanejo prikazani.

### Podrobnosti implementacije

- **Backend**
  - `rest/PregledController.java` – `GET /api/v1/pregled?mesec=LLLL-MM` (prijava obvezna; brez parametra se uporabi tekoči mesec).
    - Mesec se prebere z `YearMonth.parse`; neveljaven zapis vrne `400` s sporočilom *»Mesec mora biti v obliki LLLL-MM, npr. 2026-11.«*
    - Stroški izbranega in prejšnjega meseca se naložijo z novo poizvedbo `StrosekRepository.findByUporabnikIdAndDatumBetween(uid, prviDan, zadnjiDan)`, zato so upoštevani samo stroški prijavljenega uporabnika.
    - Za vsako kategorijo uporabnika (`KategorijaRepository.findByUporabnikId`) se izračunajo poraba v mesecu, poraba v prejšnjem mesecu, delež skupne porabe in zastavica `preseceno` (`budzet != null && poraba > budzet`). Vrstica *Brez kategorije* se doda, če obstajajo stroški brez kategorije v izbranem ali prejšnjem mesecu.
    - Zneski se zaokrožijo na 2 decimalki, deleži na 1 decimalko (v odstotkih); postavke so urejene padajoče po porabi.
  - DTO-ja: `dto/PregledDto(mesec, skupaj, skupajPrejsnji, postavke)` in `dto/PostavkaPregleda(kategorijaId, naziv, budzet, porabljeno, porabljenoPrejsnji, delez, preseceno)`; `kategorijaId` je `null` za vrstico *Brez kategorije*.
- **Frontend**
  - `types.ts`: tip `PregledPorabeOdgovor` (in postavka pregleda); `api.ts`: funkcija `getPregledPorabe(mesec)`.
  - `components/PregledPorabe.tsx`: izbira meseca (`<input type="month">`, privzeto tekoči mesec), povzetek *Skupaj* z razliko do prejšnjega meseca in tabela s stolpci *Kategorija*, *Porabljeno*, *Delež*, *Budžet*, *Prejšnji mesec*. Presežena poraba je označena z razredom `over` (rdeča). Komponenta znova naloži podatke ob spremembi meseca ali seznama stroškov (prop `stroski`).
  - `components/Pregled.tsx`: vključuje `<PregledPorabe stroski={stroski} />`.
- **Povezava s PU5:** pokriti so koraki 1–5 (izbira meseca, izračun in prikaz porabe po kategorijah) ter tok 2a (mesec brez stroškov); prihodki, mesečni budget s preostankom (koraki 3–4 v celoti), tok 2b (opozorila – US-03) in tok 5a ostajajo za nadaljnje iteracije.

### Naloge (sub-issues)

| Naloga | Opis | Odgovoren |
|---|---|---|
| US-04-T1 Backend: končna točka za pregled porabe | `PregledController` (`GET /pregled?mesec=`), DTO-ja `PregledDto` in `PostavkaPregleda`, poizvedba `findByUporabnikIdAndDatumBetween`, izračun porabe, deleža, primerjave s prejšnjim mesecem in preseženega budgeta, validacija meseca. | Lea Bogoevska |
| US-04-T2 Frontend: komponenta za pregled porabe | `PregledPorabe.tsx` z izbiro meseca, povzetkom in tabelo, tip in klic API v `types.ts` / `api.ts`, vključitev v `Pregled.tsx` in osveževanje ob spremembi stroškov. | Lea Bogoevska |
| US-04-T3 Preverjanje sprejemnih kriterijev | Ročni preizkus kriterijev 1–11 z dvema računoma in stroški v dveh zaporednih mesecih (aplikacija in Swagger UI). | Lea Bogoevska |

### Preverjanje po implementaciji

| Kriterij | Kako preveriti |
|---|---|
| 1, 2, 6 | Ustvari kategoriji *Hrana* in *Prevoz*, dodaj stroške v novembru in oktobru ter preklapljaj med meseci. |
| 3 | Primerjaj skupni znesek in stolpec *Prejšnji mesec* z ročno izračunano vsoto prejšnjega meseca. |
| 4, 5 | Nastavi budget 50 € in porabi 60 €; dodaj strošek brez kategorije in kategorijo brez budgeta. |
| 7, 9 | Registriraj nov račun (prazen pregled) in drug račun s stroški; pregled prvega računa se ne sme spremeniti. |
| 8 | Dodaj strošek in preveri, da se pregled posodobi brez osvežitve strani. |
| 10 | Izprazni polje *Mesec*. |
| 11 | Ustavi backend in izberi drug mesec. |
