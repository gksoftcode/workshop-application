import { IInvoice } from '@/shared/model/invoice.model';

import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
export interface IInvoiceDetails {
  id?: number;
  itemNo?: number | null;
  itemDesc?: string | null;
  itemPrice?: number | null;
  itemQty?: number | null;
  discountType?: DiscountType | null;
  discount?: number | null;
  totalAmount?: number | null;
  invoice?: IInvoice | null;
}

export class InvoiceDetails implements IInvoiceDetails {
  constructor(
    public id?: number,
    public itemNo?: number | null,
    public itemDesc?: string | null,
    public itemPrice?: number | null,
    public itemQty?: number | null,
    public discountType?: DiscountType | null,
    public discount?: number | null,
    public totalAmount?: number | null,
    public invoice?: IInvoice | null
  ) {}
}
