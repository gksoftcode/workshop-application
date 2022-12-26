/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ItemBrandUpdateComponent from '@/entities/item-brand/item-brand-update.vue';
import ItemBrandClass from '@/entities/item-brand/item-brand-update.component';
import ItemBrandService from '@/entities/item-brand/item-brand.service';

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
  describe('ItemBrand Management Update Component', () => {
    let wrapper: Wrapper<ItemBrandClass>;
    let comp: ItemBrandClass;
    let itemBrandServiceStub: SinonStubbedInstance<ItemBrandService>;

    beforeEach(() => {
      itemBrandServiceStub = sinon.createStubInstance<ItemBrandService>(ItemBrandService);

      wrapper = shallowMount<ItemBrandClass>(ItemBrandUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          itemBrandService: () => itemBrandServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.itemBrand = entity;
        itemBrandServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemBrandServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.itemBrand = entity;
        itemBrandServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(itemBrandServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundItemBrand = { id: 123 };
        itemBrandServiceStub.find.resolves(foundItemBrand);
        itemBrandServiceStub.retrieve.resolves([foundItemBrand]);

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
