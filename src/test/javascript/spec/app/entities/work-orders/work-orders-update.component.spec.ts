/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import WorkOrdersUpdateComponent from '@/entities/work-orders/work-orders-update.vue';
import WorkOrdersClass from '@/entities/work-orders/work-orders-update.component';
import WorkOrdersService from '@/entities/work-orders/work-orders.service';

import StatusService from '@/entities/status/status.service';

import ClientService from '@/entities/client/client.service';

import ItemModelsService from '@/entities/item-models/item-models.service';

import ItemBrandService from '@/entities/item-brand/item-brand.service';

import EmployeeService from '@/entities/employee/employee.service';

import AppintmentService from '@/entities/appintment/appintment.service';

import AttachmentNotesService from '@/entities/attachment-notes/attachment-notes.service';

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
  describe('WorkOrders Management Update Component', () => {
    let wrapper: Wrapper<WorkOrdersClass>;
    let comp: WorkOrdersClass;
    let workOrdersServiceStub: SinonStubbedInstance<WorkOrdersService>;

    beforeEach(() => {
      workOrdersServiceStub = sinon.createStubInstance<WorkOrdersService>(WorkOrdersService);

      wrapper = shallowMount<WorkOrdersClass>(WorkOrdersUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          workOrdersService: () => workOrdersServiceStub,
          alertService: () => new AlertService(),

          statusService: () =>
            sinon.createStubInstance<StatusService>(StatusService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          clientService: () =>
            sinon.createStubInstance<ClientService>(ClientService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          itemModelsService: () =>
            sinon.createStubInstance<ItemModelsService>(ItemModelsService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          itemBrandService: () =>
            sinon.createStubInstance<ItemBrandService>(ItemBrandService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          employeeService: () =>
            sinon.createStubInstance<EmployeeService>(EmployeeService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          appintmentService: () =>
            sinon.createStubInstance<AppintmentService>(AppintmentService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          attachmentNotesService: () =>
            sinon.createStubInstance<AttachmentNotesService>(AttachmentNotesService, {
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
        comp.workOrders = entity;
        workOrdersServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(workOrdersServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.workOrders = entity;
        workOrdersServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(workOrdersServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundWorkOrders = { id: 123 };
        workOrdersServiceStub.find.resolves(foundWorkOrders);
        workOrdersServiceStub.retrieve.resolves([foundWorkOrders]);

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
