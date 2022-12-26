import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IServiceCategory, ServiceCategory } from '@/shared/model/service-category.model';
import ServiceCategoryService from './service-category.service';

const validations: any = {
  serviceCategory: {
    categoryName: {},
  },
};

@Component({
  validations,
})
export default class ServiceCategoryUpdate extends Vue {
  @Inject('serviceCategoryService') private serviceCategoryService: () => ServiceCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceCategory: IServiceCategory = new ServiceCategory();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceCategoryId) {
        vm.retrieveServiceCategory(to.params.serviceCategoryId);
      }
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
    if (this.serviceCategory.id) {
      this.serviceCategoryService()
        .update(this.serviceCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.serviceCategory.updated', { param: param.id });
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
      this.serviceCategoryService()
        .create(this.serviceCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.serviceCategory.created', { param: param.id });
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

  public retrieveServiceCategory(serviceCategoryId): void {
    this.serviceCategoryService()
      .find(serviceCategoryId)
      .then(res => {
        this.serviceCategory = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
