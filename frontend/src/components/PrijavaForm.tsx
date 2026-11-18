import { useState } from "react";
import type { FormEvent } from "react";
import type { PrijavaInput } from "../types";

type PrijavaFormProps = {
  onPrijava: (p: PrijavaInput) => Promise<void>;
  onRegistracija: () => void;
};

export const PrijavaForm = ({ onPrijava, onRegistracija }: PrijavaFormProps) => {
  const [email, setEmail] = useState("");
  const [geslo, setGeslo] = useState("");
  const [napaka, setNapaka] = useState<string | null>(null);
  const [posilja, setPosilja] = useState(false);

  const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setPosilja(true);
    setNapaka(null);
    try {
      await onPrijava({ email: email.trim(), geslo });
    } catch (err) {
      setNapaka((err as Error).message);
      setGeslo("");
    } finally {
      setPosilja(false);
    }
  };

  return (
    <form className="auth" onSubmit={handleSubmit}>
      <h2>Prijava</h2>
      {napaka && <div className="alert" role="alert">{napaka}</div>}
      <div className="form__field">
        <label className="form__label" htmlFor="prijava-email">E-poštni naslov</label>
        <input id="prijava-email" className="input" type="email" autoComplete="email" value={email} onChange={(e) => setEmail(e.target.value)} required autoFocus />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="prijava-geslo">Geslo</label>
        <input id="prijava-geslo" className="input" type="password" autoComplete="current-password" value={geslo} onChange={(e) => setGeslo(e.target.value)} required />
      </div>
      <button className="btn" type="submit" disabled={posilja}>Prijava</button>
      <p className="auth__switch">
        Še nimaš računa? <button type="button" className="link" onClick={onRegistracija}>Registracija</button>
      </p>
    </form>
  );
};
