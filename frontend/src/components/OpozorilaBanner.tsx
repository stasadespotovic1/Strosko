import type { Opozorilo } from "../types";
import { formatZnesek } from "../format";

type OpozorilaBannerProps = {
  opozorila: Opozorilo[];
  onZapri: (id: number) => void;
  onZapriVse: () => void;
  onPrikazi: (naziv: string) => void;
};

const besedilo = (o: Opozorilo): string => {
  const znesek = `(${formatZnesek(o.poraba)} od ${formatZnesek(o.budzet)})`;
  const mesecni = o.kategorijaId === null;
  if (o.stopnja === "PRESEGANJE") {
    const presezek = o.poraba - o.budzet;
    const ime = mesecni ? "Mesečni budget" : `Budget kategorije ${o.naziv}`;
    return presezek > 0
      ? `${ime} je presežen za ${formatZnesek(presezek)} ${znesek}.`
      : `${ime} je porabljen v celoti ${znesek}.`;
  }
  const kje = mesecni ? "v mesecu" : `v kategoriji ${o.naziv}`;
  const cilj = mesecni ? "mesečnega budgeta" : "budgeta";
  return `Poraba ${kje} je dosegla ${Math.floor(o.delez)} % ${cilj} ${znesek}.`;
};

export const OpozorilaBanner = ({ opozorila, onZapri, onZapriVse, onPrikazi }: OpozorilaBannerProps) => {
  if (opozorila.length === 0) return null;
  return (
    <div className="opozorila" aria-label="Opozorila o budgetu">
      <div className="opozorila__glava">
        <strong>Opozorila ({opozorila.length})</strong>
        {opozorila.length > 1 && (
          <button className="link" onClick={onZapriVse}>označi vse kot prebrano</button>
        )}
      </div>
      {opozorila.map((o) => (
        <div
          key={o.id}
          className={o.stopnja === "PRESEGANJE" ? "alert" : "alert alert--warning"}
          role="alert"
        >
          {besedilo(o)} <span className="opozorila__mesec">{o.mesec}. {o.leto}</span>
          {o.kategorijaId !== null && (
            <button className="link" onClick={() => onPrikazi(o.naziv)}>prikaži stroške</button>
          )}
          <button className="link" onClick={() => onZapri(o.id)}>zapri</button>
        </div>
      ))}
    </div>
  );
};
