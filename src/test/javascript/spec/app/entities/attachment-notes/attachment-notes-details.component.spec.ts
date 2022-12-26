/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AttachmentNotesDetailComponent from '@/entities/attachment-notes/attachment-notes-details.vue';
import AttachmentNotesClass from '@/entities/attachment-notes/attachment-notes-details.component';
import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('AttachmentNotes Management Detail Component', () => {
    let wrapper: Wrapper<AttachmentNotesClass>;
    let comp: AttachmentNotesClass;
    let attachmentNotesServiceStub: SinonStubbedInstance<AttachmentNotesService>;

    beforeEach(() => {
      attachmentNotesServiceStub = sinon.createStubInstance<AttachmentNotesService>(AttachmentNotesService);

      wrapper = shallowMount<AttachmentNotesClass>(AttachmentNotesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { attachmentNotesService: () => attachmentNotesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAttachmentNotes = { id: 123 };
        attachmentNotesServiceStub.find.resolves(foundAttachmentNotes);

        // WHEN
        comp.retrieveAttachmentNotes(123);
        await comp.$nextTick();

        // THEN
        expect(comp.attachmentNotes).toBe(foundAttachmentNotes);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachmentNotes = { id: 123 };
        attachmentNotesServiceStub.find.resolves(foundAttachmentNotes);

        // WHEN
        comp.beforeRouteEnter({ params: { attachmentNotesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachmentNotes).toBe(foundAttachmentNotes);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
