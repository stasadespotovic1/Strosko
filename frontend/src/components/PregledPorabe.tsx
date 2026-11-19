import { useEffect, useState } from "react";
import type { PregledPorabeOdgovor, Strosek } from "../types";
import { getPregledPorabe } from "../api";
import { formatZnesek } from "../format";

type PregledPorabeProps = {
    stroski: Strosek[]; // ob spremembi stroškov se pregled osveži
};

const tekociMesec = () => {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`;
};

const formatDelez = (d: number) => `${d.toLocaleString("sl-SI", { maximumFractionDigits: 1 })} %`;

export const PregledPorabe = ({ stroski }: PregledPorabeProps) => {
    const [mesec, setMesec] = useState(tekociMesec());
    const [podatki, setPodatki] = useState<PregledPorabeOdgovor | null>(null);
    const [napaka, setNapaka] = useState<string | null>(null);

    useEffect(() => {
        if (!mesec) return;
        let preklicano = false;
        getPregledPorabe(mesec)
            .then((p) => {
                if (!preklicano) {
                    setPodatki(p);
                    setNapaka(null);
                }
            })
            .catch((e) => !preklicano && setNapaka((e as Error).message));
        return () => {
            preklicano = true;
        };
    }, [mesec, stroski]);

    const razlika = podatki ? podatki.skupaj - podatki.skupajPrejsnji : 0;

    return (
        <section>
            <h2>Pregled porabe</h2>
            <div className="form">
                <div className="form__field">
                    <label className="form__label" htmlFor="pregled-mesec">Mesec</label>
                    <input id="pregled-mesec" className="input" type="month" value={mesec} onChange={(e) => setMesec(e.target.value)} />
                </div>
            </div>

            {!mesec && <p className="empty">Izberite mesec za prikaz pregleda.</p>}
            {mesec && napaka && <div className="alert" role="alert">{napaka}</div>}

            {mesec && podatki && podatki.postavke.length === 0 && (
                <p className="empty">Ni stroškov za izbrani mesec.</p>
            )}

            {mesec && podatki && podatki.postavke.length > 0 && (
                <>
                    <p>
                        Skupaj: <strong>{formatZnesek(podatki.skupaj)}</strong>
                        {" · "}
                        {razlika === 0
                            ? "enako kot prejšnji mesec"
                            : razlika > 0
                                ? `za ${formatZnesek(razlika)} več kot prejšnji mesec`
                                : `za ${formatZnesek(-razlika)} manj kot prejšnji mesec`}
                    </p>
                    <table className="table">
                        <thead>
                        <tr>
                            <th>Kategorija</th>
                            <th className="table__num">Porabljeno</th>
                            <th className="table__num">Delež</th>
                            <th className="table__num">Budžet</th>
                            <th className="table__num">Prejšnji mesec</th>
                        </tr>
                        </thead>
                        <tbody>
                        {podatki.postavke.map((p) => (
                            <tr key={p.kategorijaId ?? "brez"}>
                                <td className={p.kategorijaId === null ? "table__muted" : ""}>{p.naziv}</td>
                                <td className={`table__num ${p.preseceno ? "over" : ""}`}>{formatZnesek(p.porabljeno)}</td>
                                <td className="table__num">{formatDelez(p.delez)}</td>
                                <td className="table__num">
                                    {p.budzet == null ? "—" : formatZnesek(p.budzet)}
                                    {p.preseceno && <span className="over"> (preseženo)</span>}
                                </td>
                                <td className="table__num table__muted">{formatZnesek(p.porabljenoPrejsnji)}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </>
            )}
        </section>
    );
};