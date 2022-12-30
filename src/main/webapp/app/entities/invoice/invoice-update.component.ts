import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import { IClient } from '@/shared/model/client.model';

import InvoiceDetailsService from '@/entities/invoice-details/invoice-details.service';
import { IInvoiceDetails } from '@/shared/model/invoice-details.model';

import ServicesService from '@/entities/services/services.service';
import { IServices } from '@/shared/model/services.model';

import AttachmentsService from '@/entities/attachments/attachments.service';
import { IAttachments } from '@/shared/model/attachments.model';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { IInvoice, Invoice } from '@/shared/model/invoice.model';
import InvoiceService from './invoice.service';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from '@/shared/model/enumerations/payment-status.model';

const validations: any = {
  invoice: {
    invoiceDate: {},
    issueDate: {},
    paymentTerms: {},
    discount: {},
    notes: {},
    discountType: {},
    depositAmount: {},
    isDepositPaied: {},
    depositMethod: {},
    depositPayRef: {},
    isAlreadyPaied: {},
    paymentMethod: {},
    paymentStatus: {},
    paymentRef: {},
    amount: {},
    lastAmount: {},
    shippingFees: {},
  },
};

@Component({
  validations,
})
export default class InvoiceUpdate extends Vue {
  @Inject('invoiceService') private invoiceService: () => InvoiceService;
  @Inject('alertService') private alertService: () => AlertService;

  public invoice: IInvoice = new Invoice();

  @Inject('clientService') private clientService: () => ClientService;

  public clients: IClient[] = [];

  @Inject('invoiceDetailsService') private invoiceDetailsService: () => InvoiceDetailsService;

  public invoiceDetails: IInvoiceDetails[] = [];

  @Inject('servicesService') private servicesService: () => ServicesService;

  public services: IServices[] = [];

  @Inject('attachmentsService') private attachmentsService: () => AttachmentsService;

  public attachments: IAttachments[] = [];

  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;

  public workOrders: IWorkOrders[] = [];
  public discountTypeValues: string[] = Object.keys(DiscountType);
  public paymentMethodValues: string[] = Object.keys(PaymentMethod);
  public paymentStatusValues: string[] = Object.keys(PaymentStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.invoiceId) {
        vm.retrieveInvoice(to.params.invoiceId);
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
    this.invoice.attachments = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.invoice.id) {
      this.invoiceService()
        .update(this.invoice)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.invoice.updated', { param: param.id });
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
      this.invoiceService()
        .create(this.invoice)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.invoice.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.invoice[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.invoice[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.invoice[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.invoice[field] = null;
    }
  }

  public retrieveInvoice(invoiceId): void {
    this.invoiceService()
      .find(invoiceId)
      .then(res => {
        res.invoiceDate = new Date(res.invoiceDate);
        res.issueDate = new Date(res.issueDate);
        this.invoice = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.clientService()
      .retrieve()
      .then(res => {
        this.clients = res.data;
      });
    this.invoiceDetailsService()
      .retrieve()
      .then(res => {
        this.invoiceDetails = res.data;
      });
    this.servicesService()
      .retrieve()
      .then(res => {
        this.services = res.data;
      });
    this.attachmentsService()
      .retrieve()
      .then(res => {
        this.attachments = res.data;
      });
    this.workOrdersService()
      .retrieve()
      .then(res => {
        this.workOrders = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
