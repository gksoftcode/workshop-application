import { Component, Inject } from 'vue-property-decorator';

import { mixins } from 'vue-class-component';
import JhiDataUtils from '@/shared/data/data-utils.service';

import { IAttachments } from '@/shared/model/attachments.model';
import AttachmentsService from './attachments.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AttachmentsDetails extends mixins(JhiDataUtils) {
  @Inject('attachmentsService') private attachmentsService: () => AttachmentsService;
  @Inject('alertService') private alertService: () => AlertService;

  public attachments: IAttachments = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachmentsId) {
        vm.retrieveAttachments(to.params.attachmentsId);
      }
    });
  }

  public retrieveAttachments(attachmentsId) {
    this.attachmentsService()
      .find(attachmentsId)
      .then(res => {
        this.attachments = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
