import { useEffect, useState } from "react";
import type { Kategorija, KategorijaInput, Opozorilo, Strosek, StrosekInput, Uporabnik } from "../types";
import {
  createKategorija,
  createStrosek,
  deleteKategorija,
  deleteStrosek,
  getKategorija,
  getKategorije,
  getMesecniBudzet,
  getOpozorila,
  getStrosek,
  getStroski,
  isciStroske,
  oznaciPrebrano,
  oznaciVsePrebrano,
  preveriOpozorila,
  setMesecniBudzet as shraniMesecniBudzet,
  updateKategorija,
  updateStrosek,
} from "../api";
import { StrosekList } from "./StrosekList";
import { StrosekForm } from "./StrosekForm";
import { KategorijaList } from "./KategorijaList";
import { KategorijaForm } from "./KategorijaForm";
import { MesecniBudzetForm } from "./MesecniBudzetForm";
import { OpozorilaBanner } from "./OpozorilaBanner";
import { formatZnesek } from "../format";
import { PregledPorabe } from "./PregledPorabe.tsx"

type PregledProps = {
  uporabnik: Uporabnik;
  onOdjava: () => void;
};

export const Pregled = ({ uporabnik, onOdjava }: PregledProps) => {
  const [stroski, setStroski] = useState<Strosek[]>([]);
  const [kategorije, setKategorije] = useState<Kategorija[]>([]);
  const [urejan, setUrejan] = useState<Strosek | null>(null);
  const [urejana, setUrejana] = useState<Kategorija | null>(null);
  const [napaka, setNapaka] = useState<string | null>(null);
  const [iskanje, setIskanje] = useState("");
  const [zadetki, setZadetki] = useState<Strosek[] | null>(null);
  const [opozorila, setOpozorila] = useState<Opozorilo[]>([]);
  const [mesecniBudzet, setMesecniBudzet] = useState<number | null>(null);
  // začetek datumov v tekočem mesecu (YYYY-MM), za prikaz porabe v mesecu
  const [tekociMesec] = useState(() => {
    const d = new Date();
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`;
  });

  const naloziStroske = async () => {
    try {
      setStroski(await getStroski());
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const naloziKategorije = async () => {
    try {
      setKategorije(await getKategorije());
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  // opozorila so dodatek; strošek ali budget ostane shranjen tudi, če jih ni mogoče naložiti
  const naloziOpozorila = async () => {
    try {
      setOpozorila(await getOpozorila());
    } catch {
      setOpozorila([]);
    }
  };

  useEffect(() => {
    // ob odprtju se poraba ponovno preveri (PU6), nato se naložijo neprebrana opozorila
    preveriOpozorila()
      .catch(() => undefined)
      .then(() => getOpozorila())
      .then(setOpozorila)
      .catch(() => setOpozorila([]));
    getMesecniBudzet().then((r) => setMesecniBudzet(r.budzet)).catch((e) => setNapaka(e.message));
    getStroski().then(setStroski).catch((e) => setNapaka(e.message));
    getKategorije().then(setKategorije).catch((e) => setNapaka(e.message));
  }, []);

  const q = iskanje.trim();

  useEffect(() => {
    if (!q) return;
    let preklicano = false;
    const timer = setTimeout(() => {
      isciStroske(q)
        .then((r) => !preklicano && setZadetki(r))
        .catch((e) => !preklicano && setNapaka(e.message));
    }, 300);
    return () => {
      preklicano = true;
      clearTimeout(timer);
    };
  }, [q, stroski]);

  const handleSave = async (s: StrosekInput, id?: number) => {
    try {
      if (id) await updateStrosek(id, s);
      else await createStrosek(s);
      setUrejan(null);
      setNapaka(null);
      await naloziStroske();
      await naloziOpozorila();
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleEdit = async (s: Strosek) => {
    try {
      setUrejan(await getStrosek(s.id));
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Ali res želiš izbrisati ta strošek?")) return;
    try {
      await deleteStrosek(id);
      await naloziStroske();
      await naloziOpozorila();
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleSaveKategorija = async (k: KategorijaInput, id?: number) => {
    try {
      if (id) await updateKategorija(id, k);
      else await createKategorija(k);
      setUrejana(null);
      setNapaka(null);
      await naloziKategorije();
      await naloziStroske();
      await naloziOpozorila();
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleEditKategorija = async (k: Kategorija) => {
    try {
      setUrejana(await getKategorija(k.id));
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleDeleteKategorija = async (id: number) => {
    if (!window.confirm("Izbrišem kategorijo? Stroški ostanejo, le brez kategorije.")) return;
    try {
      await deleteKategorija(id);
      await naloziKategorije();
      await naloziStroske();
      await naloziOpozorila();
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleSaveMesecniBudzet = async (budzet: number | null) => {
    try {
      const r = await shraniMesecniBudzet(budzet);
      setMesecniBudzet(r.budzet);
      setNapaka(null);
      await naloziOpozorila();
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleZapriOpozorilo = async (id: number) => {
    try {
      await oznaciPrebrano(id);
      setOpozorila((trenutna) => trenutna.filter((o) => o.id !== id));
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const handleZapriVsa = async () => {
    try {
      await oznaciVsePrebrano();
      setOpozorila([]);
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const skupaj = stroski.reduce((sum, s) => sum + s.znesek, 0);

  const porabaMesec = stroski
    .filter((s) => s.datum.startsWith(tekociMesec))
    .reduce((sum, s) => sum + s.znesek, 0);

  const filtrirani = q ? zadetki ?? [] : stroski;

  return (
    <>
      <header className="header header--user">
        <div>
          <h1>Moji stroški</h1>
          <p className="header__info">
            {stroski.length} {stroski.length === 1 ? "vnos" : "vnosov"}, skupaj <strong>{formatZnesek(skupaj)}</strong>
          </p>
        </div>
        <div className="header__user">
          <span>{uporabnik.ime}</span>
          <button className="btn btn--secondary" onClick={onOdjava}>Odjava</button>
        </div>
      </header>

      {napaka && (
        <div className="alert" role="alert">
          {napaka}
          <button className="link" onClick={() => setNapaka(null)}>zapri</button>
        </div>
      )}

      <OpozorilaBanner
        opozorila={opozorila}
        onZapri={handleZapriOpozorilo}
        onZapriVse={handleZapriVsa}
        onPrikazi={setIskanje}
      />

      <section>
        <h2>{urejan ? "Urejanje stroška" : "Nov strošek"}</h2>
        <StrosekForm
          key={urejan?.id ?? "nov"}
          kategorije={kategorije}
          urejan={urejan}
          onSave={handleSave}
          onCancel={() => setUrejan(null)}
        />
        <h2>Vsi stroški</h2>
        <input
          className="input search"
          type="search"
          placeholder="Išči po opisu ali kategoriji…"
          value={iskanje}
          onChange={(e) => setIskanje(e.target.value)}
        />
        <StrosekList
          stroski={filtrirani}
          urejanId={urejan?.id}
          praznoSporocilo={q ? "Ni zadetkov za iskanje." : undefined}
          onEdit={handleEdit}
          onDelete={handleDelete}
        />
      </section>

      <PregledPorabe stroski={stroski} />

      <section>
        <h2>Mesečni budget</h2>
        <MesecniBudzetForm
          key={mesecniBudzet ?? "brez"}
          budzet={mesecniBudzet}
          poraba={porabaMesec}
          onSave={handleSaveMesecniBudzet}
        />
      </section>

      <section>
        <h2>Kategorije</h2>
        <KategorijaForm
          key={urejana?.id ?? "nova"}
          urejana={urejana}
          onSave={handleSaveKategorija}
          onCancel={() => setUrejana(null)}
        />
        <KategorijaList
          kategorije={kategorije}
          stroski={stroski}
          urejanaId={urejana?.id}
          onEdit={handleEditKategorija}
          onDelete={handleDeleteKategorija}
        />
      </section>
    </>
  );
};
