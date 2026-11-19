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
