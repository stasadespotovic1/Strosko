const valuta = new Intl.NumberFormat("sl-SI", { style: "currency", currency: "EUR" });
const datum = new Intl.DateTimeFormat("sl-SI", { day: "numeric", month: "numeric", year: "numeric" });

export const formatZnesek = (znesek: number): string => valuta.format(znesek);

export const formatDatum = (iso: string): string => (iso ? datum.format(new Date(iso)) : "-");
