/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PaymentCreditDetailComponent from '@/entities/payment-credit/payment-credit-details.vue';
import PaymentCreditClass from '@/entities/payment-credit/payment-credit-details.component';
import PaymentCreditService from '@/entities/payment-credit/payment-credit.service';
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
  describe('PaymentCredit Management Detail Component', () => {
    let wrapper: Wrapper<PaymentCreditClass>;
    let comp: PaymentCreditClass;
    let paymentCreditServiceStub: SinonStubbedInstance<PaymentCreditService>;

    beforeEach(() => {
      paymentCreditServiceStub = sinon.createStubInstance<PaymentCreditService>(PaymentCreditService);

      wrapper = shallowMount<PaymentCreditClass>(PaymentCreditDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { paymentCreditService: () => paymentCreditServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPaymentCredit = { id: 123 };
        paymentCreditServiceStub.find.resolves(foundPaymentCredit);

        // WHEN
        comp.retrievePaymentCredit(123);
        await comp.$nextTick();

        // THEN
        expect(comp.paymentCredit).toBe(foundPaymentCredit);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPaymentCredit = { id: 123 };
        paymentCreditServiceStub.find.resolves(foundPaymentCredit);

        // WHEN
        comp.beforeRouteEnter({ params: { paymentCreditId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.paymentCredit).toBe(foundPaymentCredit);
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
