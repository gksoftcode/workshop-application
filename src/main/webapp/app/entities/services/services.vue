<template>
  <div>
    <h2 id="page-heading" data-cy="ServicesHeading">
      <span v-text="$t('workshopApp.services.home.title')" id="services-heading">Services</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.services.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ServicesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-services"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.services.home.createLabel')"> Create a new Services </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && services && services.length === 0">
      <span v-text="$t('workshopApp.services.home.notFound')">No services found</span>
    </div>
    <div class="table-responsive" v-if="services && services.length > 0">
      <table class="table table-striped" aria-describedby="services">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('workshopApp.services.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('description')">
              <span v-text="$t('workshopApp.services.description')">Description</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'description'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cost')">
              <span v-text="$t('workshopApp.services.cost')">Cost</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cost'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('price')">
              <span v-text="$t('workshopApp.services.price')">Price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discount')">
              <span v-text="$t('workshopApp.services.discount')">Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span v-text="$t('workshopApp.services.notes')">Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discountType')">
              <span v-text="$t('workshopApp.services.discountType')">Discount Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tax.name')">
              <span v-text="$t('workshopApp.services.tax')">Tax</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tax.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('serviceCategory.id')">
              <span v-text="$t('workshopApp.services.serviceCategory')">Service Category</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'serviceCategory.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('invoice.id')">
              <span v-text="$t('workshopApp.services.invoice')">Invoice</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoice.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('purchaseOrder.id')">
              <span v-text="$t('workshopApp.services.purchaseOrder')">Purchase Order</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'purchaseOrder.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="services in services" :key="services.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ServicesView', params: { servicesId: services.id } }">{{ services.id }}</router-link>
            </td>
            <td>{{ services.name }}</td>
            <td>{{ services.description }}</td>
            <td>{{ services.cost }}</td>
            <td>{{ services.price }}</td>
            <td>{{ services.discount }}</td>
            <td>{{ services.notes }}</td>
            <td v-text="$t('workshopApp.DiscountType.' + services.discountType)">{{ services.discountType }}</td>
            <td>
              <div v-if="services.tax">
                <router-link :to="{ name: 'TaxView', params: { taxId: services.tax.id } }">{{ services.tax.name }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="services.serviceCategory">
                <router-link :to="{ name: 'ServiceCategoryView', params: { serviceCategoryId: services.serviceCategory.id } }">{{
                  services.serviceCategory.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="services.invoice">
                <router-link :to="{ name: 'InvoiceView', params: { invoiceId: services.invoice.id } }">{{
                  services.invoice.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="services.purchaseOrder">
                <router-link :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: services.purchaseOrder.id } }">{{
                  services.purchaseOrder.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ServicesView', params: { servicesId: services.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ServicesEdit', params: { servicesId: services.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(services)"
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
        ><span id="workshopApp.services.delete.question" data-cy="servicesDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-services-heading" v-text="$t('workshopApp.services.delete.question', { id: removeId })">
          Are you sure you want to delete this Services?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-services"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeServices()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="services && services.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./services.component.ts"></script>
