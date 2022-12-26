/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import AttachmentNotesUpdateComponent from '@/entities/attachment-notes/attachment-notes-update.vue';
import AttachmentNotesClass from '@/entities/attachment-notes/attachment-notes-update.component';
import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';

import StatusService from '@/entities/status/status.service';

import ActionService from '@/entities/action/action.service';

import AttachmentsService from '@/entities/attachments/attachments.service';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
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
  describe('AttachmentNotes Management Update Component', () => {
    let wrapper: Wrapper<AttachmentNotesClass>;
    let comp: AttachmentNotesClass;
    let attachmentNotesServiceStub: SinonStubbedInstance<AttachmentNotesService>;

    beforeEach(() => {
      attachmentNotesServiceStub = sinon.createStubInstance<AttachmentNotesService>(AttachmentNotesService);

      wrapper = shallowMount<AttachmentNotesClass>(AttachmentNotesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          attachmentNotesService: () => attachmentNotesServiceStub,
          alertService: () => new AlertService(),

          statusService: () =>
            sinon.createStubInstance<StatusService>(StatusService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          actionService: () =>
            sinon.createStubInstance<ActionService>(ActionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          attachmentsService: () =>
            sinon.createStubInstance<AttachmentsService>(AttachmentsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          workOrdersService: () =>
            sinon.createStubInstance<WorkOrdersService>(WorkOrdersService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.attachmentNotes = entity;
        attachmentNotesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentNotesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.attachmentNotes = entity;
        attachmentNotesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(attachmentNotesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAttachmentNotes = { id: 123 };
        attachmentNotesServiceStub.find.resolves(foundAttachmentNotes);
        attachmentNotesServiceStub.retrieve.resolves([foundAttachmentNotes]);

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
