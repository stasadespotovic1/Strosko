import type { Strosek } from "../types";
import { formatDatum, formatZnesek } from "../format";

type StrosekListProps = {
  stroski: Strosek[];
  urejanId?: number;
  praznoSporocilo?: string;
  onEdit: (s: Strosek) => void;
  onDelete: (id: number) => void;
};

export const StrosekList = ({
  stroski,
  urejanId,
  praznoSporocilo = "Ni še nobenega stroška.",
  onEdit,
  onDelete,
}: StrosekListProps) => {
  if (stroski.length === 0) return <p className="empty">{praznoSporocilo}</p>;

  const urejeni = [...stroski].sort((a, b) => b.datum.localeCompare(a.datum));
  const skupaj = stroski.reduce((sum, s) => sum + s.znesek, 0);

  return (
    <table className="table">
      <thead>
        <tr>
          <th>Datum</th>
          <th>Opis</th>
          <th>Kategorija</th>
          <th className="table__num">Znesek</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {urejeni.map((s) => (
          <tr key={s.id} className={s.id === urejanId ? "table__row--active" : ""}>
            <td className="table__muted">{formatDatum(s.datum)}</td>
            <td>{s.opis}</td>
            <td className={s.kategorija ? "" : "table__muted"}>{s.kategorija?.naziv ?? "—"}</td>
            <td className="table__num">{formatZnesek(s.znesek)}</td>
            <td className="table__actions">
              <button className="link" onClick={() => onEdit(s)}>uredi</button>
              <button className="link link--danger" onClick={() => onDelete(s.id)}>izbriši</button>
            </td>
          </tr>
        ))}
      </tbody>
      <tfoot>
        <tr>
          <td colSpan={3}>Skupaj</td>
          <td className="table__num">{formatZnesek(skupaj)}</td>
          <td></td>
        </tr>
      </tfoot>
    </table>
  );
};
