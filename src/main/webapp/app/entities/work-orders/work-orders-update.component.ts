import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import StatusService from '@/entities/status/status.service';
import { IStatus } from '@/shared/model/status.model';

import ClientService from '@/entities/client/client.service';
import { IClient } from '@/shared/model/client.model';

import ItemModelsService from '@/entities/item-models/item-models.service';
import { IItemModels } from '@/shared/model/item-models.model';

import ItemBrandService from '@/entities/item-brand/item-brand.service';
import { IItemBrand } from '@/shared/model/item-brand.model';

import EmployeeService from '@/entities/employee/employee.service';
import { IEmployee } from '@/shared/model/employee.model';

import AppintmentService from '@/entities/appintment/appintment.service';
import { IAppintment } from '@/shared/model/appintment.model';

import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';
import { IAttachmentNotes } from '@/shared/model/attachment-notes.model';

import InvoiceService from '@/entities/invoice/invoice.service';
import { IInvoice } from '@/shared/model/invoice.model';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import PaymentCreditService from '@/entities/payment-credit/payment-credit.service';
import { IPaymentCredit } from '@/shared/model/payment-credit.model';

import { IWorkOrders, WorkOrders } from '@/shared/model/work-orders.model';
import WorkOrdersService from './work-orders.service';

const validations: any = {
  workOrders: {
    title: {},
    description: {},
    startDate: {},
    endDate: {},
    budget: {},
    itemSerial: {},
    isWaranty: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class WorkOrdersUpdate extends Vue {
  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;
  @Inject('alertService') private alertService: () => AlertService;

  public workOrders: IWorkOrders = new WorkOrders();

  @Inject('statusService') private statusService: () => StatusService;

  public statuses: IStatus[] = [];

  @Inject('clientService') private clientService: () => ClientService;

  public clients: IClient[] = [];

  @Inject('itemModelsService') private itemModelsService: () => ItemModelsService;

  public itemModels: IItemModels[] = [];

  @Inject('itemBrandService') private itemBrandService: () => ItemBrandService;

  public itemBrands: IItemBrand[] = [];

  @Inject('employeeService') private employeeService: () => EmployeeService;

  public employees: IEmployee[] = [];

  @Inject('appintmentService') private appintmentService: () => AppintmentService;

  public appintments: IAppintment[] = [];

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
      if (to.params.workOrdersId) {
        vm.retrieveWorkOrders(to.params.workOrdersId);
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
    if (this.workOrders.id) {
      this.workOrdersService()
        .update(this.workOrders)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.workOrders.updated', { param: param.id });
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
      this.workOrdersService()
        .create(this.workOrders)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.workOrders.created', { param: param.id });
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
      this.workOrders[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.workOrders[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.workOrders[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.workOrders[field] = null;
    }
  }

  public retrieveWorkOrders(workOrdersId): void {
    this.workOrdersService()
      .find(workOrdersId)
      .then(res => {
        res.startDate = new Date(res.startDate);
        res.endDate = new Date(res.endDate);
        this.workOrders = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.statusService()
      .retrieve()
      .then(res => {
        this.statuses = res.data;
      });
    this.clientService()
      .retrieve()
      .then(res => {
        this.clients = res.data;
      });
    this.itemModelsService()
      .retrieve()
      .then(res => {
        this.itemModels = res.data;
      });
    this.itemBrandService()
      .retrieve()
      .then(res => {
        this.itemBrands = res.data;
      });
    this.employeeService()
      .retrieve()
      .then(res => {
        this.employees = res.data;
      });
    this.appintmentService()
      .retrieve()
      .then(res => {
        this.appintments = res.data;
      });
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
