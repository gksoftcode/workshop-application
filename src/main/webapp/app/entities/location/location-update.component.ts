import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import CountryService from '@/entities/country/country.service';
import { ICountry } from '@/shared/model/country.model';

import ClientService from '@/entities/client/client.service';
import { IClient } from '@/shared/model/client.model';

import SupplierService from '@/entities/supplier/supplier.service';
import { ISupplier } from '@/shared/model/supplier.model';

import { ILocation, Location } from '@/shared/model/location.model';
import LocationService from './location.service';

const validations: any = {
  location: {
    name: {},
    streetAddress: {},
    city: {},
    stateProvince: {},
    lang: {},
    lat: {},
  },
};

@Component({
  validations,
})
export default class LocationUpdate extends Vue {
  @Inject('locationService') private locationService: () => LocationService;
  @Inject('alertService') private alertService: () => AlertService;

  public location: ILocation = new Location();

  @Inject('countryService') private countryService: () => CountryService;

  public countries: ICountry[] = [];

  @Inject('clientService') private clientService: () => ClientService;

  public clients: IClient[] = [];

  @Inject('supplierService') private supplierService: () => SupplierService;

  public suppliers: ISupplier[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.locationId) {
        vm.retrieveLocation(to.params.locationId);
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
    if (this.location.id) {
      this.locationService()
        .update(this.location)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.location.updated', { param: param.id });
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
      this.locationService()
        .create(this.location)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.location.created', { param: param.id });
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

  public retrieveLocation(locationId): void {
    this.locationService()
      .find(locationId)
      .then(res => {
        this.location = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.countryService()
      .retrieve()
      .then(res => {
        this.countries = res.data;
      });
    this.clientService()
      .retrieve()
      .then(res => {
        this.clients = res.data;
      });
    this.supplierService()
      .retrieve()
      .then(res => {
        this.suppliers = res.data;
      });
  }
}
