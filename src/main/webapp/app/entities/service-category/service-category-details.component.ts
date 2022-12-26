import { Component, Vue, Inject } from 'vue-property-decorator';

import { IServiceCategory } from '@/shared/model/service-category.model';
import ServiceCategoryService from './service-category.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ServiceCategoryDetails extends Vue {
  @Inject('serviceCategoryService') private serviceCategoryService: () => ServiceCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public serviceCategory: IServiceCategory = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.serviceCategoryId) {
        vm.retrieveServiceCategory(to.params.serviceCategoryId);
      }
    });
  }

  public retrieveServiceCategory(serviceCategoryId) {
    this.serviceCategoryService()
      .find(serviceCategoryId)
      .then(res => {
        this.serviceCategory = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
