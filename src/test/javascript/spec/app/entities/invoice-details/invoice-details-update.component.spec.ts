/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import InvoiceDetailsUpdateComponent from '@/entities/invoice-details/invoice-details-update.vue';
import InvoiceDetailsClass from '@/entities/invoice-details/invoice-details-update.component';
import InvoiceDetailsService from '@/entities/invoice-details/invoice-details.service';

import InvoiceService from '@/entities/invoice/invoice.service';
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
  describe('InvoiceDetails Management Update Component', () => {
    let wrapper: Wrapper<InvoiceDetailsClass>;
    let comp: InvoiceDetailsClass;
    let invoiceDetailsServiceStub: SinonStubbedInstance<InvoiceDetailsService>;

    beforeEach(() => {
      invoiceDetailsServiceStub = sinon.createStubInstance<InvoiceDetailsService>(InvoiceDetailsService);

      wrapper = shallowMount<InvoiceDetailsClass>(InvoiceDetailsUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          invoiceDetailsService: () => invoiceDetailsServiceStub,
          alertService: () => new AlertService(),

          invoiceService: () =>
            sinon.createStubInstance<InvoiceService>(InvoiceService, {
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
        comp.invoiceDetails = entity;
        invoiceDetailsServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceDetailsServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.invoiceDetails = entity;
        invoiceDetailsServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceDetailsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundInvoiceDetails = { id: 123 };
        invoiceDetailsServiceStub.find.resolves(foundInvoiceDetails);
        invoiceDetailsServiceStub.retrieve.resolves([foundInvoiceDetails]);

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
