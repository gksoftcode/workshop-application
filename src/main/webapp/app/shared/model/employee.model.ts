import { IJob } from '@/shared/model/job.model';
import { IDepartment } from '@/shared/model/department.model';
import { IWorkOrders } from '@/shared/model/work-orders.model';
import { IPaymentCredit } from '@/shared/model/payment-credit.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: Date | null;
  salary?: number | null;
  commissionPct?: number | null;
  jobs?: IJob[] | null;
  manager?: IEmployee | null;
  department?: IDepartment | null;
  workOrders?: IWorkOrders | null;
  paymentCredit?: IPaymentCredit | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public hireDate?: Date | null,
    public salary?: number | null,
    public commissionPct?: number | null,
    public jobs?: IJob[] | null,
    public manager?: IEmployee | null,
    public department?: IDepartment | null,
    public workOrders?: IWorkOrders | null,
    public paymentCredit?: IPaymentCredit | null
  ) {}
}
