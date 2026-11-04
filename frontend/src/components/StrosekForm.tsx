import { useState } from "react";
import type { FormEvent } from "react";
import type { Kategorija, Strosek, StrosekInput } from "../types";

type StrosekFormProps = {
  kategorije: Kategorija[];
  urejan: Strosek | null;
  onSave: (s: StrosekInput, id?: number) => void;
  onCancel: () => void;
};

const danes = () => new Date().toISOString().slice(0, 10);

export const StrosekForm = ({ kategorije, urejan, onSave, onCancel }: StrosekFormProps) => {
  const [opis, setOpis] = useState(urejan?.opis ?? "");
  const [znesek, setZnesek] = useState(urejan ? String(urejan.znesek) : "");
  const [datum, setDatum] = useState(urejan?.datum ?? danes());
  const [kategorijaId, setKategorijaId] = useState<number | "">(urejan?.kategorija?.id ?? "");

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const kategorija = kategorije.find((k) => k.id === kategorijaId) ?? null;
    onSave({ opis: opis.trim(), znesek: Number(znesek), datum, kategorija }, urejan?.id);
  };

  return (
    <form className={`form ${urejan ? "form--editing" : ""}`} onSubmit={handleSubmit}>
      <div className="form__field">
        <label className="form__label" htmlFor="datum">Datum</label>
        <input id="datum" className="input" type="date" value={datum} onChange={(e) => setDatum(e.target.value)} required />
      </div>
      <div className="form__field form__field--wide">
        <label className="form__label" htmlFor="opis">Opis</label>
        <input id="opis" className="input" value={opis} onChange={(e) => setOpis(e.target.value)} required />
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="kategorija">Kategorija</label>
        <select
          id="kategorija"
          className="input"
          value={kategorijaId}
          onChange={(e) => setKategorijaId(e.target.value === "" ? "" : Number(e.target.value))}
          required
        >
          <option value="">{kategorije.length ? "– izberi –" : "– ni kategorij –"}</option>
          {kategorije.map((k) => (
            <option key={k.id} value={k.id}>{k.naziv}</option>
          ))}
        </select>
      </div>
      <div className="form__field">
        <label className="form__label" htmlFor="znesek">Znesek (€)</label>
        <input id="znesek" className="input input--znesek" type="number" step="0.01" min="0" value={znesek} onChange={(e) => setZnesek(e.target.value)} required />
      </div>
      <button className="btn" type="submit">{urejan ? "Shrani" : "Dodaj"}</button>
      {urejan && <button className="btn btn--secondary" type="button" onClick={onCancel}>Prekliči</button>}
      
    </form>
  );
};
