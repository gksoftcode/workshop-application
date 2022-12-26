/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import WorkOrdersDetailComponent from '@/entities/work-orders/work-orders-details.vue';
import WorkOrdersClass from '@/entities/work-orders/work-orders-details.component';
import WorkOrdersService from '@/entities/work-orders/work-orders.service';
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
  describe('WorkOrders Management Detail Component', () => {
    let wrapper: Wrapper<WorkOrdersClass>;
    let comp: WorkOrdersClass;
    let workOrdersServiceStub: SinonStubbedInstance<WorkOrdersService>;

    beforeEach(() => {
      workOrdersServiceStub = sinon.createStubInstance<WorkOrdersService>(WorkOrdersService);

      wrapper = shallowMount<WorkOrdersClass>(WorkOrdersDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { workOrdersService: () => workOrdersServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundWorkOrders = { id: 123 };
        workOrdersServiceStub.find.resolves(foundWorkOrders);

        // WHEN
        comp.retrieveWorkOrders(123);
        await comp.$nextTick();

        // THEN
        expect(comp.workOrders).toBe(foundWorkOrders);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundWorkOrders = { id: 123 };
        workOrdersServiceStub.find.resolves(foundWorkOrders);

        // WHEN
        comp.beforeRouteEnter({ params: { workOrdersId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.workOrders).toBe(foundWorkOrders);
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
