/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ItemModelsComponent from '@/entities/item-models/item-models.vue';
import ItemModelsClass from '@/entities/item-models/item-models.component';
import ItemModelsService from '@/entities/item-models/item-models.service';
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
  describe('ItemModels Management Component', () => {
    let wrapper: Wrapper<ItemModelsClass>;
    let comp: ItemModelsClass;
    let itemModelsServiceStub: SinonStubbedInstance<ItemModelsService>;

    beforeEach(() => {
      itemModelsServiceStub = sinon.createStubInstance<ItemModelsService>(ItemModelsService);
      itemModelsServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ItemModelsClass>(ItemModelsComponent, {
        store,
        i18n,
        localVue,
        stubs: { jhiItemCount: true, bPagination: true, bModal: bModalStub as any },
        provide: {
          itemModelsService: () => itemModelsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      itemModelsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllItemModelss();
      await comp.$nextTick();

      // THEN
      expect(itemModelsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.itemModels[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should load a page', async () => {
      // GIVEN
      itemModelsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();

      // THEN
      expect(itemModelsServiceStub.retrieve.called).toBeTruthy();
      expect(comp.itemModels[0]).toEqual(expect.objectContaining({ id: 123 }));
    });

    it('should not load a page if the page is the same as the previous page', () => {
      // GIVEN
      itemModelsServiceStub.retrieve.reset();
      comp.previousPage = 1;

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(itemModelsServiceStub.retrieve.called).toBeFalsy();
    });

    it('should re-initialize the page', async () => {
      // GIVEN
      itemModelsServiceStub.retrieve.reset();
      itemModelsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.loadPage(2);
      await comp.$nextTick();
      comp.clear();
      await comp.$nextTick();

      // THEN
      expect(itemModelsServiceStub.retrieve.callCount).toEqual(3);
      expect(comp.page).toEqual(1);
      expect(comp.itemModels[0]).toEqual(expect.objectContaining({ id: 123 }));
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
      itemModelsServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(itemModelsServiceStub.retrieve.callCount).toEqual(1);

      comp.removeItemModels();
      await comp.$nextTick();

      // THEN
      expect(itemModelsServiceStub.delete.called).toBeTruthy();
      expect(itemModelsServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
