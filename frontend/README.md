# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Oxc](https://oxc.rs)
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/)

## React Compiler

The React Compiler is not enabled on this template because of its impact on dev & build performances. To add it, see [this documentation](https://react.dev/learn/react-compiler/installation).

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...

      // Remove tseslint.configs.recommended and replace with this
      tseslint.configs.recommendedTypeChecked,
      // Alternatively, use this for stricter rules
      tseslint.configs.strictTypeChecked,
      // Optionally, add this for stylistic rules
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])

```

You can also install [eslint-plugin-react-x](https://npmx.dev/package/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://npmx.dev/package/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])

```
## Razredni diagram

![Razredni diagram](docs/razredni-diagram.png)

Diagram prikazuje celotno načrtovano zgradbo aplikacije (PU1–PU10), vključno s še neimplementiranimi razredi (`Prihodek`, `PonavljajociStrosek`, `VarcevalniCilj`, `Administrator`).

### Vloga in namen razredov

| Razred | Vloga in namen |
|---|---|
| `Uporabnik` | Registriran uporabnik. Lastnik vseh podatkov (stroški, kategorije, prihodki, cilji, opozorila, seje). |
| `Administrator` | Specializacija uporabnika (PU10): upravlja uporabniške račune (blokiranje, odblokiranje, brisanje). |
| `Vloga` | Naštevni tip vloge (`UPORABNIK`, `ADMINISTRATOR`). |
| `Seja` | Prijava uporabnika z žetonom in rokom veljavnosti (7 dni). |
| `Kategorija` | Skupina stroškov z neobveznim mesečnim budgetom. |
| `Strosek` | Posamezen izdatek (opis, znesek, datum, kategorija). |
| `Prihodek` | Prejet denar (PU4), uporablja se pri izračunu preostanka. |
| `PonavljajociStrosek` | Predloga za strošek, ki se vsak mesec samodejno ustvari (PU8). |
| `VarcevalniCilj` | Cilj varčevanja z zneskom, rokom in napredkom (PU9). |
| `MesecniBudzet` | Skupni budget uporabnika za določen mesec (PU2). |
| `Opozorilo` | Obvestilo o približevanju ali preseganju budgeta (PU7). |
| `StopnjaOpozorila` | Naštevni tip: približevanje (od 80 %), preseganje (od 100 %). |
| `PreverjanjeBudgetaService` | Po vsakem strošku ali spremembi budgeta preveri porabo in ustvari opozorila (PU6). |
| `PregledPorabe` | Pregled porabe za izbran mesec: vsote, preostanek in primerjava s prejšnjim mesecem (PU5). |
| `PostavkaPregleda` | Ena vrstica pregleda: poraba kategorije, delež, budget in preseženje. |

### Ključne metode

| Metoda | Naloga |
|---|---|
| `Uporabnik.prijavi(email, geslo)` | Preveri geslo in ustvari novo sejo z žetonom. |
| `PreverjanjeBudgetaService.preveri(uporabnik, datum)` | Izračuna porabo po kategorijah in v mesecu ter ustvari opozorilo, če je dosežen prag. |
| `PreverjanjeBudgetaService.dolociStopnjo(poraba, budzet)` | Vrne `PRIBLIZEVANJE` (od 80 %), `PRESEGANJE` (od 100 %) ali nič. |
| `PonavljajociStrosek.ustvariStrosek(datum)` | Iz predloge ustvari nov strošek na dan zapadlosti. |
| `VarcevalniCilj.dodajVplacilo(znesek)` | Poveča privarčevani znesek in po potrebi označi cilj kot dosežen. |
| `MesecniBudzet.izracunajPreostanek(poraba)` | Vrne budget minus poraba za mesec. |
| `PregledPorabe.izracunaj(uporabnik, mesec)` | Sestavi pregled po kategorijah z deleži in primerjavo s prejšnjim mesecem. |
| `Administrator.blokiraj(uporabnik)` | Označi račun kot blokiran in prekine njegove seje. |

### Odnosi med razredi

- **Generalizacija:** `Administrator` je poseben `Uporabnik`.
- **Kompozicija:** vse, kar pripada uporabniku, se ob izbrisu računa izbriše z njim (PU10).
- **Asociacija:** `Strosek` se nanaša na eno kategorijo (ali nobene), ki ostane pri brisanju kategorije.
- **Odvisnost:** storitev `PreverjanjeBudgetaService` bere stroške, kategorije in budget ter ustvarja opozorila.
