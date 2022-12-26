import { Component, Vue, Inject } from 'vue-property-decorator';

import { IItemModels } from '@/shared/model/item-models.model';
import ItemModelsService from './item-models.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ItemModelsDetails extends Vue {
  @Inject('itemModelsService') private itemModelsService: () => ItemModelsService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemModels: IItemModels = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.itemModelsId) {
        vm.retrieveItemModels(to.params.itemModelsId);
      }
    });
  }

  public retrieveItemModels(itemModelsId) {
    this.itemModelsService()
      .find(itemModelsId)
      .then(res => {
        this.itemModels = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
