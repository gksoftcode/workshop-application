export interface IServiceCategory {
  id?: number;
  categoryName?: string | null;
}

export class ServiceCategory implements IServiceCategory {
  constructor(public id?: number, public categoryName?: string | null) {}
}
