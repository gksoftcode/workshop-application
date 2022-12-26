/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import PurchaseOrderDetailsDetailComponent from '@/entities/purchase-order-details/purchase-order-details-details.vue';
import PurchaseOrderDetailsClass from '@/entities/purchase-order-details/purchase-order-details-details.component';
import PurchaseOrderDetailsService from '@/entities/purchase-order-details/purchase-order-details.service';
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
  describe('PurchaseOrderDetails Management Detail Component', () => {
    let wrapper: Wrapper<PurchaseOrderDetailsClass>;
    let comp: PurchaseOrderDetailsClass;
    let purchaseOrderDetailsServiceStub: SinonStubbedInstance<PurchaseOrderDetailsService>;

    beforeEach(() => {
      purchaseOrderDetailsServiceStub = sinon.createStubInstance<PurchaseOrderDetailsService>(PurchaseOrderDetailsService);

      wrapper = shallowMount<PurchaseOrderDetailsClass>(PurchaseOrderDetailsDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { purchaseOrderDetailsService: () => purchaseOrderDetailsServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundPurchaseOrderDetails = { id: 123 };
        purchaseOrderDetailsServiceStub.find.resolves(foundPurchaseOrderDetails);

        // WHEN
        comp.retrievePurchaseOrderDetails(123);
        await comp.$nextTick();

        // THEN
        expect(comp.purchaseOrderDetails).toBe(foundPurchaseOrderDetails);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPurchaseOrderDetails = { id: 123 };
        purchaseOrderDetailsServiceStub.find.resolves(foundPurchaseOrderDetails);

        // WHEN
        comp.beforeRouteEnter({ params: { purchaseOrderDetailsId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.purchaseOrderDetails).toBe(foundPurchaseOrderDetails);
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
