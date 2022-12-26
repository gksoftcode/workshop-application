<template>
  <div>
    <h2 id="page-heading" data-cy="InvoiceDetailsHeading">
      <span v-text="$t('workshopApp.invoiceDetails.home.title')" id="invoice-details-heading">Invoice Details</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.invoiceDetails.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'InvoiceDetailsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-invoice-details"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.invoiceDetails.home.createLabel')"> Create a new Invoice Details </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && invoiceDetails && invoiceDetails.length === 0">
      <span v-text="$t('workshopApp.invoiceDetails.home.notFound')">No invoiceDetails found</span>
    </div>
    <div class="table-responsive" v-if="invoiceDetails && invoiceDetails.length > 0">
      <table class="table table-striped" aria-describedby="invoiceDetails">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemNo')">
              <span v-text="$t('workshopApp.invoiceDetails.itemNo')">Item No</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemNo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemDesc')">
              <span v-text="$t('workshopApp.invoiceDetails.itemDesc')">Item Desc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemDesc'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemPrice')">
              <span v-text="$t('workshopApp.invoiceDetails.itemPrice')">Item Price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemQty')">
              <span v-text="$t('workshopApp.invoiceDetails.itemQty')">Item Qty</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemQty'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discountType')">
              <span v-text="$t('workshopApp.invoiceDetails.discountType')">Discount Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discount')">
              <span v-text="$t('workshopApp.invoiceDetails.discount')">Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('totalAmount')">
              <span v-text="$t('workshopApp.invoiceDetails.totalAmount')">Total Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('invoice.id')">
              <span v-text="$t('workshopApp.invoiceDetails.invoice')">Invoice</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoice.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="invoiceDetails in invoiceDetails" :key="invoiceDetails.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InvoiceDetailsView', params: { invoiceDetailsId: invoiceDetails.id } }">{{
                invoiceDetails.id
              }}</router-link>
            </td>
            <td>{{ invoiceDetails.itemNo }}</td>
            <td>{{ invoiceDetails.itemDesc }}</td>
            <td>{{ invoiceDetails.itemPrice }}</td>
            <td>{{ invoiceDetails.itemQty }}</td>
            <td v-text="$t('workshopApp.DiscountType.' + invoiceDetails.discountType)">{{ invoiceDetails.discountType }}</td>
            <td>{{ invoiceDetails.discount }}</td>
            <td>{{ invoiceDetails.totalAmount }}</td>
            <td>
              <div v-if="invoiceDetails.invoice">
                <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoiceDetails.invoice.id } }">{{
                  invoiceDetails.invoice.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'InvoiceDetailsView', params: { invoiceDetailsId: invoiceDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'InvoiceDetailsEdit', params: { invoiceDetailsId: invoiceDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(invoiceDetails)"
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
        ><span
          id="workshopApp.invoiceDetails.delete.question"
          data-cy="invoiceDetailsDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-invoiceDetails-heading" v-text="$t('workshopApp.invoiceDetails.delete.question', { id: removeId })">
          Are you sure you want to delete this Invoice Details?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-invoiceDetails"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeInvoiceDetails()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="invoiceDetails && invoiceDetails.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice-details.component.ts"></script>
