import { useState } from "react";
import type { FormEvent } from "react";
import type { Kategorija, KategorijaInput } from "../types";

type KategorijaFormProps = {
  urejana: Kategorija | null;
  onSave: (k: KategorijaInput, id?: number) => void;
  onCancel: () => void;
};

export const KategorijaForm = ({ urejana, onSave, onCancel }: KategorijaFormProps) => {
  const [naziv, setNaziv] = useState(urejana?.naziv ?? "");
  const [budzet, setBudzet] = useState(urejana?.budzet?.toString() ?? "");

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSave({ naziv: naziv.trim(), budzet: budzet === "" ? null : Number(budzet) }, urejana?.id);
    setNaziv("");
    setBudzet("");
  };

  return (
    <form className={`form ${urejana ? "form--editing" : ""}`} onSubmit={handleSubmit}>
      <div className="form__field form__field--wide">
        <label className="form__label" htmlFor="naziv">{urejana ? "Nov naziv" : "Naziv kategorije"}</label>
        <input id="naziv" className="input" value={naziv} onChange={(e) => setNaziv(e.target.value)} autoFocus={!!urejana} required />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="budzet">Mesečni budžet (€)</label>
        <input
          id="budzet"
          className="input input--znesek"
          type="number"
          min="0"
          step="0.01"
          placeholder="brez"
          value={budzet}
          onChange={(e) => setBudzet(e.target.value)}
        />
      </div>
      <button className="btn" type="submit">{urejana ? "Shrani" : "Dodaj"}</button>
      {urejana && <button className="btn btn--secondary" type="button" onClick={onCancel}>Prekliči</button>}
    </form>
  );
};
