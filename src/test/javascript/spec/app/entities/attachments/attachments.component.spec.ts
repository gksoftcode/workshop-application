/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import AttachmentsComponent from '@/entities/attachments/attachments.vue';
import AttachmentsClass from '@/entities/attachments/attachments.component';
import AttachmentsService from '@/entities/attachments/attachments.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.component('jhi-sort-indicator', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Attachments Management Component', () => {
    let wrapper: Wrapper<AttachmentsClass>;
    let comp: AttachmentsClass;
    let attachmentsServiceStub: SinonStubbedInstance<AttachmentsService>;

    beforeEach(() => {
      attachmentsServiceStub = sinon.createStubInstance<AttachmentsService>(AttachmentsService);
      attachmentsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<AttachmentsClass>(AttachmentsComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          attachmentsService: () => attachmentsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      attachmentsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllAttachmentss();
      await comp.$nextTick();

      // THEN
      expect(attachmentsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.attachments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      attachmentsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(attachmentsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.attachments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      attachmentsServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(attachmentsServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      attachmentsServiceStub.retrieve.reset();
      attachmentsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(attachmentsServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.attachments[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,asc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // GIVEN
      comp.propOrder = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,asc', 'id']);
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      attachmentsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(attachmentsServiceStub.retrieve.callCount).toEqual(1);

      comp.removeAttachments();
      await comp.$nextTick();

      // THEN
      expect(attachmentsServiceStub.delete.called).toBeTruthy();
      expect(attachmentsServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
