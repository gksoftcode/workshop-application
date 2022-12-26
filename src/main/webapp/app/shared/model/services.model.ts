import { ITax } from '@/shared/model/tax.model';
import { IServiceCategory } from '@/shared/model/service-category.model';
import { IInvoice } from '@/shared/model/invoice.model';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
export interface IServices {
  id?: number;
  name?: string | null;
  description?: string | null;
  cost?: number | null;
  price?: number | null;
  discount?: number | null;
  notes?: number | null;
  discountType?: DiscountType | null;
  tax?: ITax | null;
  serviceCategory?: IServiceCategory | null;
  invoice?: IInvoice | null;
  purchaseOrder?: IPurchaseOrder | null;
}

export class Services implements IServices {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public cost?: number | null,
    public price?: number | null,
    public discount?: number | null,
    public notes?: number | null,
    public discountType?: DiscountType | null,
    public tax?: ITax | null,
    public serviceCategory?: IServiceCategory | null,
    public invoice?: IInvoice | null,
    public purchaseOrder?: IPurchaseOrder | null
  ) {}
}
