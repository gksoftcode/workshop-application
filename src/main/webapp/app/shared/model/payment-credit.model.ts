import { IEmployee } from '@/shared/model/employee.model';
import { IAttachments } from '@/shared/model/attachments.model';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from '@/shared/model/enumerations/payment-status.model';
export interface IPaymentCredit {
  id?: number;
  paymentMethod?: PaymentMethod | null;
  paymentRef?: string | null;
  amount?: number | null;
  addDate?: Date | null;
  paymentStatus?: PaymentStatus | null;
  paymentDetails?: string | null;
  receiptNotes?: string | null;
  collectedBies?: IEmployee[] | null;
  attachments?: IAttachments[] | null;
  workOrders?: IWorkOrders | null;
}

export class PaymentCredit implements IPaymentCredit {
  constructor(
    public id?: number,
    public paymentMethod?: PaymentMethod | null,
    public paymentRef?: string | null,
    public amount?: number | null,
    public addDate?: Date | null,
    public paymentStatus?: PaymentStatus | null,
    public paymentDetails?: string | null,
    public receiptNotes?: string | null,
    public collectedBies?: IEmployee[] | null,
    public attachments?: IAttachments[] | null,
    public workOrders?: IWorkOrders | null
  ) {}
}
