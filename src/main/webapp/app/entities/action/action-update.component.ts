import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import { IAction, Action } from '@/shared/model/action.model';
import ActionService from './action.service';

const validations: any = {
  action: {
    name: {},
  },
};

@Component({
  validations,
})
export default class ActionUpdate extends Vue {
  @Inject('actionService') private actionService: () => ActionService;
  @Inject('alertService') private alertService: () => AlertService;

  public action: IAction = new Action();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.actionId) {
        vm.retrieveAction(to.params.actionId);
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
    if (this.action.id) {
      this.actionService()
        .update(this.action)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.action.updated', { param: param.id });
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
      this.actionService()
        .create(this.action)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('workshopApp.action.created', { param: param.id });
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

  public retrieveAction(actionId): void {
    this.actionService()
      .find(actionId)
      .then(res => {
        this.action = res;
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
