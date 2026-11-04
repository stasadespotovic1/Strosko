import { useEffect, useState } from "react";
import type { Kategorija, KategorijaInput, Strosek, StrosekInput } from "./types";
import {
  createKategorija,
  createStrosek,
  deleteKategorija,
  deleteStrosek,
  getKategorija,
  getKategorije,
  getStrosek,
  getStroski,
  updateKategorija,
  updateStrosek,
} from "./api";
import { StrosekList } from "./components/StrosekList";
import { StrosekForm } from "./components/StrosekForm";
import { KategorijaList } from "./components/KategorijaList";
import { KategorijaForm } from "./components/KategorijaForm";
import { formatZnesek } from "./format";

const App = () => {
  const [stroski, setStroski] = useState<Strosek[]>([]);
  const [kategorije, setKategorije] = useState<Kategorija[]>([]);
  const [urejan, setUrejan] = useState<Strosek | null>(null);
  const [urejana, setUrejana] = useState<Kategorija | null>(null);
  const [napaka, setNapaka] = useState<string | null>(null);
  const [iskanje, setIskanje] = useState("");

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

  useEffect(() => {
    getStroski().then(setStroski).catch((e) => setNapaka(e.message));
    getKategorije().then(setKategorije).catch((e) => setNapaka(e.message));
  }, []);

  const handleSave = async (s: StrosekInput, id?: number) => {
    try {
      if (id) await updateStrosek(id, s);
      else await createStrosek(s);
      setUrejan(null);
      setNapaka(null);
      await naloziStroske();
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
    } catch (e) {
      setNapaka((e as Error).message);
    }
  };

  const skupaj = stroski.reduce((sum, s) => sum + s.znesek, 0);

  const q = iskanje.trim().toLowerCase();
  const filtrirani = q
    ? stroski.filter(
        (s) => s.opis.toLowerCase().includes(q) || (s.kategorija?.naziv.toLowerCase().includes(q) ?? false)
      )
    : stroski;

  return (
    <>
      <header className="header">
        <h1>Moji stroški</h1>
        <p className="header__info">
          {stroski.length} {stroski.length === 1 ? "vnos" : "vnosov"}, skupaj <strong>{formatZnesek(skupaj)}</strong>
        </p>
      </header>

      {napaka && (
        <div className="alert" role="alert">
          {napaka}
          <button className="link" onClick={() => setNapaka(null)}>zapri</button>
        </div>
      )}

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

export default App;
