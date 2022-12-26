import { ITax } from '@/shared/model/tax.model';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
export interface IPurchaseOrderDetails {
  id?: number;
  itemNo?: number | null;
  itemDesc?: string | null;
  itemPrice?: number | null;
  itemQty?: number | null;
  discountType?: DiscountType | null;
  discount?: number | null;
  totalAmount?: number | null;
  tax?: ITax | null;
  purchaseOrder?: IPurchaseOrder | null;
}

export class PurchaseOrderDetails implements IPurchaseOrderDetails {
  constructor(
    public id?: number,
    public itemNo?: number | null,
    public itemDesc?: string | null,
    public itemPrice?: number | null,
    public itemQty?: number | null,
    public discountType?: DiscountType | null,
    public discount?: number | null,
    public totalAmount?: number | null,
    public tax?: ITax | null,
    public purchaseOrder?: IPurchaseOrder | null
  ) {}
}
