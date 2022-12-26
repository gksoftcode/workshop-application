import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IItemBrand, ItemBrand } from '@/shared/model/item-brand.model';
import ItemBrandService from './item-brand.service';

const validations: any = {
  itemBrand: {
    name: {},
  },
};

@Component({
  validations,
})
export default class ItemBrandUpdate extends Vue {
  @Inject('itemBrandService') private itemBrandService: () => ItemBrandService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemBrand: IItemBrand = new ItemBrand();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.itemBrandId) {
        vm.retrieveItemBrand(to.params.itemBrandId);
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
    if (this.itemBrand.id) {
      this.itemBrandService()
        .update(this.itemBrand)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.itemBrand.updated', { param: param.id });
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
      this.itemBrandService()
        .create(this.itemBrand)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.itemBrand.created', { param: param.id });
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

  public retrieveItemBrand(itemBrandId): void {
    this.itemBrandService()
      .find(itemBrandId)
      .then(res => {
        this.itemBrand = res;
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
