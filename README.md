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

1. [O projektu](#-o-projektu)
2. [Uporabljene tehnologije](#-uporabljene-tehnologije)
3. [Struktura projekta](#-struktura-projekta)
4. [Navodila za nameščanje in zagon](#-navodila-za-nameščanje-in-zagon)
5. [REST API](#-rest-api)
6. [Navodila za razvijalce](#-navodila-za-razvijalce)
7. [Standardi kodiranja](#-standardi-kodiranja)

---

## 📖 O projektu

**$troško** je spletna aplikacija, katere glavni cilj je čim večja **preglednost nad osebnimi financami** – prihodki in odhodki. Namenjena je predvsem študentom in mladim, ki pogosto nimajo pregleda nad drobnimi vsakodnevnimi stroški, ki se hitro naberejo.

### 🎯 Cilji

- Uporabniku jasno prikazati, **koliko je v tekočem mesecu že porabil** in **koliko mu še ostane**.
- Prikazati **razporeditev porabe po kategorijah**.
- **Pravočasno opozoriti** uporabnika, ko se približa ali preseže budget kategorije.
- Spodbujati **varčevanje** s spremljanjem varčevalnih ciljev.

### ✨ Funkcionalnosti

| Funkcionalnost | Opis | Status |
|---|---|---|
| Kategorije | Ustvarjanje, urejanje in brisanje lastnih kategorij (hrana, prevoz, zabava, naročnine …) | ✅ |
| Budget po kategorijah | Vsaka kategorija ima lahko svoj mesečni budget | ✅ |
| Vnos stroškov | Vsak zapis ima znesek, datum, kratek opis in kategorijo | ✅ |
| Mesečni budget | Splošen mesečni budget, nastavljen na začetku meseca | 🚧 |
| Vnos prihodkov | Štipendija, študentsko delo, žepnina … | 🚧 |
| Pregled porabe | Poraba v tekočem mesecu, preostanek in razporeditev po kategorijah | 🚧 |
| Opozorila | Obvestilo ob približevanju ali preseganju budgeta kategorije | 🚧 |
| Ponavljajoči se stroški | Najemnina, telefon, Netflix, Spotify … se vnesejo enkrat in se samodejno upoštevajo vsak mesec | 📝 |
| Varčevalni cilji | Npr. *300 € za poletje* – spremljanje, koliko ostane za cilj na koncu meseca | 📝 |

<sub>✅ implementirano · 🚧 v razvoju · 📝 načrtovano</sub>

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
│   │   │   │   ├── vao/                      # Entitete (modeli podatkov)
│   │   │   │   │   ├── Kategorija.java
│   │   │   │   │   └── Strosek.java
│   │   │   │   ├── dao/                      # Repozitoriji (dostop do baze)
│   │   │   │   │   ├── KategorijaRepository.java
│   │   │   │   │   └── StrosekRepository.java
│   │   │   │   └── rest/                     # REST kontrolerji (API končne točke)
│   │   │   │       ├── InfoController.java
│   │   │   │       ├── KategorijaController.java
│   │   │   │       └── StrosekController.java
│   │   │   └── resources/
│   │   │       └── application.properties    # Konfiguracija (port, baza, JPA)
│   │   └── test/                             # Testi
│   ├── mvnw / mvnw.cmd              # Skripti za zagon Maven wrapperja
│   └── pom.xml                      # Maven odvisnosti in nastavitve gradnje
│
├── frontend/                        # React + TypeScript odjemalec
│   ├── public/                      # Statične datoteke (favicon, ikone)
│   ├── src/
│   │   ├── components/              # React komponente
│   │   │   ├── KategorijaForm.tsx   # Obrazec za dodajanje/urejanje kategorije
│   │   │   ├── KategorijaList.tsx   # Seznam kategorij
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
└── README.md
```

### Razlaga map

**Backend** sledi slojni arhitekturi:

| Mapa | Vloga |
|---|---|
| `vao/` | *Value Access Objects* – JPA entitete, ki predstavljajo tabele v bazi (`Kategorija`, `Strosek`). |
| `dao/` | *Data Access Objects* – Spring Data JPA repozitoriji za CRUD operacije nad entitetami. |
| `rest/` | REST kontrolerji, ki sprejemajo HTTP zahteve, validirajo podatke in kličejo repozitorije. |
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

Nastavitve backenda so v [`backend/src/main/resources/application.properties`](backend/src/main/resources/application.properties). Vsako lastnost lahko prepišete z okoljsko spremenljivko, zato **gesla za bazo ne vpisujte v repozitorij**, ampak ga nastavite lokalno:

| Spremenljivka | Privzeta vrednost | Opis |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/stroski` | Naslov baze |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | Uporabnik baze |
| `SPRING_DATASOURCE_PASSWORD` | – | Geslo uporabnika baze |
| `SERVER_PORT` | `8100` | Port backenda |

**Windows (PowerShell):**

```powershell
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "vase_geslo"
```

**Linux / macOS:**

```bash
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=vase_geslo
```

> V IntelliJ IDEA jih lahko nastavite tudi v *Run → Edit Configurations → Environment variables*.

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
| `password authentication failed` | Preverite `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`. |
| `Port 8100 already in use` | Ustavite drug proces ali nastavite `SERVER_PORT`. |
| Napake CORS v brskalniku | Preverite, da backend teče in da se `BASE` v `api.ts` ujema s portom. |

---

## 🔌 REST API

Osnovni URL: `http://localhost:8100/api/v1`

| Metoda | Končna točka | Opis |
|---|---|---|
| `GET` | `/info` | Informacija o delovanju strežnika |
| `GET` | `/kategorije` | Seznam vseh kategorij |
| `GET` | `/kategorije/{id}` | Ena kategorija |
| `POST` | `/kategorije` | Nova kategorija |
| `PUT` | `/kategorije/{id}` | Posodobitev kategorije |
| `DELETE` | `/kategorije/{id}` | Brisanje kategorije |
| `GET` | `/stroski` | Seznam vseh stroškov |
| `GET` | `/stroski/{id}` | En strošek |
| `POST` | `/stroski` | Nov strošek |
| `PUT` | `/stroski/{id}` | Posodobitev stroška |
| `DELETE` | `/stroski/{id}` | Brisanje stroška |

<details>
<summary>Primer zahteve – nov strošek</summary>

```http
POST /api/v1/stroski
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

- gesla in drugi skrivni podatki (uporabite okoljske spremenljivke),
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
