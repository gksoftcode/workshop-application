<template>
  <div>
    <h2 id="page-heading" data-cy="AppintmentHeading">
      <span v-text="$t('workshopApp.appintment.home.title')" id="appintment-heading">Appintments</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.appintment.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AppintmentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-appintment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.appintment.home.createLabel')"> Create a new Appintment </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && appintments && appintments.length === 0">
      <span v-text="$t('workshopApp.appintment.home.notFound')">No appintments found</span>
    </div>
    <div class="table-responsive" v-if="appintments && appintments.length > 0">
      <table class="table table-striped" aria-describedby="appintments">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('appDate')">
              <span v-text="$t('workshopApp.appintment.appDate')">App Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'appDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span v-text="$t('workshopApp.appintment.notes')">Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('client.id')">
              <span v-text="$t('workshopApp.appintment.client')">Client</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'client.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('workOrders.id')">
              <span v-text="$t('workshopApp.appintment.workOrders')">Work Orders</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'workOrders.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appintment in appintments" :key="appintment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AppintmentView', params: { appintmentId: appintment.id } }">{{ appintment.id }}</router-link>
            </td>
            <td>{{ appintment.appDate ? $d(Date.parse(appintment.appDate), 'short') : '' }}</td>
            <td>{{ appintment.notes }}</td>
            <td>
              <div v-if="appintment.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: appintment.client.id } }">{{
                  appintment.client.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="appintment.workOrders">
                <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: appintment.workOrders.id } }">{{
                  appintment.workOrders.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AppintmentView', params: { appintmentId: appintment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AppintmentEdit', params: { appintmentId: appintment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(appintment)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="workshopApp.appintment.delete.question" data-cy="appintmentDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-appintment-heading" v-text="$t('workshopApp.appintment.delete.question', { id: removeId })">
          Are you sure you want to delete this Appintment?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-appintment"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAppintment()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="appintments && appintments.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./appintment.component.ts"></script>
