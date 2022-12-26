import { IClient } from '@/shared/model/client.model';
import { IWorkOrders } from '@/shared/model/work-orders.model';

export interface IAppintment {
  id?: number;
  appDate?: Date | null;
  notes?: string | null;
  client?: IClient | null;
  workOrders?: IWorkOrders | null;
}

export class Appintment implements IAppintment {
  constructor(
    public id?: number,
    public appDate?: Date | null,
    public notes?: string | null,
    public client?: IClient | null,
    public workOrders?: IWorkOrders | null
  ) {}
}
