import { Component, Vue, Inject } from 'vue-property-decorator';

import { IItemBrand } from '@/shared/model/item-brand.model';
import ItemBrandService from './item-brand.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ItemBrandDetails extends Vue {
  @Inject('itemBrandService') private itemBrandService: () => ItemBrandService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemBrand: IItemBrand = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.itemBrandId) {
        vm.retrieveItemBrand(to.params.itemBrandId);
      }
    });
  }

  public retrieveItemBrand(itemBrandId) {
    this.itemBrandService()
      .find(itemBrandId)
      .then(res => {
        this.itemBrand = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
