import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IItemModels, ItemModels } from '@/shared/model/item-models.model';
import ItemModelsService from './item-models.service';

const validations: any = {
  itemModels: {
    name: {},
  },
};

@Component({
  validations,
})
export default class ItemModelsUpdate extends Vue {
  @Inject('itemModelsService') private itemModelsService: () => ItemModelsService;
  @Inject('alertService') private alertService: () => AlertService;

  public itemModels: IItemModels = new ItemModels();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.itemModelsId) {
        vm.retrieveItemModels(to.params.itemModelsId);
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
    if (this.itemModels.id) {
      this.itemModelsService()
        .update(this.itemModels)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.itemModels.updated', { param: param.id });
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
      this.itemModelsService()
        .create(this.itemModels)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.itemModels.created', { param: param.id });
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

  public retrieveItemModels(itemModelsId): void {
    this.itemModelsService()
      .find(itemModelsId)
      .then(res => {
        this.itemModels = res;
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
