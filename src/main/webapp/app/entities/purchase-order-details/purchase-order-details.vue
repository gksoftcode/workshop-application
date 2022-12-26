<template>
  <div>
    <h2 id="page-heading" data-cy="PurchaseOrderDetailsHeading">
      <span v-text="$t('workshopApp.purchaseOrderDetails.home.title')" id="purchase-order-details-heading">Purchase Order Details</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.purchaseOrderDetails.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PurchaseOrderDetailsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-purchase-order-details"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.purchaseOrderDetails.home.createLabel')"> Create a new Purchase Order Details </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && purchaseOrderDetails && purchaseOrderDetails.length === 0">
      <span v-text="$t('workshopApp.purchaseOrderDetails.home.notFound')">No purchaseOrderDetails found</span>
    </div>
    <div class="table-responsive" v-if="purchaseOrderDetails && purchaseOrderDetails.length > 0">
      <table class="table table-striped" aria-describedby="purchaseOrderDetails">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemNo')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.itemNo')">Item No</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemNo'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemDesc')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.itemDesc')">Item Desc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemDesc'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemPrice')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.itemPrice')">Item Price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemPrice'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('itemQty')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.itemQty')">Item Qty</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'itemQty'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discountType')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.discountType')">Discount Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discount')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.discount')">Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('totalAmount')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.totalAmount')">Total Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('tax.name')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.tax')">Tax</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'tax.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('purchaseOrder.id')">
              <span v-text="$t('workshopApp.purchaseOrderDetails.purchaseOrder')">Purchase Order</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'purchaseOrder.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="purchaseOrderDetails in purchaseOrderDetails" :key="purchaseOrderDetails.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PurchaseOrderDetailsView', params: { purchaseOrderDetailsId: purchaseOrderDetails.id } }">{{
                purchaseOrderDetails.id
              }}</router-link>
            </td>
            <td>{{ purchaseOrderDetails.itemNo }}</td>
            <td>{{ purchaseOrderDetails.itemDesc }}</td>
            <td>{{ purchaseOrderDetails.itemPrice }}</td>
            <td>{{ purchaseOrderDetails.itemQty }}</td>
            <td v-text="$t('workshopApp.DiscountType.' + purchaseOrderDetails.discountType)">{{ purchaseOrderDetails.discountType }}</td>
            <td>{{ purchaseOrderDetails.discount }}</td>
            <td>{{ purchaseOrderDetails.totalAmount }}</td>
            <td>
              <div v-if="purchaseOrderDetails.tax">
                <router-link :to="{ name: 'TaxView', params: { taxId: purchaseOrderDetails.tax.id } }">{{
                  purchaseOrderDetails.tax.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="purchaseOrderDetails.purchaseOrder">
                <router-link :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: purchaseOrderDetails.purchaseOrder.id } }">{{
                  purchaseOrderDetails.purchaseOrder.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PurchaseOrderDetailsView', params: { purchaseOrderDetailsId: purchaseOrderDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PurchaseOrderDetailsEdit', params: { purchaseOrderDetailsId: purchaseOrderDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(purchaseOrderDetails)"
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
          id="workshopApp.purchaseOrderDetails.delete.question"
          data-cy="purchaseOrderDetailsDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-purchaseOrderDetails-heading" v-text="$t('workshopApp.purchaseOrderDetails.delete.question', { id: removeId })">
          Are you sure you want to delete this Purchase Order Details?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-purchaseOrderDetails"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePurchaseOrderDetails()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="purchaseOrderDetails && purchaseOrderDetails.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./purchase-order-details.component.ts"></script>
