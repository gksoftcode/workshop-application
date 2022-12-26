export interface IItemBrand {
  id?: number;
  name?: string | null;
}

export class ItemBrand implements IItemBrand {
  constructor(public id?: number, public name?: string | null) {}
}
