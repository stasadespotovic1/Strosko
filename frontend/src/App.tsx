import { useEffect, useState } from "react";
import type { PrijavaInput, RegistracijaInput, Uporabnik } from "./types";
import { clearToken, getJaz, getToken, odjava, prijava, registracija, setOnUnauthorized } from "./api";
import { Pregled } from "./components/Pregled";
import { PrijavaForm } from "./components/PrijavaForm";
import { RegistracijaForm } from "./components/RegistracijaForm";

const App = () => {
  const [uporabnik, setUporabnik] = useState<Uporabnik | null>(null);
  // dokler preverjamo shranjen žeton, ne prikažemo ničesar (brez utripa prijavnega obrazca)
  const [preverjam, setPreverjam] = useState(() => getToken() !== null);
  const [zaslon, setZaslon] = useState<"prijava" | "registracija">("prijava");

  useEffect(() => {
    setOnUnauthorized(() => setUporabnik(null));
    if (!getToken()) return;
    getJaz()
      .then(setUporabnik)
      .catch(() => clearToken())
      .finally(() => setPreverjam(false));
  }, []);

  const handlePrijava = async (p: PrijavaInput) => {
    setUporabnik(await prijava(p));
  };

  const handleRegistracija = async (r: RegistracijaInput) => {
    setUporabnik(await registracija(r));
  };

  const handleOdjava = async () => {
    try {
      await odjava();
    } catch {
      // žeton je lokalno vseeno pobrisan
    }
    setUporabnik(null);
    setZaslon("prijava");
  };

  if (preverjam) return null;

  if (uporabnik) return <Pregled key={uporabnik.id} uporabnik={uporabnik} onOdjava={handleOdjava} />;

  return (
    <>
      <header className="header">
        <h1>$troško</h1>
        <p className="header__info">Pregled nad tvojimi stroški.</p>
      </header>
      {zaslon === "prijava" ? (
        <PrijavaForm onPrijava={handlePrijava} onRegistracija={() => setZaslon("registracija")} />
      ) : (
        <RegistracijaForm onRegistracija={handleRegistracija} onPrijava={() => setZaslon("prijava")} />
      )}
    </>
  );
};

export default App;
