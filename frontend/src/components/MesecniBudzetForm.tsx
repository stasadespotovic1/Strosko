import { useState } from "react";
import type { FormEvent } from "react";
import { formatZnesek } from "../format";

type MesecniBudzetFormProps = {
  budzet: number | null;
  poraba: number;
  onSave: (budzet: number | null) => void;
};

export const MesecniBudzetForm = ({ budzet, poraba, onSave }: MesecniBudzetFormProps) => {
  const [vnos, setVnos] = useState(budzet?.toString() ?? "");

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    onSave(vnos.trim() === "" ? null : Number(vnos));
  };

  return (
    <>
      <form className="form" onSubmit={handleSubmit}>
        <div className="form__field">
          <label className="form__label" htmlFor="mesecni-budzet">Mesečni budget (€)</label>
          <input
            id="mesecni-budzet"
            className="input input--znesek"
            type="number"
            min="0.01"
            step="0.01"
            placeholder="brez"
            value={vnos}
            onChange={(e) => setVnos(e.target.value)}
          />
        </div>
        <button className="btn" type="submit">Shrani</button>
      </form>
      {budzet !== null && (
        <p className="budzet-info">
          Poraba v tem mesecu: {formatZnesek(poraba)} od {formatZnesek(budzet)} · ostane {formatZnesek(budzet - poraba)}
        </p>
      )}
    </>
  );
};
