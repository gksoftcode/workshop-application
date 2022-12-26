import { ILocation } from '@/shared/model/location.model';

export interface ISupplier {
  id?: number;
  name?: string | null;
  email?: string | null;
  phone?: string | null;
  mobilePone?: string | null;
  locations?: ILocation[] | null;
}

export class Supplier implements ISupplier {
  constructor(
    public id?: number,
    public name?: string | null,
    public email?: string | null,
    public phone?: string | null,
    public mobilePone?: string | null,
    public locations?: ILocation[] | null
  ) {}
}
