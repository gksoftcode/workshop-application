import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import AppintmentService from '@/entities/appintment/appintment.service';
import { IAppintment } from '@/shared/model/appintment.model';

import LocationService from '@/entities/location/location.service';
import { ILocation } from '@/shared/model/location.model';

import { IClient, Client } from '@/shared/model/client.model';
import ClientService from './client.service';

const validations: any = {
  client: {
    fullName: {},
    mobileNo: {},
    email: {},
    phoneNumber: {},
  },
};

@Component({
  validations,
})
export default class ClientUpdate extends Vue {
  @Inject('clientService') private clientService: () => ClientService;
  @Inject('alertService') private alertService: () => AlertService;

  public client: IClient = new Client();

  @Inject('appintmentService') private appintmentService: () => AppintmentService;

  public appintments: IAppintment[] = [];

  @Inject('locationService') private locationService: () => LocationService;

  public locations: ILocation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.clientId) {
        vm.retrieveClient(to.params.clientId);
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
    this.client.locations = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.client.id) {
      this.clientService()
        .update(this.client)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.client.updated', { param: param.id });
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
      this.clientService()
        .create(this.client)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.client.created', { param: param.id });
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

  public retrieveClient(clientId): void {
    this.clientService()
      .find(clientId)
      .then(res => {
        this.client = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.appintmentService()
      .retrieve()
      .then(res => {
        this.appintments = res.data;
      });
    this.locationService()
      .retrieve()
      .then(res => {
        this.locations = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      return selectedVals.find(value => option.id === value.id) ?? option;
    }
    return option;
  }
}
