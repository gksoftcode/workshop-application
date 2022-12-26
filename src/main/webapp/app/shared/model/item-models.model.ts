export interface IItemModels {
  id?: number;
  name?: string | null;
}

export class ItemModels implements IItemModels {
  constructor(public id?: number, public name?: string | null) {}
}
