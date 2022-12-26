/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ServicesDetailComponent from '@/entities/services/services-details.vue';
import ServicesClass from '@/entities/services/services-details.component';
import ServicesService from '@/entities/services/services.service';
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
  describe('Services Management Detail Component', () => {
    let wrapper: Wrapper<ServicesClass>;
    let comp: ServicesClass;
    let servicesServiceStub: SinonStubbedInstance<ServicesService>;

    beforeEach(() => {
      servicesServiceStub = sinon.createStubInstance<ServicesService>(ServicesService);

      wrapper = shallowMount<ServicesClass>(ServicesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { servicesService: () => servicesServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundServices = { id: 123 };
        servicesServiceStub.find.resolves(foundServices);

        // WHEN
        comp.retrieveServices(123);
        await comp.$nextTick();

        // THEN
        expect(comp.services).toBe(foundServices);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServices = { id: 123 };
        servicesServiceStub.find.resolves(foundServices);

        // WHEN
        comp.beforeRouteEnter({ params: { servicesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.services).toBe(foundServices);
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
