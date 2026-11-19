import type {
  Kategorija,
  KategorijaInput,
  MesecniBudzet,
  Opozorilo,
  PrijavaInput,
  PrijavaResponse,
  PregledPorabeOdgovor,
  RegistracijaInput,
  Strosek,
  StrosekInput,
  Uporabnik,
} from "./types";

const BASE = "http://localhost:8100/api/v1";
const TOKEN_KEY = "strosko_token";


export const getToken = (): string | null => {
  try {
    return localStorage.getItem(TOKEN_KEY);
  } catch {
    return null;
  }
};

const setToken = (token: string) => {
  try {
    localStorage.setItem(TOKEN_KEY, token);
  } catch {
  }
};

export const clearToken = () => {
  try {
    localStorage.removeItem(TOKEN_KEY);
  } catch {
  }
};

let onUnauthorized: () => void = () => {};
export const setOnUnauthorized = (fn: () => void) => {
  onUnauthorized = fn;
};


const request = async <T>(path: string, napaka: string, init: RequestInit = {}): Promise<T> => {
  const headers = new Headers(init.headers);
  if (init.body) headers.set("Content-Type", "application/json");
  const token = getToken();
  if (token) headers.set("Authorization", `Bearer ${token}`);

  let res: Response;
  try {
    res = await fetch(`${BASE}${path}`, { ...init, headers });
  } catch {
    throw new Error("Strežnik trenutno ni dosegljiv. Poskusite znova.");
  }

  if (res.status === 401 && token) {
    clearToken();
    onUnauthorized();
  }
  if (!res.ok) {
    const body = await res.json().catch(() => null);
    throw new Error(body?.message || napaka);
  }
  if (res.status === 204) return undefined as T;
  return res.json();
};

const json = (method: string, data: unknown): RequestInit => ({
  method,
  body: JSON.stringify(data),
});


export const registracija = async (r: RegistracijaInput): Promise<Uporabnik> => {
  const odg = await request<PrijavaResponse>("/auth/registracija", "Registracija trenutno ni mogoča. Poskusite znova.", json("POST", r));
  setToken(odg.token);
  return odg.uporabnik;
};

export const prijava = async (p: PrijavaInput): Promise<Uporabnik> => {
  const odg = await request<PrijavaResponse>("/auth/prijava", "Prijava trenutno ni mogoča. Poskusite znova.", json("POST", p));
  setToken(odg.token);
  return odg.uporabnik;
};

export const odjava = async (): Promise<void> => {
  try {
    await request<void>("/auth/odjava", "Napaka pri odjavi", { method: "POST" });
  } finally {
    clearToken();
  }
};

export const getJaz = (): Promise<Uporabnik> => request("/auth/jaz", "Napaka pri branju uporabnika");


export const getStroski = (): Promise<Strosek[]> => request("/stroski", "Napaka pri branju stroškov");

export const isciStroske = (q: string): Promise<Strosek[]> =>
  request(`/stroski/iskanje?q=${encodeURIComponent(q)}`, "Napaka pri iskanju");

export const getStrosek = (id: number): Promise<Strosek> => request(`/stroski/${id}`, "Strošek ne obstaja");

export const createStrosek = (s: StrosekInput): Promise<Strosek> =>
  request("/stroski", "Napaka pri dodajanju", json("POST", s));

export const updateStrosek = (id: number, s: StrosekInput): Promise<Strosek> =>
  request(`/stroski/${id}`, "Napaka pri posodabljanju", json("PUT", s));

export const deleteStrosek = (id: number): Promise<void> =>
  request(`/stroski/${id}`, "Napaka pri brisanju", { method: "DELETE" });


export const getKategorije = (): Promise<Kategorija[]> => request("/kategorije", "Napaka pri branju kategorij");

export const getKategorija = (id: number): Promise<Kategorija> => request(`/kategorije/${id}`, "Kategorija ne obstaja");

export const createKategorija = (k: KategorijaInput): Promise<Kategorija> =>
  request("/kategorije", "Napaka pri dodajanju kategorije", json("POST", k));

export const updateKategorija = (id: number, k: KategorijaInput): Promise<Kategorija> =>
  request(`/kategorije/${id}`, "Napaka pri posodabljanju kategorije", json("PUT", k));

export const deleteKategorija = (id: number): Promise<void> =>
  request(`/kategorije/${id}`, "Napaka pri brisanju kategorije", { method: "DELETE" });

export const getPregledPorabe = (mesec: string): Promise<PregledPorabeOdgovor> =>
  request(`/pregled?mesec=${encodeURIComponent(mesec)}`, "Napaka pri branju pregleda porabe");

export const getOpozorila = (): Promise<Opozorilo[]> => request("/opozorila", "Opozoril ni mogoče naložiti");

export const preveriOpozorila = (): Promise<void> =>
  request("/opozorila/preveri", "Preverjanje budgeta ni uspelo", { method: "POST" });

export const oznaciPrebrano = (id: number): Promise<void> =>
  request(`/opozorila/${id}/prebrano`, "Opozorila ni mogoče označiti kot prebranega", { method: "PUT" });

export const oznaciVsePrebrano = (): Promise<void> =>
  request("/opozorila/prebrano", "Opozoril ni mogoče označiti kot prebranih", { method: "PUT" });

export const getMesecniBudzet = (): Promise<MesecniBudzet> =>
  request("/mesecni-budzet", "Mesečnega budgeta ni mogoče naložiti");

export const setMesecniBudzet = (budzet: number | null): Promise<MesecniBudzet> =>
  request("/mesecni-budzet", "Napaka pri shranjevanju mesečnega budgeta", json("PUT", { budzet }));
main
