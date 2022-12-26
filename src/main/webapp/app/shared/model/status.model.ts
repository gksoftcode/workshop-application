export interface IStatus {
  id?: number;
  name?: string | null;
}

export class Status implements IStatus {
  constructor(public id?: number, public name?: string | null) {}
}
