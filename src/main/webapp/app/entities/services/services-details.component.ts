import { Component, Vue, Inject } from 'vue-property-decorator';

import { IServices } from '@/shared/model/services.model';
import ServicesService from './services.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ServicesDetails extends Vue {
  @Inject('servicesService') private servicesService: () => ServicesService;
  @Inject('alertService') private alertService: () => AlertService;

  public services: IServices = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.servicesId) {
        vm.retrieveServices(to.params.servicesId);
      }
    });
  }

  public retrieveServices(servicesId) {
    this.servicesService()
      .find(servicesId)
      .then(res => {
        this.services = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
