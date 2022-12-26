/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ActionDetailComponent from '@/entities/action/action-details.vue';
import ActionClass from '@/entities/action/action-details.component';
import ActionService from '@/entities/action/action.service';
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
  describe('Action Management Detail Component', () => {
    let wrapper: Wrapper<ActionClass>;
    let comp: ActionClass;
    let actionServiceStub: SinonStubbedInstance<ActionService>;

    beforeEach(() => {
      actionServiceStub = sinon.createStubInstance<ActionService>(ActionService);

      wrapper = shallowMount<ActionClass>(ActionDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { actionService: () => actionServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundAction = { id: 123 };
        actionServiceStub.find.resolves(foundAction);

        // WHEN
        comp.retrieveAction(123);
        await comp.$nextTick();

        // THEN
        expect(comp.action).toBe(foundAction);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundAction = { id: 123 };
        actionServiceStub.find.resolves(foundAction);

        // WHEN
        comp.beforeRouteEnter({ params: { actionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.action).toBe(foundAction);
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
