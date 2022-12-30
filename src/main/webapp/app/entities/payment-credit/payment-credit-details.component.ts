import { Component, Vue, Inject } from 'vue-property-decorator';

import { IPaymentCredit } from '@/shared/model/payment-credit.model';
import PaymentCreditService from './payment-credit.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class PaymentCreditDetails extends Vue {
  @Inject('paymentCreditService') private paymentCreditService: () => PaymentCreditService;
  @Inject('alertService') private alertService: () => AlertService;

  public paymentCredit: IPaymentCredit = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paymentCreditId) {
        vm.retrievePaymentCredit(to.params.paymentCreditId);
      }
    });
  }

  public retrievePaymentCredit(paymentCreditId) {
    this.paymentCreditService()
      .find(paymentCreditId)
      .then(res => {
        this.paymentCredit = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
