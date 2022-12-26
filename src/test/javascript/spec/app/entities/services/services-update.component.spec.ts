/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ServicesUpdateComponent from '@/entities/services/services-update.vue';
import ServicesClass from '@/entities/services/services-update.component';
import ServicesService from '@/entities/services/services.service';

import TaxService from '@/entities/tax/tax.service';

import ServiceCategoryService from '@/entities/service-category/service-category.service';

import InvoiceService from '@/entities/invoice/invoice.service';

import PurchaseOrderService from '@/entities/purchase-order/purchase-order.service';
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
  describe('Services Management Update Component', () => {
    let wrapper: Wrapper<ServicesClass>;
    let comp: ServicesClass;
    let servicesServiceStub: SinonStubbedInstance<ServicesService>;

    beforeEach(() => {
      servicesServiceStub = sinon.createStubInstance<ServicesService>(ServicesService);

      wrapper = shallowMount<ServicesClass>(ServicesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          servicesService: () => servicesServiceStub,
          alertService: () => new AlertService(),

          taxService: () =>
            sinon.createStubInstance<TaxService>(TaxService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          serviceCategoryService: () =>
            sinon.createStubInstance<ServiceCategoryService>(ServiceCategoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          invoiceService: () =>
            sinon.createStubInstance<InvoiceService>(InvoiceService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          purchaseOrderService: () =>
            sinon.createStubInstance<PurchaseOrderService>(PurchaseOrderService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.services = entity;
        servicesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(servicesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.services = entity;
        servicesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(servicesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundServices = { id: 123 };
        servicesServiceStub.find.resolves(foundServices);
        servicesServiceStub.retrieve.resolves([foundServices]);

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
