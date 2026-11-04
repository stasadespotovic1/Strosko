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