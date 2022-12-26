import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import LocationService from '@/entities/location/location.service';
import { ILocation } from '@/shared/model/location.model';

import { ISupplier, Supplier } from '@/shared/model/supplier.model';
import SupplierService from './supplier.service';

const validations: any = {
  supplier: {
    name: {},
    email: {},
    phone: {},
    mobilePone: {},
  },
};

@Component({
  validations,
})
export default class SupplierUpdate extends Vue {
  @Inject('supplierService') private supplierService: () => SupplierService;
  @Inject('alertService') private alertService: () => AlertService;

  public supplier: ISupplier = new Supplier();

  @Inject('locationService') private locationService: () => LocationService;

  public locations: ILocation[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.supplierId) {
        vm.retrieveSupplier(to.params.supplierId);
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
    this.supplier.locations = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.supplier.id) {
      this.supplierService()
        .update(this.supplier)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.supplier.updated', { param: param.id });
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
      this.supplierService()
        .create(this.supplier)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.supplier.created', { param: param.id });
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

  public retrieveSupplier(supplierId): void {
    this.supplierService()
      .find(supplierId)
      .then(res => {
        this.supplier = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
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
