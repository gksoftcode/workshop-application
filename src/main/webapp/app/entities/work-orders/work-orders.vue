<template>
  <div>
    <h2 id="page-heading" data-cy="WorkOrdersHeading">
      <span v-text="$t('workshopApp.workOrders.home.title')" id="work-orders-heading">Work Orders</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.workOrders.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'WorkOrdersCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-work-orders"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.workOrders.home.createLabel')"> Create a new Work Orders </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && workOrders && workOrders.length === 0">
      <span v-text="$t('workshopApp.workOrders.home.notFound')">No workOrders found</span>
    </div>
    <div class="table-responsive" v-if="workOrders && workOrders.length > 0">
      <table class="table table-striped" aria-describedby="workOrders">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('title')">
              <span v-text="$t('workshopApp.workOrders.title')">Title</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'title'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('workshopApp.workOrders.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('startDate')">
              <span v-text="$t('workshopApp.workOrders.startDate')">Start Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('endDate')">
              <span v-text="$t('workshopApp.workOrders.endDate')">End Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('budget')">
              <span v-text="$t('workshopApp.workOrders.budget')">Budget</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'budget'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemSerial')">
              <span v-text="$t('workshopApp.workOrders.itemSerial')">Item Serial</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemSerial'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isWaranty')">
              <span v-text="$t('workshopApp.workOrders.isWaranty')">Is Waranty</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isWaranty'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span v-text="$t('workshopApp.workOrders.notes')">Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('status.name')">
              <span v-text="$t('workshopApp.workOrders.status')">Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('client.fullName')">
              <span v-text="$t('workshopApp.workOrders.client')">Client</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'client.fullName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemModels.name')">
              <span v-text="$t('workshopApp.workOrders.itemModels')">Item Models</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemModels.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemBrand.name')">
              <span v-text="$t('workshopApp.workOrders.itemBrand')">Item Brand</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemBrand.name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="workOrders in workOrders" :key="workOrders.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: workOrders.id } }">{{ workOrders.id }}</router-link>
            </td>
            <td>{{ workOrders.title }}</td>
            <td>{{ workOrders.description }}</td>
            <td>{{ workOrders.startDate ? $d(Date.parse(workOrders.startDate), 'short') : '' }}</td>
            <td>{{ workOrders.endDate ? $d(Date.parse(workOrders.endDate), 'short') : '' }}</td>
            <td>{{ workOrders.budget }}</td>
            <td>{{ workOrders.itemSerial }}</td>
            <td>{{ workOrders.isWaranty }}</td>
            <td>{{ workOrders.notes }}</td>
            <td>
              <div v-if="workOrders.status">
                <router-link :to="{ name: 'StatusView', params: { statusId: workOrders.status.id } }">{{
                  workOrders.status.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="workOrders.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: workOrders.client.id } }">{{
                  workOrders.client.fullName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="workOrders.itemModels">
                <router-link :to="{ name: 'ItemModelsView', params: { itemModelsId: workOrders.itemModels.id } }">{{
                  workOrders.itemModels.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="workOrders.itemBrand">
                <router-link :to="{ name: 'ItemBrandView', params: { itemBrandId: workOrders.itemBrand.id } }">{{
                  workOrders.itemBrand.name
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: workOrders.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'WorkOrdersEdit', params: { workOrdersId: workOrders.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(workOrders)"
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
        ><span id="workshopApp.workOrders.delete.question" data-cy="workOrdersDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-workOrders-heading" v-text="$t('workshopApp.workOrders.delete.question', { id: removeId })">
          Are you sure you want to delete this Work Orders?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-workOrders"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeWorkOrders()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="workOrders && workOrders.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./work-orders.component.ts"></script>
