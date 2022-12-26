/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AttachmentsUpdateComponent from '@/entities/attachments/attachments-update.vue';
import AttachmentsClass from '@/entities/attachments/attachments-update.component';
import AttachmentsService from '@/entities/attachments/attachments.service';

import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';

import InvoiceService from '@/entities/invoice/invoice.service';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Attachments Management Update Component', () => {
    let wrapper: Wrapper<AttachmentsClass>;
    let comp: AttachmentsClass;
    let attachmentsServiceStub: SinonStubbedInstance<AttachmentsService>;

    beforeEach(() => {
      attachmentsServiceStub = sinon.createStubInstance<AttachmentsService>(AttachmentsService);

      wrapper = shallowMount<AttachmentsClass>(AttachmentsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          attachmentsService: () => attachmentsServiceStub,
          alertService: () => new AlertService(),

          attachmentNotesService: () =>
            sinon.createStubInstance<AttachmentNotesService>(AttachmentNotesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          invoiceService: () =>
            sinon.createStubInstance<InvoiceService>(InvoiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          purchaseOrderService: () =>
            sinon.createStubInstance<PurchaseOrderService>(PurchaseOrderService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.attachments = entity;
        attachmentsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.attachments = entity;
        attachmentsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachments = { id: 123 };
        attachmentsServiceStub.find.resolves(foundAttachments);
        attachmentsServiceStub.retrieve.resolves([foundAttachments]);

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
