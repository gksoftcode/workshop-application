<template>
  <div>
    <h2 id="page-heading" data-cy="InvoiceHeading">
      <span v-text="$t('workshopApp.invoice.home.title')" id="invoice-heading">Invoices</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.invoice.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'InvoiceCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-invoice"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.invoice.home.createLabel')"> Create a new Invoice </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && invoices && invoices.length === 0">
      <span v-text="$t('workshopApp.invoice.home.notFound')">No invoices found</span>
    </div>
    <div class="table-responsive" v-if="invoices && invoices.length > 0">
      <table class="table table-striped" aria-describedby="invoices">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('invoiceDate')">
              <span v-text="$t('workshopApp.invoice.invoiceDate')">Invoice Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'invoiceDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('issueDate')">
              <span v-text="$t('workshopApp.invoice.issueDate')">Issue Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'issueDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentTerms')">
              <span v-text="$t('workshopApp.invoice.paymentTerms')">Payment Terms</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentTerms'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discount')">
              <span v-text="$t('workshopApp.invoice.discount')">Discount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('notes')">
              <span v-text="$t('workshopApp.invoice.notes')">Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'notes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('discountType')">
              <span v-text="$t('workshopApp.invoice.discountType')">Discount Type</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountType'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositAmount')">
              <span v-text="$t('workshopApp.invoice.depositAmount')">Deposit Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isDepositPaied')">
              <span v-text="$t('workshopApp.invoice.isDepositPaied')">Is Deposit Paied</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isDepositPaied'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositMethod')">
              <span v-text="$t('workshopApp.invoice.depositMethod')">Deposit Method</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositMethod'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('depositPayRef')">
              <span v-text="$t('workshopApp.invoice.depositPayRef')">Deposit Pay Ref</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'depositPayRef'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('isAlreadyPaied')">
              <span v-text="$t('workshopApp.invoice.isAlreadyPaied')">Is Already Paied</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'isAlreadyPaied'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentMethod')">
              <span v-text="$t('workshopApp.invoice.paymentMethod')">Payment Method</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentMethod'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentRef')">
              <span v-text="$t('workshopApp.invoice.paymentRef')">Payment Ref</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentRef'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="$t('workshopApp.invoice.amount')">Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('lastAmount')">
              <span v-text="$t('workshopApp.invoice.lastAmount')">Last Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'lastAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('shippingFees')">
              <span v-text="$t('workshopApp.invoice.shippingFees')">Shipping Fees</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'shippingFees'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('client.fullName')">
              <span v-text="$t('workshopApp.invoice.client')">Client</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'client.fullName'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('workOrders.id')">
              <span v-text="$t('workshopApp.invoice.workOrders')">Work Orders</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'workOrders.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="invoice in invoices" :key="invoice.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }">{{ invoice.id }}</router-link>
            </td>
            <td>{{ invoice.invoiceDate ? $d(Date.parse(invoice.invoiceDate), 'short') : '' }}</td>
            <td>{{ invoice.issueDate ? $d(Date.parse(invoice.issueDate), 'short') : '' }}</td>
            <td>{{ invoice.paymentTerms }}</td>
            <td>{{ invoice.discount }}</td>
            <td>{{ invoice.notes }}</td>
            <td v-text="$t('workshopApp.DiscountType.' + invoice.discountType)">{{ invoice.discountType }}</td>
            <td>{{ invoice.depositAmount }}</td>
            <td>{{ invoice.isDepositPaied }}</td>
            <td v-text="$t('workshopApp.PaymentMethod.' + invoice.depositMethod)">{{ invoice.depositMethod }}</td>
            <td>{{ invoice.depositPayRef }}</td>
            <td>{{ invoice.isAlreadyPaied }}</td>
            <td v-text="$t('workshopApp.PaymentMethod.' + invoice.paymentMethod)">{{ invoice.paymentMethod }}</td>
            <td>{{ invoice.paymentRef }}</td>
            <td>{{ invoice.amount }}</td>
            <td>{{ invoice.lastAmount }}</td>
            <td>{{ invoice.shippingFees }}</td>
            <td>
              <div v-if="invoice.client">
                <router-link :to="{ name: 'ClientView', params: { clientId: invoice.client.id } }">{{
                  invoice.client.fullName
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="invoice.workOrders">
                <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: invoice.workOrders.id } }">{{
                  invoice.workOrders.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'InvoiceEdit', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(invoice)"
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
        ><span id="workshopApp.invoice.delete.question" data-cy="invoiceDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-invoice-heading" v-text="$t('workshopApp.invoice.delete.question', { id: removeId })">
          Are you sure you want to delete this Invoice?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-invoice"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeInvoice()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="invoices && invoices.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice.component.ts"></script>
