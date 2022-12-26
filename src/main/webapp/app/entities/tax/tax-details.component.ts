import { Component, Vue, Inject } from 'vue-property-decorator';

import { ITax } from '@/shared/model/tax.model';
import TaxService from './tax.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class TaxDetails extends Vue {
  @Inject('taxService') private taxService: () => TaxService;
  @Inject('alertService') private alertService: () => AlertService;

  public tax: ITax = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.taxId) {
        vm.retrieveTax(to.params.taxId);
      }
    });
  }

  public retrieveTax(taxId) {
    this.taxService()
      .find(taxId)
      .then(res => {
        this.tax = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
