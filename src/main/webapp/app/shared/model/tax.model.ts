export interface ITax {
  id?: number;
  name?: string | null;
  taxValue?: number | null;
}

export class Tax implements ITax {
  constructor(public id?: number, public name?: string | null, public taxValue?: number | null) {}
}
