/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import AppintmentDetailComponent from '@/entities/appintment/appintment-details.vue';
import AppintmentClass from '@/entities/appintment/appintment-details.component';
import AppintmentService from '@/entities/appintment/appintment.service';
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
  describe('Appintment Management Detail Component', () => {
    let wrapper: Wrapper<AppintmentClass>;
    let comp: AppintmentClass;
    let appintmentServiceStub: SinonStubbedInstance<AppintmentService>;

    beforeEach(() => {
      appintmentServiceStub = sinon.createStubInstance<AppintmentService>(AppintmentService);

      wrapper = shallowMount<AppintmentClass>(AppintmentDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { appintmentService: () => appintmentServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAppintment = { id: 123 };
        appintmentServiceStub.find.resolves(foundAppintment);

        // WHEN
        comp.retrieveAppintment(123);
        await comp.$nextTick();

        // THEN
        expect(comp.appintment).toBe(foundAppintment);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAppintment = { id: 123 };
        appintmentServiceStub.find.resolves(foundAppintment);

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
