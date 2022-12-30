import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import AlertService from '@/shared/alert/alert.service';

import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';
import { IAttachmentNotes } from '@/shared/model/attachment-notes.model';

import InvoiceService from '@/entities/invoice/invoice.service';
import { IInvoice } from '@/shared/model/invoice.model';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import PaymentCreditService from '@/entities/payment-credit/payment-credit.service';
import { IPaymentCredit } from '@/shared/model/payment-credit.model';

import { IAttachments, Attachments } from '@/shared/model/attachments.model';
import AttachmentsService from './attachments.service';

const validations: any = {
  attachments: {
    name: {},
    attach: {},
  },
};

@Component({
  validations,
})
export default class AttachmentsUpdate extends mixins(JhiDataUtils) {
  @Inject('attachmentsService') private attachmentsService: () => AttachmentsService;
  @Inject('alertService') private alertService: () => AlertService;

  public attachments: IAttachments = new Attachments();

  @Inject('attachmentNotesService') private attachmentNotesService: () => AttachmentNotesService;

  public attachmentNotes: IAttachmentNotes[] = [];

  @Inject('invoiceService') private invoiceService: () => InvoiceService;

  public invoices: IInvoice[] = [];

  @Inject('purchaseOrderService') private purchaseOrderService: () => PurchaseOrderService;

  public purchaseOrders: IPurchaseOrder[] = [];

  @Inject('paymentCreditService') private paymentCreditService: () => PaymentCreditService;

  public paymentCredits: IPaymentCredit[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachmentsId) {
        vm.retrieveAttachments(to.params.attachmentsId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.attachments.id) {
      this.attachmentsService()
        .update(this.attachments)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.attachments.updated', { param: param.id });
          return (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.attachmentsService()
        .create(this.attachments)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.attachments.created', { param: param.id });
          (this.$root as any).$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveAttachments(attachmentsId): void {
    this.attachmentsService()
      .find(attachmentsId)
      .then(res => {
        this.attachments = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.attachmentNotesService()
      .retrieve()
      .then(res => {
        this.attachmentNotes = res.data;
      });
    this.invoiceService()
      .retrieve()
      .then(res => {
        this.invoices = res.data;
      });
    this.purchaseOrderService()
      .retrieve()
      .then(res => {
        this.purchaseOrders = res.data;
      });
    this.paymentCreditService()
      .retrieve()
      .then(res => {
        this.paymentCredits = res.data;
      });
  }
}
