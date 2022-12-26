<template>
  <div>
    <h2 id="page-heading" data-cy="PurchaseOrderHeading">
      <span v-text="$t('workshopApp.purchaseOrder.home.title')" id="purchase-order-heading">Purchase Orders</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.purchaseOrder.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PurchaseOrderCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-purchase-order"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.purchaseOrder.home.createLabel')"> Create a new Purchase Order </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && purchaseOrders && purchaseOrders.length === 0">
      <span v-text="$t('workshopApp.purchaseOrder.home.notFound')">No purchaseOrders found</span>
    </div>
    <div class="table-responsive" v-if="purchaseOrders && purchaseOrders.length > 0">
      <table class="table table-striped" aria-describedby="purchaseOrders">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('invoiceDate')">
              <span v-text="$t('workshopApp.purchaseOrder.invoiceDate')">Invoice Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('issueDate')">
              <span v-text="$t('workshopApp.purchaseOrder.issueDate')">Issue Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'issueDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentTerms')">
              <span v-text="$t('workshopApp.purchaseOrder.paymentTerms')">Payment Terms</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentTerms'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discount')">
              <span v-text="$t('workshopApp.purchaseOrder.discount')">Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span v-text="$t('workshopApp.purchaseOrder.notes')">Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discountType')">
              <span v-text="$t('workshopApp.purchaseOrder.discountType')">Discount Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositAmount')">
              <span v-text="$t('workshopApp.purchaseOrder.depositAmount')">Deposit Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isDepositPaied')">
              <span v-text="$t('workshopApp.purchaseOrder.isDepositPaied')">Is Deposit Paied</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isDepositPaied'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositMethod')">
              <span v-text="$t('workshopApp.purchaseOrder.depositMethod')">Deposit Method</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositMethod'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositPayRef')">
              <span v-text="$t('workshopApp.purchaseOrder.depositPayRef')">Deposit Pay Ref</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositPayRef'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isAlreadyPaied')">
              <span v-text="$t('workshopApp.purchaseOrder.isAlreadyPaied')">Is Already Paied</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isAlreadyPaied'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentMethod')">
              <span v-text="$t('workshopApp.purchaseOrder.paymentMethod')">Payment Method</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentMethod'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentRef')">
              <span v-text="$t('workshopApp.purchaseOrder.paymentRef')">Payment Ref</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentRef'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="$t('workshopApp.purchaseOrder.amount')">Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('lastAmount')">
              <span v-text="$t('workshopApp.purchaseOrder.lastAmount')">Last Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('shippingFees')">
              <span v-text="$t('workshopApp.purchaseOrder.shippingFees')">Shipping Fees</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'shippingFees'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('supplier.name')">
              <span v-text="$t('workshopApp.purchaseOrder.supplier')">Supplier</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'supplier.name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('workOrders.id')">
              <span v-text="$t('workshopApp.purchaseOrder.workOrders')">Work Orders</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'workOrders.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="purchaseOrder in purchaseOrders" :key="purchaseOrder.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: purchaseOrder.id } }">{{
                purchaseOrder.id
              }}</router-link>
            </td>
            <td>{{ purchaseOrder.invoiceDate ? $d(Date.parse(purchaseOrder.invoiceDate), 'short') : '' }}</td>
            <td>{{ purchaseOrder.issueDate ? $d(Date.parse(purchaseOrder.issueDate), 'short') : '' }}</td>
            <td>{{ purchaseOrder.paymentTerms }}</td>
            <td>{{ purchaseOrder.discount }}</td>
            <td>{{ purchaseOrder.notes }}</td>
            <td v-text="$t('workshopApp.DiscountType.' + purchaseOrder.discountType)">{{ purchaseOrder.discountType }}</td>
            <td>{{ purchaseOrder.depositAmount }}</td>
            <td>{{ purchaseOrder.isDepositPaied }}</td>
            <td v-text="$t('workshopApp.PaymentMethod.' + purchaseOrder.depositMethod)">{{ purchaseOrder.depositMethod }}</td>
            <td>{{ purchaseOrder.depositPayRef }}</td>
            <td>{{ purchaseOrder.isAlreadyPaied }}</td>
            <td v-text="$t('workshopApp.PaymentMethod.' + purchaseOrder.paymentMethod)">{{ purchaseOrder.paymentMethod }}</td>
            <td>{{ purchaseOrder.paymentRef }}</td>
            <td>{{ purchaseOrder.amount }}</td>
            <td>{{ purchaseOrder.lastAmount }}</td>
            <td>{{ purchaseOrder.shippingFees }}</td>
            <td>
              <div v-if="purchaseOrder.supplier">
                <router-link :to="{ name: 'SupplierView', params: { supplierId: purchaseOrder.supplier.id } }">{{
                  purchaseOrder.supplier.name
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="purchaseOrder.workOrders">
                <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: purchaseOrder.workOrders.id } }">{{
                  purchaseOrder.workOrders.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: purchaseOrder.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PurchaseOrderEdit', params: { purchaseOrderId: purchaseOrder.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(purchaseOrder)"
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
        ><span id="workshopApp.purchaseOrder.delete.question" data-cy="purchaseOrderDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-purchaseOrder-heading" v-text="$t('workshopApp.purchaseOrder.delete.question', { id: removeId })">
          Are you sure you want to delete this Purchase Order?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-purchaseOrder"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePurchaseOrder()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="purchaseOrders && purchaseOrders.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./purchase-order.component.ts"></script>
