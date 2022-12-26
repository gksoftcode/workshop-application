import { Component, Vue, Inject } from 'vue-property-decorator';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ClientService from '@/entities/client/client.service';
import { IClient } from '@/shared/model/client.model';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
import { IWorkOrders } from '@/shared/model/work-orders.model';

import { IAppintment, Appintment } from '@/shared/model/appintment.model';
import AppintmentService from './appintment.service';

const validations: any = {
  appintment: {
    appDate: {},
    notes: {},
  },
};

@Component({
  validations,
})
export default class AppintmentUpdate extends Vue {
  @Inject('appintmentService') private appintmentService: () => AppintmentService;
  @Inject('alertService') private alertService: () => AlertService;

  public appintment: IAppintment = new Appintment();

  @Inject('clientService') private clientService: () => ClientService;

  public clients: IClient[] = [];

  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;

  public workOrders: IWorkOrders[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.appintmentId) {
        vm.retrieveAppintment(to.params.appintmentId);
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
    if (this.appintment.id) {
      this.appintmentService()
        .update(this.appintment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.appintment.updated', { param: param.id });
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
      this.appintmentService()
        .create(this.appintment)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.appintment.created', { param: param.id });
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
      this.appintment[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.appintment[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.appintment[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.appintment[field] = null;
    }
  }

  public retrieveAppintment(appintmentId): void {
    this.appintmentService()
      .find(appintmentId)
      .then(res => {
        res.appDate = new Date(res.appDate);
        this.appintment = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.clientService()
      .retrieve()
      .then(res => {
        this.clients = res.data;
      });
    this.workOrdersService()
      .retrieve()
      .then(res => {
        this.workOrders = res.data;
      });
  }
}
