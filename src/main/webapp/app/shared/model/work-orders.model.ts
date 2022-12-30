import { IStatus } from '@/shared/model/status.model';
import { IClient } from '@/shared/model/client.model';
import { IItemModels } from '@/shared/model/item-models.model';
import { IItemBrand } from '@/shared/model/item-brand.model';
import { IEmployee } from '@/shared/model/employee.model';
import { IAppintment } from '@/shared/model/appintment.model';
import { IAttachmentNotes } from '@/shared/model/attachment-notes.model';
import { IInvoice } from '@/shared/model/invoice.model';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';
import { IPaymentCredit } from '@/shared/model/payment-credit.model';

export interface IWorkOrders {
  id?: number;
  title?: string | null;
  description?: string | null;
  startDate?: Date | null;
  endDate?: Date | null;
  budget?: number | null;
  itemSerial?: string | null;
  isWaranty?: boolean | null;
  notes?: string | null;
  status?: IStatus | null;
  client?: IClient | null;
  itemModels?: IItemModels | null;
  itemBrand?: IItemBrand | null;
  assignedStaffs?: IEmployee[] | null;
  appintments?: IAppintment[] | null;
  attachmentNotes?: IAttachmentNotes[] | null;
  invoices?: IInvoice[] | null;
  purchaseOrders?: IPurchaseOrder[] | null;
  paymentCredits?: IPaymentCredit[] | null;
}

export class WorkOrders implements IWorkOrders {
  constructor(
    public id?: number,
    public title?: string | null,
    public description?: string | null,
    public startDate?: Date | null,
    public endDate?: Date | null,
    public budget?: number | null,
    public itemSerial?: string | null,
    public isWaranty?: boolean | null,
    public notes?: string | null,
    public status?: IStatus | null,
    public client?: IClient | null,
    public itemModels?: IItemModels | null,
    public itemBrand?: IItemBrand | null,
    public assignedStaffs?: IEmployee[] | null,
    public appintments?: IAppintment[] | null,
    public attachmentNotes?: IAttachmentNotes[] | null,
    public invoices?: IInvoice[] | null,
    public purchaseOrders?: IPurchaseOrder[] | null,
    public paymentCredits?: IPaymentCredit[] | null
  ) {
    this.isWaranty = this.isWaranty ?? false;
  }
}
