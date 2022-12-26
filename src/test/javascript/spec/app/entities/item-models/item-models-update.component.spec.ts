/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ItemModelsUpdateComponent from '@/entities/item-models/item-models-update.vue';
import ItemModelsClass from '@/entities/item-models/item-models-update.component';
import ItemModelsService from '@/entities/item-models/item-models.service';

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
  describe('ItemModels Management Update Component', () => {
    let wrapper: Wrapper<ItemModelsClass>;
    let comp: ItemModelsClass;
    let itemModelsServiceStub: SinonStubbedInstance<ItemModelsService>;

    beforeEach(() => {
      itemModelsServiceStub = sinon.createStubInstance<ItemModelsService>(ItemModelsService);

      wrapper = shallowMount<ItemModelsClass>(ItemModelsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          itemModelsService: () => itemModelsServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.itemModels = entity;
        itemModelsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemModelsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.itemModels = entity;
        itemModelsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemModelsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundItemModels = { id: 123 };
        itemModelsServiceStub.find.resolves(foundItemModels);
        itemModelsServiceStub.retrieve.resolves([foundItemModels]);

        // WHEN
        comp.beforeRouteEnter({ params: { itemModelsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.itemModels).toBe(foundItemModels);
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
