import { Component, Vue, Inject } from 'vue-property-decorator';

import { IAttachmentNotes } from '@/shared/model/attachment-notes.model';
import AttachmentNotesService from './attachment-notes.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class AttachmentNotesDetails extends Vue {
  @Inject('attachmentNotesService') private attachmentNotesService: () => AttachmentNotesService;
  @Inject('alertService') private alertService: () => AlertService;

  public attachmentNotes: IAttachmentNotes = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.attachmentNotesId) {
        vm.retrieveAttachmentNotes(to.params.attachmentNotesId);
      }
    });
  }

  public retrieveAttachmentNotes(attachmentNotesId) {
    this.attachmentNotesService()
      .find(attachmentNotesId)
      .then(res => {
        this.attachmentNotes = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
