import { Component, Vue, Inject } from 'vue-property-decorator';

import { IInvoiceDetails } from '@/shared/model/invoice-details.model';
import InvoiceDetailsService from './invoice-details.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class InvoiceDetailsDetails extends Vue {
  @Inject('invoiceDetailsService') private invoiceDetailsService: () => InvoiceDetailsService;
  @Inject('alertService') private alertService: () => AlertService;

  public invoiceDetails: IInvoiceDetails = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.invoiceDetailsId) {
        vm.retrieveInvoiceDetails(to.params.invoiceDetailsId);
      }
    });
  }

  public retrieveInvoiceDetails(invoiceDetailsId) {
    this.invoiceDetailsService()
      .find(invoiceDetailsId)
      .then(res => {
        this.invoiceDetails = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
