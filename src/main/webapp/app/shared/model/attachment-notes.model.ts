import { IStatus } from '@/shared/model/status.model';
import { IAction } from '@/shared/model/action.model';
import { IAttachments } from '@/shared/model/attachments.model';
import { IWorkOrders } from '@/shared/model/work-orders.model';

export interface IAttachmentNotes {
  id?: number;
  isShared?: boolean | null;
  actionDate?: Date | null;
  note?: string | null;
  status?: IStatus | null;
  action?: IAction | null;
  attachments?: IAttachments[] | null;
  workOrders?: IWorkOrders | null;
}

export class AttachmentNotes implements IAttachmentNotes {
  constructor(
    public id?: number,
    public isShared?: boolean | null,
    public actionDate?: Date | null,
    public note?: string | null,
    public status?: IStatus | null,
    public action?: IAction | null,
    public attachments?: IAttachments[] | null,
    public workOrders?: IWorkOrders | null
  ) {
    this.isShared = this.isShared ?? false;
  }
}
