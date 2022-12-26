import { IAttachmentNotes } from '@/shared/model/attachment-notes.model';
import { IInvoice } from '@/shared/model/invoice.model';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

export interface IAttachments {
  id?: number;
  name?: string | null;
  attachContentType?: string | null;
  attach?: string | null;
  attachmentNotes?: IAttachmentNotes[] | null;
  invoices?: IInvoice[] | null;
  purchaseOrders?: IPurchaseOrder[] | null;
}

export class Attachments implements IAttachments {
  constructor(
    public id?: number,
    public name?: string | null,
    public attachContentType?: string | null,
    public attach?: string | null,
    public attachmentNotes?: IAttachmentNotes[] | null,
    public invoices?: IInvoice[] | null,
    public purchaseOrders?: IPurchaseOrder[] | null
  ) {}
}
