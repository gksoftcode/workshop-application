import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import TaxService from '@/entities/tax/tax.service';
import { ITax } from '@/shared/model/tax.model';

import ServiceCategoryService from '@/entities/service-category/service-category.service';
import { IServiceCategory } from '@/shared/model/service-category.model';

import InvoiceService from '@/entities/invoice/invoice.service';
import { IInvoice } from '@/shared/model/invoice.model';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import { IServices, Services } from '@/shared/model/services.model';
import ServicesService from './services.service';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';

const validations: any = {
  services: {
    name: {},
    description: {},
    cost: {},
    price: {},
    discount: {},
    notes: {},
    discountType: {},
  },
};

@Component({
  validations,
})
export default class ServicesUpdate extends Vue {
  @Inject('servicesService') private servicesService: () => ServicesService;
  @Inject('alertService') private alertService: () => AlertService;

  public services: IServices = new Services();

  @Inject('taxService') private taxService: () => TaxService;

  public taxes: ITax[] = [];

  @Inject('serviceCategoryService') private serviceCategoryService: () => ServiceCategoryService;

  public serviceCategories: IServiceCategory[] = [];

  @Inject('invoiceService') private invoiceService: () => InvoiceService;

  public invoices: IInvoice[] = [];

  @Inject('purchaseOrderService') private purchaseOrderService: () => PurchaseOrderService;

  public purchaseOrders: IPurchaseOrder[] = [];
  public discountTypeValues: string[] = Object.keys(DiscountType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.servicesId) {
        vm.retrieveServices(to.params.servicesId);
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
    if (this.services.id) {
      this.servicesService()
        .update(this.services)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.services.updated', { param: param.id });
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
      this.servicesService()
        .create(this.services)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.services.created', { param: param.id });
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

  public retrieveServices(servicesId): void {
    this.servicesService()
      .find(servicesId)
      .then(res => {
        this.services = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.taxService()
      .retrieve()
      .then(res => {
        this.taxes = res.data;
      });
    this.serviceCategoryService()
      .retrieve()
      .then(res => {
        this.serviceCategories = res.data;
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
  }
}
