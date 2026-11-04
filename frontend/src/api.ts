import type { Kategorija, KategorijaInput, Strosek, StrosekInput } from "./types";

const BASE = "http://localhost:8100/api/v1";

export const getStroski = async (): Promise<Strosek[]> => {
  const res = await fetch(`${BASE}/stroski`);
  if (!res.ok) throw new Error("Napaka pri branju stroškov");
  return res.json();
};

export const getStrosek = async (id: number): Promise<Strosek> => {
  const res = await fetch(`${BASE}/stroski/${id}`);
  if (!res.ok) throw new Error("Strošek ne obstaja");
  return res.json();
};

export const createStrosek = async (s: StrosekInput): Promise<Strosek> => {
  const res = await fetch(`${BASE}/stroski`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(s),
  });
  if (!res.ok) throw new Error("Napaka pri dodajanju");
  return res.json();
};

export const updateStrosek = async (id: number, s: StrosekInput): Promise<Strosek> => {
  const res = await fetch(`${BASE}/stroski/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(s),
  });
  if (!res.ok) throw new Error("Napaka pri posodabljanju");
  return res.json();
};

export const deleteStrosek = async (id: number): Promise<void> => {
  const res = await fetch(`${BASE}/stroski/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Napaka pri brisanju");
};

export const getKategorije = async (): Promise<Kategorija[]> => {
  const res = await fetch(`${BASE}/kategorije`);
  if (!res.ok) throw new Error("Napaka pri branju kategorij");
  return res.json();
};

export const getKategorija = async (id: number): Promise<Kategorija> => {
  const res = await fetch(`${BASE}/kategorije/${id}`);
  if (!res.ok) throw new Error("Kategorija ne obstaja");
  return res.json();
};

export const createKategorija = async (k: KategorijaInput): Promise<Kategorija> => {
  const res = await fetch(`${BASE}/kategorije`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(k),
  });
  if (!res.ok) throw new Error("Napaka pri dodajanju kategorije");
  return res.json();
};

export const updateKategorija = async (id: number, k: KategorijaInput): Promise<Kategorija> => {
  const res = await fetch(`${BASE}/kategorije/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(k),
  });
  if (!res.ok) throw new Error("Napaka pri posodabljanju kategorije");
  return res.json();
};

export const deleteKategorija = async (id: number): Promise<void> => {
  const res = await fetch(`${BASE}/kategorije/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Napaka pri brisanju kategorije");
};