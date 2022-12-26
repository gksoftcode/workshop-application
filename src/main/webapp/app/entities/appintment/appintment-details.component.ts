import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAppintment } from '@/shared/model/appintment.model';
import AppintmentService from './appintment.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AppintmentDetails extends Vue {
  @Inject('appintmentService') private appintmentService: () => AppintmentService;
  @Inject('alertService') private alertService: () => AlertService;

  public appintment: IAppintment = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.appintmentId) {
        vm.retrieveAppintment(to.params.appintmentId);
      }
    });
  }

  public retrieveAppintment(appintmentId) {
    this.appintmentService()
      .find(appintmentId)
      .then(res => {
        this.appintment = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
