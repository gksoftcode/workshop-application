<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentCreditHeading">
      <span v-text="$t('workshopApp.paymentCredit.home.title')" id="payment-credit-heading">Payment Credits</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.paymentCredit.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PaymentCreditCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-credit"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.paymentCredit.home.createLabel')"> Create a new Payment Credit </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentCredits && paymentCredits.length === 0">
      <span v-text="$t('workshopApp.paymentCredit.home.notFound')">No paymentCredits found</span>
    </div>
    <div class="table-responsive" v-if="paymentCredits && paymentCredits.length > 0">
      <table class="table table-striped" aria-describedby="paymentCredits">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentMethod')">
              <span v-text="$t('workshopApp.paymentCredit.paymentMethod')">Payment Method</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentMethod'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentRef')">
              <span v-text="$t('workshopApp.paymentCredit.paymentRef')">Payment Ref</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentRef'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('amount')">
              <span v-text="$t('workshopApp.paymentCredit.amount')">Amount</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('addDate')">
              <span v-text="$t('workshopApp.paymentCredit.addDate')">Add Date</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'addDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentStatus')">
              <span v-text="$t('workshopApp.paymentCredit.paymentStatus')">Payment Status</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentStatus'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('paymentDetails')">
              <span v-text="$t('workshopApp.paymentCredit.paymentDetails')">Payment Details</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentDetails'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('receiptNotes')">
              <span v-text="$t('workshopApp.paymentCredit.receiptNotes')">Receipt Notes</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'receiptNotes'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('workOrders.id')">
              <span v-text="$t('workshopApp.paymentCredit.workOrders')">Work Orders</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'workOrders.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentCredit in paymentCredits" :key="paymentCredit.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentCreditView', params: { paymentCreditId: paymentCredit.id } }">{{
                paymentCredit.id
              }}</router-link>
            </td>
            <td v-text="$t('workshopApp.PaymentMethod.' + paymentCredit.paymentMethod)">{{ paymentCredit.paymentMethod }}</td>
            <td>{{ paymentCredit.paymentRef }}</td>
            <td>{{ paymentCredit.amount }}</td>
            <td>{{ paymentCredit.addDate ? $d(Date.parse(paymentCredit.addDate), 'short') : '' }}</td>
            <td v-text="$t('workshopApp.PaymentStatus.' + paymentCredit.paymentStatus)">{{ paymentCredit.paymentStatus }}</td>
            <td>{{ paymentCredit.paymentDetails }}</td>
            <td>{{ paymentCredit.receiptNotes }}</td>
            <td>
              <div v-if="paymentCredit.workOrders">
                <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: paymentCredit.workOrders.id } }">{{
                  paymentCredit.workOrders.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PaymentCreditView', params: { paymentCreditId: paymentCredit.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PaymentCreditEdit', params: { paymentCreditId: paymentCredit.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(paymentCredit)"
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
        ><span id="workshopApp.paymentCredit.delete.question" data-cy="paymentCreditDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-paymentCredit-heading" v-text="$t('workshopApp.paymentCredit.delete.question', { id: removeId })">
          Are you sure you want to delete this Payment Credit?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-paymentCredit"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePaymentCredit()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="paymentCredits && paymentCredits.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-credit.component.ts"></script>
