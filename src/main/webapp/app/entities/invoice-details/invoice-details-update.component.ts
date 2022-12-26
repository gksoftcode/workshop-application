import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import InvoiceService from '@/entities/invoice/invoice.service';
import { IInvoice } from '@/shared/model/invoice.model';

import { IInvoiceDetails, InvoiceDetails } from '@/shared/model/invoice-details.model';
import InvoiceDetailsService from './invoice-details.service';
import { DiscountType } from '@/shared/model/enumerations/discount-type.model';

const validations: any = {
  invoiceDetails: {
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
export default class InvoiceDetailsUpdate extends Vue {
  @Inject('invoiceDetailsService') private invoiceDetailsService: () => InvoiceDetailsService;
  @Inject('alertService') private alertService: () => AlertService;

  public invoiceDetails: IInvoiceDetails = new InvoiceDetails();

  @Inject('invoiceService') private invoiceService: () => InvoiceService;

  public invoices: IInvoice[] = [];
  public discountTypeValues: string[] = Object.keys(DiscountType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.invoiceDetailsId) {
        vm.retrieveInvoiceDetails(to.params.invoiceDetailsId);
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
    if (this.invoiceDetails.id) {
      this.invoiceDetailsService()
        .update(this.invoiceDetails)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.invoiceDetails.updated', { param: param.id });
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
      this.invoiceDetailsService()
        .create(this.invoiceDetails)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.invoiceDetails.created', { param: param.id });
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

  public retrieveInvoiceDetails(invoiceDetailsId): void {
    this.invoiceDetailsService()
      .find(invoiceDetailsId)
      .then(res => {
        this.invoiceDetails = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.invoiceService()
      .retrieve()
      .then(res => {
        this.invoices = res.data;
      });
  }
}
