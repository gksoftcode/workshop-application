/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import AppintmentUpdateComponent from '@/entities/appintment/appintment-update.vue';
import AppintmentClass from '@/entities/appintment/appintment-update.component';
import AppintmentService from '@/entities/appintment/appintment.service';

import ClientService from '@/entities/client/client.service';

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
  describe('Appintment Management Update Component', () => {
    let wrapper: Wrapper<AppintmentClass>;
    let comp: AppintmentClass;
    let appintmentServiceStub: SinonStubbedInstance<AppintmentService>;

    beforeEach(() => {
      appintmentServiceStub = sinon.createStubInstance<AppintmentService>(AppintmentService);

      wrapper = shallowMount<AppintmentClass>(AppintmentUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          appintmentService: () => appintmentServiceStub,
          alertService: () => new AlertService(),

          clientService: () =>
            sinon.createStubInstance<ClientService>(ClientService, {
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
        comp.appintment = entity;
        appintmentServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appintmentServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.appintment = entity;
        appintmentServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appintmentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAppintment = { id: 123 };
        appintmentServiceStub.find.resolves(foundAppintment);
        appintmentServiceStub.retrieve.resolves([foundAppintment]);

        // WHEN
        comp.beforeRouteEnter({ params: { appintmentId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.appintment).toBe(foundAppintment);
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
