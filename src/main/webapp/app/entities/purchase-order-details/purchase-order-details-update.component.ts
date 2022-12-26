import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import TaxService from '@/entities/tax/tax.service';
import { ITax } from '@/shared/model/tax.model';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import { IPurchaseOrder } from '@/shared/model/purchase-order.model';

import { IPurchaseOrderDetails, PurchaseOrderDetails } from '@/shared/model/purchase-order-details.model';
import PurchaseOrderDetailsService from './purchase-order-details.service';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';

const validations: any = {
  purchaseOrderDetails: {
    itemNo: {},
    itemDesc: {},
    itemPrice: {},
    itemQty: {},
    discountType: {},
    discount: {},
    totalAmount: {},
  },
};

@Component({
  validations,
})
export default class PurchaseOrderDetailsUpdate extends Vue {
  @Inject('purchaseOrderDetailsService') private purchaseOrderDetailsService: () => PurchaseOrderDetailsService;
  @Inject('alertService') private alertService: () => AlertService;

  public purchaseOrderDetails: IPurchaseOrderDetails = new PurchaseOrderDetails();

  @Inject('taxService') private taxService: () => TaxService;

  public taxes: ITax[] = [];

  @Inject('purchaseOrderService') private purchaseOrderService: () => PurchaseOrderService;

  public purchaseOrders: IPurchaseOrder[] = [];
  public discountTypeValues: string[] = Object.keys(DiscountType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.purchaseOrderDetailsId) {
        vm.retrievePurchaseOrderDetails(to.params.purchaseOrderDetailsId);
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
    if (this.purchaseOrderDetails.id) {
      this.purchaseOrderDetailsService()
        .update(this.purchaseOrderDetails)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.purchaseOrderDetails.updated', { param: param.id });
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
      this.purchaseOrderDetailsService()
        .create(this.purchaseOrderDetails)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.purchaseOrderDetails.created', { param: param.id });
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

  public retrievePurchaseOrderDetails(purchaseOrderDetailsId): void {
    this.purchaseOrderDetailsService()
      .find(purchaseOrderDetailsId)
      .then(res => {
        this.purchaseOrderDetails = res;
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
    this.purchaseOrderService()
      .retrieve()
      .then(res => {
        this.purchaseOrders = res.data;
      });
  }
}
