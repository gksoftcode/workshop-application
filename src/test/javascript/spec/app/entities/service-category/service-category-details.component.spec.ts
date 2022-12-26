/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ServiceCategoryDetailComponent from '@/entities/service-category/service-category-details.vue';
import ServiceCategoryClass from '@/entities/service-category/service-category-details.component';
import ServiceCategoryService from '@/entities/service-category/service-category.service';
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
  describe('ServiceCategory Management Detail Component', () => {
    let wrapper: Wrapper<ServiceCategoryClass>;
    let comp: ServiceCategoryClass;
    let serviceCategoryServiceStub: SinonStubbedInstance<ServiceCategoryService>;

    beforeEach(() => {
      serviceCategoryServiceStub = sinon.createStubInstance<ServiceCategoryService>(ServiceCategoryService);

      wrapper = shallowMount<ServiceCategoryClass>(ServiceCategoryDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { serviceCategoryService: () => serviceCategoryServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundServiceCategory = { id: 123 };
        serviceCategoryServiceStub.find.resolves(foundServiceCategory);

        // WHEN
        comp.retrieveServiceCategory(123);
        await comp.$nextTick();

        // THEN
        expect(comp.serviceCategory).toBe(foundServiceCategory);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServiceCategory = { id: 123 };
        serviceCategoryServiceStub.find.resolves(foundServiceCategory);

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
