/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import InvoiceUpdateComponent from '@/entities/invoice/invoice-update.vue';
import InvoiceClass from '@/entities/invoice/invoice-update.component';
import InvoiceService from '@/entities/invoice/invoice.service';

import ClientService from '@/entities/client/client.service';

import InvoiceDetailsService from '@/entities/invoice-details/invoice-details.service';

import ServicesService from '@/entities/services/services.service';

import AttachmentsService from '@/entities/attachments/attachments.service';

import WorkOrdersService from '@/entities/work-orders/work-orders.service';
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
  describe('Invoice Management Update Component', () => {
    let wrapper: Wrapper<InvoiceClass>;
    let comp: InvoiceClass;
    let invoiceServiceStub: SinonStubbedInstance<InvoiceService>;

    beforeEach(() => {
      invoiceServiceStub = sinon.createStubInstance<InvoiceService>(InvoiceService);

      wrapper = shallowMount<InvoiceClass>(InvoiceUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          invoiceService: () => invoiceServiceStub,
          alertService: () => new AlertService(),

          clientService: () =>
            sinon.createStubInstance<ClientService>(ClientService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          invoiceDetailsService: () =>
            sinon.createStubInstance<InvoiceDetailsService>(InvoiceDetailsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          servicesService: () =>
            sinon.createStubInstance<ServicesService>(ServicesService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          attachmentsService: () =>
            sinon.createStubInstance<AttachmentsService>(AttachmentsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          workOrdersService: () =>
            sinon.createStubInstance<WorkOrdersService>(WorkOrdersService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.invoice = entity;
        invoiceServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.invoice = entity;
        invoiceServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(invoiceServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundInvoice = { id: 123 };
        invoiceServiceStub.find.resolves(foundInvoice);
        invoiceServiceStub.retrieve.resolves([foundInvoice]);

        // WHEN
        comp.beforeRouteEnter({ params: { invoiceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.invoice).toBe(foundInvoice);
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
