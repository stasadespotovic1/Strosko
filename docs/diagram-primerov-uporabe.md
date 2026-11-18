# 🧩 Razlaga povezav na diagramu primerov uporabe

Podporni dokument k [diagramu primerov uporabe](../README.md#-diagram-primerov-uporabe). Podrobni opisi primerov uporabe so v [`primeri_uporabe.md`](../primeri_uporabe.md).

| Povezava | Razlaga |
|---|---|
| **PU3 Vnos stroška «include» PU6 Preverjanje budgeta** | Ob vsakem vnesenem strošku sistem vedno preveri porabo glede na budget. |
| **PU2 Upravljanje kategorij in budgetov «include» PU6 Preverjanje budgeta** | Ob vsaki spremembi budgeta kategorije ali mesečnega budgeta sistem vedno znova preveri, ali trenutna poraba že dosega nov budget. |
| **PU7 Prikaz opozorila «extend» PU6 Preverjanje budgeta** | Opozorilo se prikaže samo pod pogojem, da poraba doseže 80 % ali več budgeta. |
| **PU7 Prikaz opozorila «extend» PU5 Pregled porabe** | Ob odprtju pregleda porabe se opozorila prikažejo samo pod pogojem, da ima uporabnik neprebrana opozorila (npr. ker je bil strošek vnesen samodejno, ko ni bil prijavljen). |
| **Administrator → Registriran uporabnik (generalizacija)** | Administrator podeduje vse primere uporabe registriranega uporabnika. Na diagramu je prikazana s polno črto in praznim trikotnikom, ki kaže proti registriranemu uporabniku. |
