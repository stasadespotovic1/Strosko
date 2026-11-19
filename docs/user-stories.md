# 📖 Uporabniške zgodbe – $troško

Dokument vsebuje uporabniške zgodbe za trenutno iteracijo projekta. Vsaka zgodba ustreza enemu ali več primerom uporabe (PU) iz [README.md](../README.md) in [`primeri_uporabe.md`](../primeri_uporabe.md). Vsaka zgodba je vnesena tudi kot GitHub Issue z dvema ali več sub-issues (nalogami).

## Pregled zgodb

| ID | Zgodba | Primer uporabe | Odgovorni član | Prioriteta | Ocena zahtevnosti |
|---|---|---|---|---|---|
| US-1 | Registracija in prijava | PU1 | Staša Despotović ([@stasadespotovic1](https://github.com/stasadespotovic1)) | Visoka (Must) | 8 točk |
| US-2 | Preverjanje budgeta in opozorila | PU6, PU7 | Nikola Mojsov ([@mojsov](https://github.com/mojsov)) | Visoka (Must) | 8 točk |
| US-3 | _(zgodba Lee Bogoevske – dopolni)_ | | Lea Bogoevska ([@leabogoevska26](https://github.com/leabogoevska26)) | | |

<sub>Ocena zahtevnosti je v *story points* (Fibonaccijevo zaporedje: 1, 2, 3, 5, 8). Prioriteta po metodi MoSCoW.</sub>

**Skladnost z INVEST:** zgodbe so med seboj neodvisne in jih lahko razvijamo vzporedno, vsaka prinaša vrednost uporabniku, je dovolj majhna za eno iteracijo in je preverljiva s sprejemnimi kriteriji. Podrobnosti so predmet pogajanj (*Negotiable*) in jih lahko ekipa prilagodi.

---

## US-1 – Registracija in prijava

| Polje | Vrednost |
|---|---|
| **ID** | US-1 |
| **Odgovorni član** | Staša Despotović (@stasadespotovic1) |
| **Primer uporabe** | PU1 – Registracija (prijava je predpogoj za ostale PU) |
| **Prioriteta** | Visoka (Must) |
| **Ocena zahtevnosti** | 8 točk |

### Uporabniška zgodba

**Kot** neregistriran uporabnik **želim** ustvariti uporabniški račun in se prijaviti z e-pošto in geslom, **da** vidim in urejam samo svoje stroške in kategorije.

### Sprejemni kriteriji

**AC-1.1 – Uspešna registracija**
- **Given:** nimam računa in sem na obrazcu za registracijo
- **When:** vnesem ime, veljaven e-poštni naslov, geslo z vsaj 8 znaki in potrdim
- **Then:** je račun ustvarjen, sem prijavljen in vidim svojo (prazno) stran s stroški

**AC-1.2 – Že obstoječ e-poštni naslov**
- **Given:** račun z naslovom `ana@primer.si` že obstaja
- **When:** se poskusim registrirati z istim naslovom
- **Then:** račun ni ustvarjen in prikaže se sporočilo »Račun s tem e-poštnim naslovom že obstaja.«

**AC-1.3 – Neveljaven vnos pri registraciji**
- **Given:** sem na obrazcu za registracijo
- **When:** vnesem geslo s 5 znaki ali e-poštni naslov brez znaka @
- **Then:** račun ni ustvarjen in prikaže se sporočilo o prvi napaki (»Geslo mora imeti vsaj 8 znakov.« oziroma »E-poštni naslov ni veljavne oblike.«)

**AC-1.4 – Uspešna prijava**
- **Given:** imam račun
- **When:** vnesem pravilen e-poštni naslov in geslo
- **Then:** sem prijavljen in vidim samo svoje stroške in kategorije

**AC-1.5 – Napačno geslo**
- **Given:** imam račun
- **When:** vnesem pravilen e-poštni naslov in napačno geslo
- **Then:** nisem prijavljen in prikaže se sporočilo »Napačen e-poštni naslov ali geslo.«

**AC-1.6 – Dostop brez prijave in odjava**
- **Given:** nisem prijavljen (ali sem se odjavil)
- **When:** poskusim dostopati do stroškov ali kategorij
- **Then:** podatkov ne vidim in sem preusmerjen na prijavo

### Podrobnosti implementacije

Zgodba je že implementirana (`UporabnikController`, `AuthInterceptor`, `Seja`, `PrijavaForm`, `RegistracijaForm`): gesla so zgoščena z BCrypt, prijava ustvari sejo z žetonom (veljavnost 7 dni, glava `Authorization: Bearer`), vsi viri (`Strosek`, `Kategorija`) so vezani na uporabnika. _Staša naj ta razdelek preveri in po potrebi dopolni._

### Naloge (sub-issues)

| # | Naziv | Opis | Odgovorni |
|---|---|---|---|
| T-1.1 | Backend: registracija, prijava, seje | Entiteti `Uporabnik` in `Seja`, `UporabnikController`, `AuthInterceptor`, validacija vnosa, ločitev podatkov po uporabnikih. | Staša Despotović |
| T-1.2 | Frontend: obrazca za registracijo in prijavo | `RegistracijaForm`, `PrijavaForm`, shranjevanje žetona, odjava, preusmeritev ob potekli seji. | Staša Despotović |

---

## US-2 – Preverjanje budgeta in opozorila

| Polje | Vrednost |
|---|---|
| **ID** | US-2 |
| **Odgovorni član** | Nikola Mojsov (@mojsov) |
| **Primer uporabe** | PU6 – Preverjanje budgeta, PU7 – Prikaz opozorila (z nastavitvijo mesečnega budgeta iz PU2) |
| **Prioriteta** | Visoka (Must) |
| **Ocena zahtevnosti** | 8 točk |

### Uporabniška zgodba

**Kot** registriran uporabnik **želim**, da sistem ob vsakem novem strošku in ob vsaki spremembi budgeta preveri mojo porabo v kategoriji in v mesecu ter me opozori in obdrži opozorilo, dokler ga ne preberem, ko poraba doseže 80 % ali 100 % budgeta, **da** lahko pravočasno zmanjšam porabo in mi denarja ne zmanjka pred koncem meseca.

**Pravila:**
- opozorilo *približevanje* velja pri porabi od 80 % do manj kot 100 % budgeta, opozorilo *preseganje* pa pri porabi 100 % budgeta ali več,
- preverja se budget vsake kategorije in splošni mesečni budget (skupna poraba v mesecu, tudi stroški brez kategorije),
- poraba se šteje za mesec, v katerega spada datum stroška, in samo za stroške prijavljenega uporabnika,
- za isti cilj (kategorijo ali mesečni budget), mesec in stopnjo je naenkrat največ eno opozorilo,
- opozorilo ostane neprebrano, dokler ga uporabnik ne zapre,
- **izven obsega:** opozorila po e-pošti ali push obvestila, opozorila ob samodejnih ponavljajočih se stroških (PU8), mesečni budget, ki bi bil različen za vsak mesec (velja en mesečni budget za vse mesece).

### Sprejemni kriteriji

**AC-2.1 – Opozorilo ob 80 % budgeta kategorije**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 70,00 €
- **When:** dodam strošek 10,00 € v kategorijo Hrana z datumom v novembru 2026
- **Then:** se prikaže neprebrano opozorilo »Poraba v kategoriji Hrana je dosegla 80 % budgeta (80,00 € od 100,00 €).«

**AC-2.2 – Opozorilo ob preseganju budgeta kategorije**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 90,00 €
- **When:** dodam strošek 15,00 € v kategorijo Hrana z datumom v novembru 2026
- **Then:** se prikaže opozorilo »Budget kategorije Hrana je presežen za 5,00 € (105,00 € od 100,00 €).«, strošek pa je kljub temu shranjen

**AC-2.3 – Poraba je natanko enaka budgetu**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 90,00 €
- **When:** dodam strošek 10,00 € v kategorijo Hrana z datumom v novembru 2026
- **Then:** se prikaže opozorilo »Budget kategorije Hrana je porabljen v celoti (100,00 € od 100,00 €).«

**AC-2.4 – Brez opozorila pod pragom**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 50,00 €
- **When:** dodam strošek 10,00 € v kategorijo Hrana z datumom v novembru 2026
- **Then:** se ne prikaže nobeno opozorilo (poraba je 60 %)

**AC-2.5 – Kategorija brez budgeta**
- **Given:** kategorija Zabava nima nastavljenega budgeta
- **When:** dodam strošek 500,00 € v kategorijo Zabava
- **Then:** se ne prikaže nobeno opozorilo za kategorijo Zabava

**AC-2.6 – Opozorilo ob 80 % mesečnega budgeta**
- **Given:** moj mesečni budget je 500,00 € in v novembru 2026 imam stroške v več kategorijah ter en strošek brez kategorije v skupni višini 380,00 €
- **When:** dodam strošek 20,00 € z datumom v novembru 2026
- **Then:** se prikaže opozorilo »Poraba v mesecu je dosegla 80 % mesečnega budgeta (400,00 € od 500,00 €).«

**AC-2.7 – Opozorilo ob preseganju mesečnega budgeta**
- **Given:** moj mesečni budget je 500,00 € in v novembru 2026 imam porabo 490,00 €
- **When:** dodam strošek 30,00 € z datumom v novembru 2026
- **Then:** se prikaže opozorilo »Mesečni budget je presežen za 20,00 € (520,00 € od 500,00 €).«

**AC-2.8 – Opozorilo se ne podvaja**
- **Given:** za kategorijo Hrana (budget 100,00 €) je prikazano opozorilo o 80 % budgeta
- **When:** dodam še strošek 5,00 €, tako da je poraba 85 %
- **Then:** je še vedno prikazano natanko eno opozorilo za kategorijo Hrana, ki kaže porabo 85,00 € od 100,00 €

**AC-2.9 – Preseganje nadomesti opozorilo o približevanju**
- **Given:** za kategorijo Hrana (budget 100,00 €) je prikazano neprebrano opozorilo o 90 % budgeta
- **When:** dodam strošek 15,00 €, tako da je poraba 105 %
- **Then:** je prikazano samo opozorilo o preseganju budgeta, opozorilo o približevanju pa ni več med neprebranimi

**AC-2.10 – Opozorilo ostane do branja**
- **Given:** za kategorijo Hrana je prikazano neprebrano opozorilo
- **When:** se odjavim, se znova prijavim in odprem stran s stroški
- **Then:** je opozorilo še vedno prikazano na vrhu strani

**AC-2.11 – Zapiranje opozorila**
- **Given:** prikazani sta dve neprebrani opozorili
- **When:** pri prvem kliknem »zapri« in osvežim stran
- **Then:** prvo opozorilo ni več prikazano, drugo pa je še vedno; ob izbiri »označi vse kot prebrano« se ne prikaže nobeno opozorilo več

**AC-2.12 – Prikaz stroškov iz opozorila**
- **Given:** je prikazano opozorilo za kategorijo Hrana
- **When:** kliknem »prikaži stroške«
- **Then:** se v polje za iskanje vpiše »Hrana« in seznam prikaže samo stroške, ki ustrezajo temu iskanju (med njimi stroški kategorije Hrana)

**AC-2.13 – Opozorilo izgine po povišanju budgeta**
- **Given:** kategorija Hrana ima budget 100,00 €, porabo 90,00 € in prikazano opozorilo o približevanju
- **When:** budget kategorije povišam na 200,00 € in shranim
- **Then:** opozorilo za kategorijo Hrana ni več prikazano (poraba je 45 %)

**AC-2.14 – Opozorilo se pojavi po znižanju budgeta**
- **Given:** kategorija Hrana ima budget 200,00 € in porabo 90,00 € brez opozorila
- **When:** budget kategorije znižam na 100,00 € in shranim
- **Then:** se prikaže opozorilo o približevanju budgetu (poraba je 90 %)

**AC-2.15 – Opozorilo izgine po brisanju stroška**
- **Given:** kategorija Hrana ima budget 100,00 €, porabo 105,00 € in prikazano opozorilo o preseganju
- **When:** izbrišem strošek v višini 50,00 €
- **Then:** opozorilo o preseganju ni več prikazano (poraba je 55 %)

**AC-2.16 – Upoštevanje meseca stroška**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 70,00 €
- **When:** dodam strošek 50,00 € v kategorijo Hrana z datumom 20. 10. 2026
- **Then:** se ne prikaže opozorilo za november 2026

**AC-2.17 – Neveljaven znesek stroška (scenarij napake)**
- **Given:** kategorija Hrana ima budget 100,00 € in v novembru 2026 porabo 70,00 €
- **When:** poskusim shraniti strošek z zneskom 0 ali negativnim zneskom
- **Then:** strošek ni shranjen, prikaže se sporočilo »Znesek mora biti večji od 0.« in opozorila se ne spremenijo

**AC-2.18 – Neveljaven mesečni budget (scenarij napake)**
- **Given:** sem na obrazcu za mesečni budget
- **When:** vnesem 0 ali negativen znesek in shranim
- **Then:** mesečni budget ni spremenjen in prikaže se sporočilo »Mesečni budget mora biti večji od 0.«

**AC-2.19 – Upoštevani so samo moji stroški**
- **Given:** drug uporabnik ima v kategoriji z istim imenom Hrana stroške v višini 500,00 €, jaz pa imam budget 100,00 € in porabo 10,00 €
- **When:** dodam strošek 5,00 € v svojo kategorijo Hrana
- **Then:** se ne prikaže nobeno opozorilo

### Podrobnosti implementacije

**Podatkovni model:**
- Nova entiteta `vao/Opozorilo` (shranjeno opozorilo): `id`, `uporabnik`, `kategorija` (`null` pri mesečnem budgetu), `leto`, `mesec`, `stopnja` (enum `StopnjaOpozorila`: `PRIBLIZEVANJE`, `PRESEGANJE`), `poraba`, `budzet`, `delez`, `prebrano`, `ustvarjeno`. Ob brisanju uporabnika ali kategorije se opozorila izbrišejo (`@OnDelete(CASCADE)`).
- `Uporabnik` dobi polje `mesecniBudzet` (`Double`, `null` = ni nastavljen).
- Nov repozitorij `dao/OpozoriloRepository`; `StrosekRepository` dobi poizvedbi `porabaPoKategorijah` (vsota po kategoriji za obdobje) in `skupnaPoraba` (vsota vseh stroškov v obdobju).

**Preverjanje budgeta (PU6)** – `service/PreverjanjeBudgetaService`:
- `preveri(uporabnik, datum)` izračuna porabo za mesec datuma in za vsak cilj (vsako kategorijo in mesečni budget) uskladi shranjena opozorila:
  - najvišja dosežena stopnja brez obstoječega opozorila: ustvari novo (neprebrano),
  - opozorilo, ki ga poraba ne dosega več (povišan budget, izbrisan strošek, odstranjen budget): izbriše,
  - opozorilo iste stopnje že obstaja: samo osveži zneske (brez podvajanja),
  - ob preseganju se opozorilo o približevanju označi kot prebrano.
- Pragova sta konstanti (`PRAG_PRIBLIZEVANJE = 0.8`, `PRAG_PRESEGANJE = 1.0`); pri primerjavi se upošteva majhna toleranca zaradi decimalnega seštevanja.
- Klic se izvede po uspešnem shranjevanju: `POST`/`PUT`/`DELETE` v `StrosekController` (mesec stroška, pri prestavitvi v drug mesec tudi prejšnji mesec), `POST`/`PUT` v `KategorijaController` (tekoči mesec) in `PUT /mesecni-budzet`. Napaka pri preverjanju se zabeleži, strošek pa ostane shranjen (izjema PU6).
- `StrosekController` zavrne strošek z zneskom ≤ 0 ali brez datuma (`400 Bad Request`).

**REST API** (vse zahteve zahtevajo prijavo, podatki so omejeni na prijavljenega uporabnika):

| Metoda | Pot | Opis | Odgovori |
|---|---|---|---|
| `GET` | `/opozorila` | Neprebrana opozorila (najnovejša prva); z `?vsa=true` tudi prebrana | `200` |
| `POST` | `/opozorila/preveri` | Ponovno preveri porabo v tekočem mesecu (ob odprtju aplikacije) | `204` |
| `PUT` | `/opozorila/{id}/prebrano` | Označi opozorilo kot prebrano | `204`, `404` |
| `PUT` | `/opozorila/prebrano` | Označi vsa opozorila kot prebrana | `204` |
| `GET` | `/mesecni-budzet` | Trenutni mesečni budget (`{"budzet": 500.0}` ali `null`) | `200` |
| `PUT` | `/mesecni-budzet` | Nastavi ali (z `null`) odstrani mesečni budget | `200`, `400` |

**Frontend:**
- `types.ts`, `api.ts`: tipa `Opozorilo` in `MesecniBudzet` ter funkcije `getOpozorila`, `preveriOpozorila`, `oznaciPrebrano`, `oznaciVsePrebrano`, `getMesecniBudzet`, `setMesecniBudzet`.
- `components/OpozorilaBanner.tsx`: seznam neprebranih opozoril (rumeno za približevanje, rdeče za preseganje) z gumbi »zapri«, »označi vse kot prebrano« in »prikaži stroške« (nastavi iskanje po imenu kategorije).
- `components/MesecniBudzetForm.tsx`: nastavitev mesečnega budgeta in prikaz porabe v tekočem mesecu.
- `Pregled.tsx`: ob odprtju pokliče `POST /opozorila/preveri` in naloži opozorila, po vsaki spremembi stroška, kategorije ali mesečnega budgeta pa jih znova naloži.

**Opombe:** opozorila so shranjena (za razliko od izračuna ob vsakem klicu), zato ostanejo, dokler jih uporabnik ne prebere (PU7, korak 4a). Zapis zneskov v opozorilu se osvežuje ob vsakem preverjanju. Mesečni budget je ena vrednost za vse mesece; podrobnejše upravljanje (zgodovina po mesecih) je del PU2 in ni del te zgodbe.

### Naloge (sub-issues)

| # | Naziv | Opis | Odgovorni |
|---|---|---|---|
| T-2.1 | Backend: entiteta `Opozorilo`, repozitorij in poizvedbe porabe | `Opozorilo`, `StopnjaOpozorila`, `OpozoriloRepository`, poizvedbi `porabaPoKategorijah` in `skupnaPoraba` v `StrosekRepository`. | Nikola Mojsov |
| T-2.2 | Backend: `PreverjanjeBudgetaService` in klici po spremembah | Usklajevanje opozoril (ustvari, osveži, izbriši, brez podvajanja), klici iz `StrosekController` in `KategorijaController`, validacija zneska in datuma stroška. | Nikola Mojsov |
| T-2.3 | Backend: REST API za opozorila in mesečni budget | `OpozorilaController`, `MesecniBudzetController`, polje `mesecniBudzet` pri `Uporabnik`, validacija. | Nikola Mojsov |
| T-2.4 | Frontend: prikaz in upravljanje opozoril | `OpozorilaBanner` (zapri, označi vse, prikaži stroške), klici API in osveževanje v `Pregled.tsx`. | Nikola Mojsov |
| T-2.5 | Frontend: nastavitev mesečnega budgeta in testi | `MesecniBudzetForm`, prikaz porabe v mesecu, enotni testi `PreverjanjeBudgetaServiceTest`, preverjanje kriterijev. | Nikola Mojsov |

### Preverjanje po implementaciji

| Kriterij | Kako preveriti |
|---|---|
| AC-2.1 – AC-2.5 | Ustvari kategorijo Hrana z budgetom 100 € in dodajaj stroške po scenarijih; enotni testi preverjajo meji 80 % in 100 %. |
| AC-2.6, AC-2.7 | Nastavi mesečni budget 500 €, dodaj stroške v več kategorij in enega brez kategorije. |
| AC-2.8, AC-2.9 | Dodajaj stroške znotraj istega praga in nato čez prag; v bazi (`opozorilo`) mora biti največ eno opozorilo na stopnjo in cilj. |
| AC-2.10, AC-2.11 | Osveži stran / se odjavi in prijavi; klikni »zapri« in »označi vse kot prebrano«. |
| AC-2.12 | Klikni »prikaži stroške« pri opozorilu. |
| AC-2.13 – AC-2.15 | Spremeni budget kategorije in izbriši strošek; opozorila morajo ustrezno izginiti ali se pojaviti. |
| AC-2.16 | Dodaj strošek z datumom v prejšnjem mesecu. |
| AC-2.17, AC-2.18 | Znesek 0 ali −5 pri stroških in mesečnem budgetu; pričakuj `400` in sporočilo. |
| AC-2.19 | Registriraj dva računa z enako imenovano kategorijo; stroški drugega računa ne smejo vplivati na opozorila. |

---

## US-3 – _(Lea Bogoevska – dopolni)_

Predloga za zapis zgodbe:

| Polje | Vrednost |
|---|---|
| **ID** | US-3 |
| **Odgovorni član** | Lea Bogoevska (@leabogoevska26) |
| **Primer uporabe** | |
| **Prioriteta** | |
| **Ocena zahtevnosti** | |

**Kot** … **želim** … **da** …

Sprejemni kriteriji (vsaj 3, vključno z enim scenarijem napake): *Given / When / Then*. Podrobnosti implementacije. Naloge (vsaj 2).

---

## Definition of Ready

Pred začetkom implementacije za vsako zgodbo velja:

- [x] uporabniška zgodba je jasno definirana (oblika *Kot … želim … da …*),
- [x] sprejemni kriteriji so jasni, merljivi in vključujejo scenarij napake,
- [x] ekipa razume namen in kontekst (povezava s PU v README),
- [x] podrobnosti implementacije so zapisane.

Po implementaciji vsak član preveri, ali funkcionalnost deluje v skladu s sprejemnimi kriteriji svoje zgodbe.
# 📋 Uporabniške zgodbe

| ID | Uporabniška zgodba | Odgovoren | Prioriteta | Ocena (SP) | GitHub |
|---|---|---|---|---|---|
| US-01 | Registracija | Staša Despotović | visoka | 5 | _issue_ |
| US-02 | Prijava in odjava | Staša Despotović | visoka | 3 | _issue_ |

<sub>Ocena zahtevnosti je v *story points* (Fibonaccijevo zaporedje 1, 2, 3, 5, 8).</sub>

**Definition of Ready** – z implementacijo začnemo, ko je zgodba jasno definirana, so sprejemni kriteriji jasni in merljivi, ekipa razume namen in kontekst ter so zapisane podrobnosti implementacije.

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

| ID | Odgovoren | Prioriteta | Ocena |
|---|---|---|---|
| US-02 | Staša Despotović ([@stasadespotovic1](https://github.com/stasadespotovic1)) | visoka | 3 SP |

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
