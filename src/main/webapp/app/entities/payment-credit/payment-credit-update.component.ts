import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import EmployeeService from '@/entities/employee/employee.service';
import { IEmployee } from '@/shared/model/employee.model';

import AttachmentsService from '@/entities/attachments/attachments.service';
import { IAttachments } from '@/shared/model/attachments.model';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { IPaymentCredit, PaymentCredit } from '@/shared/model/payment-credit.model';
import PaymentCreditService from './payment-credit.service';
import { PaymentMethod } from '@/shared/model/enumerations/payment-method.model';
import { PaymentStatus } from '@/shared/model/enumerations/payment-status.model';

const validations: any = {
  paymentCredit: {
    paymentMethod: {},
    paymentRef: {},
    amount: {},
    addDate: {},
    paymentStatus: {},
    paymentDetails: {},
    receiptNotes: {},
  },
};

@Component({
  validations,
})
export default class PaymentCreditUpdate extends Vue {
  @Inject('paymentCreditService') private paymentCreditService: () => PaymentCreditService;
  @Inject('alertService') private alertService: () => AlertService;

  public paymentCredit: IPaymentCredit = new PaymentCredit();

  @Inject('employeeService') private employeeService: () => EmployeeService;

  public employees: IEmployee[] = [];

  @Inject('attachmentsService') private attachmentsService: () => AttachmentsService;

  public attachments: IAttachments[] = [];

  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;

  public workOrders: IWorkOrders[] = [];
  public paymentMethodValues: string[] = Object.keys(PaymentMethod);
  public paymentStatusValues: string[] = Object.keys(PaymentStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.paymentCreditId) {
        vm.retrievePaymentCredit(to.params.paymentCreditId);
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
    this.paymentCredit.attachments = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.paymentCredit.id) {
      this.paymentCreditService()
        .update(this.paymentCredit)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.paymentCredit.updated', { param: param.id });
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
      this.paymentCreditService()
        .create(this.paymentCredit)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.paymentCredit.created', { param: param.id });
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
      this.paymentCredit[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.paymentCredit[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.paymentCredit[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.paymentCredit[field] = null;
    }
  }

  public retrievePaymentCredit(paymentCreditId): void {
    this.paymentCreditService()
      .find(paymentCreditId)
      .then(res => {
        res.addDate = new Date(res.addDate);
        this.paymentCredit = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.employeeService()
      .retrieve()
      .then(res => {
        this.employees = res.data;
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
