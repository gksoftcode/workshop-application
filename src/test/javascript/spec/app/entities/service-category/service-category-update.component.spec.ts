/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ServiceCategoryUpdateComponent from '@/entities/service-category/service-category-update.vue';
import ServiceCategoryClass from '@/entities/service-category/service-category-update.component';
import ServiceCategoryService from '@/entities/service-category/service-category.service';

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
  describe('ServiceCategory Management Update Component', () => {
    let wrapper: Wrapper<ServiceCategoryClass>;
    let comp: ServiceCategoryClass;
    let serviceCategoryServiceStub: SinonStubbedInstance<ServiceCategoryService>;

    beforeEach(() => {
      serviceCategoryServiceStub = sinon.createStubInstance<ServiceCategoryService>(ServiceCategoryService);

      wrapper = shallowMount<ServiceCategoryClass>(ServiceCategoryUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          serviceCategoryService: () => serviceCategoryServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.serviceCategory = entity;
        serviceCategoryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceCategoryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.serviceCategory = entity;
        serviceCategoryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(serviceCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceCategory = { id: 123 };
        serviceCategoryServiceStub.find.resolves(foundServiceCategory);
        serviceCategoryServiceStub.retrieve.resolves([foundServiceCategory]);

        // WHEN
        comp.beforeRouteEnter({ params: { serviceCategoryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.serviceCategory).toBe(foundServiceCategory);
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
