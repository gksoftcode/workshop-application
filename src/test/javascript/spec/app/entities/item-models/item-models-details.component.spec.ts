/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ItemModelsDetailComponent from '@/entities/item-models/item-models-details.vue';
import ItemModelsClass from '@/entities/item-models/item-models-details.component';
import ItemModelsService from '@/entities/item-models/item-models.service';
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
  describe('ItemModels Management Detail Component', () => {
    let wrapper: Wrapper<ItemModelsClass>;
    let comp: ItemModelsClass;
    let itemModelsServiceStub: SinonStubbedInstance<ItemModelsService>;

    beforeEach(() => {
      itemModelsServiceStub = sinon.createStubInstance<ItemModelsService>(ItemModelsService);

      wrapper = shallowMount<ItemModelsClass>(ItemModelsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { itemModelsService: () => itemModelsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundItemModels = { id: 123 };
        itemModelsServiceStub.find.resolves(foundItemModels);

        // WHEN
        comp.retrieveItemModels(123);
        await comp.$nextTick();

        // THEN
        expect(comp.itemModels).toBe(foundItemModels);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundItemModels = { id: 123 };
        itemModelsServiceStub.find.resolves(foundItemModels);

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
