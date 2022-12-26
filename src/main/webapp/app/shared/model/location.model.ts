import { ICountry } from '@/shared/model/country.model';
import { IClient } from '@/shared/model/client.model';
import { ISupplier } from '@/shared/model/supplier.model';

export interface ILocation {
  id?: number;
  name?: string | null;
  streetAddress?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  lang?: string | null;
  lat?: string | null;
  country?: ICountry | null;
  clients?: IClient[] | null;
  suppliers?: ISupplier[] | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public name?: string | null,
    public streetAddress?: string | null,
    public city?: string | null,
    public stateProvince?: string | null,
    public lang?: string | null,
    public lat?: string | null,
    public country?: ICountry | null,
    public clients?: IClient[] | null,
    public suppliers?: ISupplier[] | null
  ) {}
}
