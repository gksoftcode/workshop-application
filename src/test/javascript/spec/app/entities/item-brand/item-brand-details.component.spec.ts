/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ItemBrandDetailComponent from '@/entities/item-brand/item-brand-details.vue';
import ItemBrandClass from '@/entities/item-brand/item-brand-details.component';
import ItemBrandService from '@/entities/item-brand/item-brand.service';
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
  describe('ItemBrand Management Detail Component', () => {
    let wrapper: Wrapper<ItemBrandClass>;
    let comp: ItemBrandClass;
    let itemBrandServiceStub: SinonStubbedInstance<ItemBrandService>;

    beforeEach(() => {
      itemBrandServiceStub = sinon.createStubInstance<ItemBrandService>(ItemBrandService);

      wrapper = shallowMount<ItemBrandClass>(ItemBrandDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { itemBrandService: () => itemBrandServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundItemBrand = { id: 123 };
        itemBrandServiceStub.find.resolves(foundItemBrand);

        // WHEN
        comp.retrieveItemBrand(123);
        await comp.$nextTick();

        // THEN
        expect(comp.itemBrand).toBe(foundItemBrand);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundItemBrand = { id: 123 };
        itemBrandServiceStub.find.resolves(foundItemBrand);

        // WHEN
        comp.beforeRouteEnter({ params: { itemBrandId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.itemBrand).toBe(foundItemBrand);
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
