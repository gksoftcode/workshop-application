import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPurchaseOrderDetails } from '@/shared/model/purchase-order-details.model';
import PurchaseOrderDetailsService from './purchase-order-details.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PurchaseOrderDetailsDetails extends Vue {
  @Inject('purchaseOrderDetailsService') private purchaseOrderDetailsService: () => PurchaseOrderDetailsService;
  @Inject('alertService') private alertService: () => AlertService;

  public purchaseOrderDetails: IPurchaseOrderDetails = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.purchaseOrderDetailsId) {
        vm.retrievePurchaseOrderDetails(to.params.purchaseOrderDetailsId);
      }
    });
  }

  public retrievePurchaseOrderDetails(purchaseOrderDetailsId) {
    this.purchaseOrderDetailsService()
      .find(purchaseOrderDetailsId)
      .then(res => {
        this.purchaseOrderDetails = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
