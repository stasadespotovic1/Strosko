import type { Kategorija, Strosek } from "../types";
import { formatZnesek } from "../format";

type KategorijaListProps = {
  kategorije: Kategorija[];
  stroski: Strosek[];
  urejanaId?: number;
  onEdit: (k: Kategorija) => void;
  onDelete: (id: number) => void;
};

const tekociMesec = () => {
  const d = new Date();
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`;
};

export const KategorijaList = ({ kategorije, stroski, urejanaId, onEdit, onDelete }: KategorijaListProps) => {
  if (kategorije.length === 0) return <p className="empty">Ni še nobene kategorije.</p>;

  const mesec = tekociMesec();

  return (
    <ul className="kategorije">
      {kategorije.map((k) => {
        const porabljeno = stroski
          .filter((s) => s.kategorija?.id === k.id && s.datum.startsWith(mesec))
          .reduce((sum, s) => sum + s.znesek, 0);
        const presezeno = k.budzet != null && porabljeno > k.budzet;
        const delez = k.budzet ? Math.min(100, (porabljeno / k.budzet) * 100) : 100;

        return (
          <li key={k.id} className={`kategorije__item ${k.id === urejanaId ? "kategorije__item--active" : ""}`}>
            <div className="budzet">
              <span>{k.naziv}</span>
              <span className={`budzet__info ${presezeno ? "budzet__info--over" : ""}`}>
                {k.budzet == null
                  ? `Ta mesec: ${formatZnesek(porabljeno)} · brez budžeta`
                  : presezeno
                    ? `${formatZnesek(porabljeno)} od ${formatZnesek(k.budzet)} · preseženo za ${formatZnesek(porabljeno - k.budzet)}`
                    : `${formatZnesek(porabljeno)} od ${formatZnesek(k.budzet)} · ostane ${formatZnesek(k.budzet - porabljeno)}`}
              </span>
              {k.budzet != null && (
                <div className="budzet__bar">
                  <div className={`budzet__fill ${presezeno ? "budzet__fill--over" : ""}`} style={{ width: `${delez}%` }} />
                </div>
              )}
            </div>
            <span>
              <button className="link" onClick={() => onEdit(k)}>uredi</button>
              <button className="link link--danger" onClick={() => onDelete(k.id)}>izbriši</button>
            </span>
          </li>
        );
      })}
    </ul>
  );
};
