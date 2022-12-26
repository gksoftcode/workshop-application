import { Component, Vue, Inject } from 'vue-property-decorator';

import { IStatus } from '@/shared/model/status.model';
import StatusService from './status.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class StatusDetails extends Vue {
  @Inject('statusService') private statusService: () => StatusService;
  @Inject('alertService') private alertService: () => AlertService;

  public status: IStatus = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.statusId) {
        vm.retrieveStatus(to.params.statusId);
      }
    });
  }

  public retrieveStatus(statusId) {
    this.statusService()
      .find(statusId)
      .then(res => {
        this.status = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
