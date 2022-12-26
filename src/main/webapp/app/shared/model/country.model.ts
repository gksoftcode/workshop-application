export interface ICountry {
  id?: number;
  name?: string | null;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string | null) {}
}
