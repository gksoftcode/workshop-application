/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import InvoiceDetailsDetailComponent from '@/entities/invoice-details/invoice-details-details.vue';
import InvoiceDetailsClass from '@/entities/invoice-details/invoice-details-details.component';
import InvoiceDetailsService from '@/entities/invoice-details/invoice-details.service';
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
  describe('InvoiceDetails Management Detail Component', () => {
    let wrapper: Wrapper<InvoiceDetailsClass>;
    let comp: InvoiceDetailsClass;
    let invoiceDetailsServiceStub: SinonStubbedInstance<InvoiceDetailsService>;

    beforeEach(() => {
      invoiceDetailsServiceStub = sinon.createStubInstance<InvoiceDetailsService>(InvoiceDetailsService);

      wrapper = shallowMount<InvoiceDetailsClass>(InvoiceDetailsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { invoiceDetailsService: () => invoiceDetailsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundInvoiceDetails = { id: 123 };
        invoiceDetailsServiceStub.find.resolves(foundInvoiceDetails);

        // WHEN
        comp.retrieveInvoiceDetails(123);
        await comp.$nextTick();

        // THEN
        expect(comp.invoiceDetails).toBe(foundInvoiceDetails);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundInvoiceDetails = { id: 123 };
        invoiceDetailsServiceStub.find.resolves(foundInvoiceDetails);

        // WHEN
        comp.beforeRouteEnter({ params: { invoiceDetailsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.invoiceDetails).toBe(foundInvoiceDetails);
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
