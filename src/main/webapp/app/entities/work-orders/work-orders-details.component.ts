import { Component, Vue, Inject } from 'vue-property-decorator';

import { IWorkOrders } from '@/shared/model/work-orders.model';
import WorkOrdersService from './work-orders.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class WorkOrdersDetails extends Vue {
  @Inject('workOrdersService') private workOrdersService: () => WorkOrdersService;
  @Inject('alertService') private alertService: () => AlertService;

  public workOrders: IWorkOrders = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.workOrdersId) {
        vm.retrieveWorkOrders(to.params.workOrdersId);
      }
    });
  }

  public retrieveWorkOrders(workOrdersId) {
    this.workOrdersService()
      .find(workOrdersId)
      .then(res => {
        this.workOrders = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
