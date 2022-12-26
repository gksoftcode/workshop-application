/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import TaxDetailComponent from '@/entities/tax/tax-details.vue';
import TaxClass from '@/entities/tax/tax-details.component';
import TaxService from '@/entities/tax/tax.service';
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
  describe('Tax Management Detail Component', () => {
    let wrapper: Wrapper<TaxClass>;
    let comp: TaxClass;
    let taxServiceStub: SinonStubbedInstance<TaxService>;

    beforeEach(() => {
      taxServiceStub = sinon.createStubInstance<TaxService>(TaxService);

      wrapper = shallowMount<TaxClass>(TaxDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { taxService: () => taxServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundTax = { id: 123 };
        taxServiceStub.find.resolves(foundTax);

        // WHEN
        comp.retrieveTax(123);
        await comp.$nextTick();

        // THEN
        expect(comp.tax).toBe(foundTax);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundTax = { id: 123 };
        taxServiceStub.find.resolves(foundTax);

        // WHEN
        comp.beforeRouteEnter({ params: { taxId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.tax).toBe(foundTax);
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
