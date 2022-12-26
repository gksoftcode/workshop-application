import { IClient } from '@/shared/model/client.model';
import { IInvoiceDetails } from '@/shared/model/invoice-details.model';
import { IServices } from '@/shared/model/services.model';
import { IAttachments } from '@/shared/model/attachments.model';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';
export interface IInvoice {
  id?: number;
  invoiceDate?: Date | null;
  issueDate?: Date | null;
  paymentTerms?: number | null;
  discount?: number | null;
  notes?: number | null;
  discountType?: DiscountType | null;
  depositAmount?: number | null;
  isDepositPaied?: boolean | null;
  depositMethod?: PaymentMethod | null;
  depositPayRef?: string | null;
  isAlreadyPaied?: boolean | null;
  paymentMethod?: PaymentMethod | null;
  paymentRef?: string | null;
  amount?: number | null;
  lastAmount?: number | null;
  shippingFees?: number | null;
  client?: IClient | null;
  invoiceDetails?: IInvoiceDetails[] | null;
  services?: IServices[] | null;
  attachments?: IAttachments[] | null;
  workOrders?: IWorkOrders | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceDate?: Date | null,
    public issueDate?: Date | null,
    public paymentTerms?: number | null,
    public discount?: number | null,
    public notes?: number | null,
    public discountType?: DiscountType | null,
    public depositAmount?: number | null,
    public isDepositPaied?: boolean | null,
    public depositMethod?: PaymentMethod | null,
    public depositPayRef?: string | null,
    public isAlreadyPaied?: boolean | null,
    public paymentMethod?: PaymentMethod | null,
    public paymentRef?: string | null,
    public amount?: number | null,
    public lastAmount?: number | null,
    public shippingFees?: number | null,
    public client?: IClient | null,
    public invoiceDetails?: IInvoiceDetails[] | null,
    public services?: IServices[] | null,
    public attachments?: IAttachments[] | null,
    public workOrders?: IWorkOrders | null
  ) {
    this.isDepositPaied = this.isDepositPaied ?? false;
    this.isAlreadyPaied = this.isAlreadyPaied ?? false;
  }
}
