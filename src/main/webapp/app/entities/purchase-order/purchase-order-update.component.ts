import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import SupplierService from '@/entities/supplier/supplier.service';
import { ISupplier } from '@/shared/model/supplier.model';

import PurchaseOrderDetailsService from '@/entities/purchase-order-details/purchase-order-details.service';
import { IPurchaseOrderDetails } from '@/shared/model/purchase-order-details.model';

import ServicesService from '@/entities/services/services.service';
import { IServices } from '@/shared/model/services.model';

import AttachmentsService from '@/entities/attachments/attachments.service';
import { IAttachments } from '@/shared/model/attachments.model';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { IPurchaseOrder, PurchaseOrder } from '@/shared/model/purchase-order.model';
import PurchaseOrderService from './purchase-order.service';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';
import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';

const validations: any = {
  purchaseOrder: {
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
    paymentRef: {},
    amount: {},
    lastAmount: {},
    shippingFees: {},
  },
};

@Component({
  validations,
})
export default class PurchaseOrderUpdate extends Vue {
  @Inject('purchaseOrderService') private purchaseOrderService: () => PurchaseOrderService;
  @Inject('alertService') private alertService: () => AlertService;

  public purchaseOrder: IPurchaseOrder = new PurchaseOrder();

  @Inject('supplierService') private supplierService: () => SupplierService;

  public suppliers: ISupplier[] = [];

  @Inject('purchaseOrderDetailsService') private purchaseOrderDetailsService: () => PurchaseOrderDetailsService;

  public purchaseOrderDetails: IPurchaseOrderDetails[] = [];

  @Inject('servicesService') private servicesService: () => ServicesService;

  public services: IServices[] = [];

  @Inject('attachmentsService') private attachmentsService: () => AttachmentsService;

  public attachments: IAttachments[] = [];

  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;

  public workOrders: IWorkOrders[] = [];
  public discountTypeValues: string[] = Object.keys(DiscountType);
  public paymentMethodValues: string[] = Object.keys(PaymentMethod);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.purchaseOrderId) {
        vm.retrievePurchaseOrder(to.params.purchaseOrderId);
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
    this.purchaseOrder.attachments = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.purchaseOrder.id) {
      this.purchaseOrderService()
        .update(this.purchaseOrder)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.purchaseOrder.updated', { param: param.id });
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
      this.purchaseOrderService()
        .create(this.purchaseOrder)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.purchaseOrder.created', { param: param.id });
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
      this.purchaseOrder[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.purchaseOrder[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.purchaseOrder[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.purchaseOrder[field] = null;
    }
  }

  public retrievePurchaseOrder(purchaseOrderId): void {
    this.purchaseOrderService()
      .find(purchaseOrderId)
      .then(res => {
        res.invoiceDate = new Date(res.invoiceDate);
        res.issueDate = new Date(res.issueDate);
        this.purchaseOrder = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.supplierService()
      .retrieve()
      .then(res => {
        this.suppliers = res.data;
      });
    this.purchaseOrderDetailsService()
      .retrieve()
      .then(res => {
        this.purchaseOrderDetails = res.data;
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
