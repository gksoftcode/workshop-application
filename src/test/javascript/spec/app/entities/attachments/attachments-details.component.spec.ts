/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AttachmentsDetailComponent from '@/entities/attachments/attachments-details.vue';
import AttachmentsClass from '@/entities/attachments/attachments-details.component';
import AttachmentsService from '@/entities/attachments/attachments.service';
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
  describe('Attachments Management Detail Component', () => {
    let wrapper: Wrapper<AttachmentsClass>;
    let comp: AttachmentsClass;
    let attachmentsServiceStub: SinonStubbedInstance<AttachmentsService>;

    beforeEach(() => {
      attachmentsServiceStub = sinon.createStubInstance<AttachmentsService>(AttachmentsService);

      wrapper = shallowMount<AttachmentsClass>(AttachmentsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { attachmentsService: () => attachmentsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAttachments = { id: 123 };
        attachmentsServiceStub.find.resolves(foundAttachments);

        // WHEN
        comp.retrieveAttachments(123);
        await comp.$nextTick();

        // THEN
        expect(comp.attachments).toBe(foundAttachments);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachments = { id: 123 };
        attachmentsServiceStub.find.resolves(foundAttachments);

        // WHEN
        comp.beforeRouteEnter({ params: { attachmentsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.attachments).toBe(foundAttachments);
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
