import { useState } from "react";
import type { FormEvent } from "react";
import type { RegistracijaInput } from "../types";

type RegistracijaFormProps = {
  onRegistracija: (r: RegistracijaInput) => Promise<void>;
  onPrijava: () => void;
};

const EMAIL_RE = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const OBSTAJA = "Račun s tem e-poštnim naslovom že obstaja.";

export const RegistracijaForm = ({ onRegistracija, onPrijava }: RegistracijaFormProps) => {
  const [ime, setIme] = useState("");
  const [email, setEmail] = useState("");
  const [geslo, setGeslo] = useState("");
  const [ponovitev, setPonovitev] = useState("");
  const [napaka, setNapaka] = useState<string | null>(null);
  const [posilja, setPosilja] = useState(false);

  const preveri = (): string | null => {
    if (!ime.trim()) return "Ime je obvezno.";
    if (!EMAIL_RE.test(email.trim())) return "E-poštni naslov ni veljavne oblike."; // PU1 4a
    if (geslo.length < 8) return "Geslo mora imeti vsaj 8 znakov."; // PU1 4b
    if (geslo !== ponovitev) return "Gesli se ne ujemata."; // PU1 4b
    return null;
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const n = preveri();
    if (n) {
      setNapaka(n);
      return;
    }
    setPosilja(true);
    setNapaka(null);
    try {
      await onRegistracija({ ime: ime.trim(), email: email.trim(), geslo });
    } catch (err) {
      setNapaka((err as Error).message);
      setGeslo("");
      setPonovitev("");
    } finally {
      setPosilja(false);
    }
  };

  return (
    <form className="auth" onSubmit={handleSubmit} noValidate>
      <h2>Registracija</h2>
      {napaka && (
        <div className="alert" role="alert">
          {napaka}
          {napaka === OBSTAJA && (
            <button type="button" className="link" onClick={onPrijava}>Prijava</button>
          )}
        </div>
      )}
      <div className="form__field">
        <label className="form__label" htmlFor="reg-ime">Ime</label>
        <input id="reg-ime" className="input" autoComplete="given-name" value={ime} onChange={(e) => setIme(e.target.value)} required autoFocus />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="reg-email">E-poštni naslov</label>
        <input id="reg-email" className="input" type="email" autoComplete="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="reg-geslo">Geslo (vsaj 8 znakov)</label>
        <input id="reg-geslo" className="input" type="password" autoComplete="new-password" value={geslo} onChange={(e) => setGeslo(e.target.value)} required minLength={8} />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="reg-ponovitev">Ponovitev gesla</label>
        <input id="reg-ponovitev" className="input" type="password" autoComplete="new-password" value={ponovitev} onChange={(e) => setPonovitev(e.target.value)} required />
      </div>
      <div className="auth__actions">
        <button className="btn" type="submit" disabled={posilja}>Ustvari račun</button>
        <button className="btn btn--secondary" type="button" onClick={onPrijava}>Prekliči</button>
      </div>
      <p className="auth__switch">
        Že imaš račun? <button type="button" className="link" onClick={onPrijava}>Prijava</button>
      </p>
    </form>
  );
};
