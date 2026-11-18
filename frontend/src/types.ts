export type Kategorija = {
  id: number;
  naziv: string;
  budzet: number | null;
};

export type Strosek = {
  id: number;
  opis: string;
  znesek: number;
  datum: string;
  kategorija: Kategorija | null;
};

export type StrosekInput = Omit<Strosek, "id">;

export type KategorijaInput = Omit<Kategorija, "id">;

export type Uporabnik = {
  id: number;
  ime: string;
  email: string;
  vloga: "UPORABNIK" | "ADMINISTRATOR";
};

export type PrijavaInput = {
  email: string;
  geslo: string;
};

export type RegistracijaInput = PrijavaInput & {
  ime: string;
};

export type PrijavaResponse = {
  token: string;
  uporabnik: Uporabnik;
};