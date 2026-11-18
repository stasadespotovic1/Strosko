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