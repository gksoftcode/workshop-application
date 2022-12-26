<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.invoice.home.createOrEditLabel"
          data-cy="InvoiceCreateUpdateHeading"
          v-text="$t('workshopApp.invoice.home.createOrEditLabel')"
        >
          Create or edit a Invoice
        </h2>
        <div>
          <div class="form-group" v-if="invoice.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="invoice.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.invoiceDate')" for="invoice-invoiceDate">Invoice Date</label>
            <div class="d-flex">
              <input
                id="invoice-invoiceDate"
                data-cy="invoiceDate"
                type="datetime-local"
                class="form-control"
                name="invoiceDate"
                :class="{ valid: !$v.invoice.invoiceDate.$invalid, invalid: $v.invoice.invoiceDate.$invalid }"
                :value="convertDateTimeFromServer($v.invoice.invoiceDate.$model)"
                @change="updateInstantField('invoiceDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.issueDate')" for="invoice-issueDate">Issue Date</label>
            <div class="d-flex">
              <input
                id="invoice-issueDate"
                data-cy="issueDate"
                type="datetime-local"
                class="form-control"
                name="issueDate"
                :class="{ valid: !$v.invoice.issueDate.$invalid, invalid: $v.invoice.issueDate.$invalid }"
                :value="convertDateTimeFromServer($v.invoice.issueDate.$model)"
                @change="updateInstantField('issueDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.paymentTerms')" for="invoice-paymentTerms"
              >Payment Terms</label
            >
            <input
              type="number"
              class="form-control"
              name="paymentTerms"
              id="invoice-paymentTerms"
              data-cy="paymentTerms"
              :class="{ valid: !$v.invoice.paymentTerms.$invalid, invalid: $v.invoice.paymentTerms.$invalid }"
              v-model.number="$v.invoice.paymentTerms.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.discount')" for="invoice-discount">Discount</label>
            <input
              type="number"
              class="form-control"
              name="discount"
              id="invoice-discount"
              data-cy="discount"
              :class="{ valid: !$v.invoice.discount.$invalid, invalid: $v.invoice.discount.$invalid }"
              v-model.number="$v.invoice.discount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.notes')" for="invoice-notes">Notes</label>
            <input
              type="number"
              class="form-control"
              name="notes"
              id="invoice-notes"
              data-cy="notes"
              :class="{ valid: !$v.invoice.notes.$invalid, invalid: $v.invoice.notes.$invalid }"
              v-model.number="$v.invoice.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.discountType')" for="invoice-discountType"
              >Discount Type</label
            >
            <select
              class="form-control"
              name="discountType"
              :class="{ valid: !$v.invoice.discountType.$invalid, invalid: $v.invoice.discountType.$invalid }"
              v-model="$v.invoice.discountType.$model"
              id="invoice-discountType"
              data-cy="discountType"
            >
              <option
                v-for="discountType in discountTypeValues"
                :key="discountType"
                v-bind:value="discountType"
                v-bind:label="$t('workshopApp.DiscountType.' + discountType)"
              >
                {{ discountType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.depositAmount')" for="invoice-depositAmount"
              >Deposit Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="depositAmount"
              id="invoice-depositAmount"
              data-cy="depositAmount"
              :class="{ valid: !$v.invoice.depositAmount.$invalid, invalid: $v.invoice.depositAmount.$invalid }"
              v-model.number="$v.invoice.depositAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.isDepositPaied')" for="invoice-isDepositPaied"
              >Is Deposit Paied</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="isDepositPaied"
              id="invoice-isDepositPaied"
              data-cy="isDepositPaied"
              :class="{ valid: !$v.invoice.isDepositPaied.$invalid, invalid: $v.invoice.isDepositPaied.$invalid }"
              v-model="$v.invoice.isDepositPaied.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.depositMethod')" for="invoice-depositMethod"
              >Deposit Method</label
            >
            <select
              class="form-control"
              name="depositMethod"
              :class="{ valid: !$v.invoice.depositMethod.$invalid, invalid: $v.invoice.depositMethod.$invalid }"
              v-model="$v.invoice.depositMethod.$model"
              id="invoice-depositMethod"
              data-cy="depositMethod"
            >
              <option
                v-for="paymentMethod in paymentMethodValues"
                :key="paymentMethod"
                v-bind:value="paymentMethod"
                v-bind:label="$t('workshopApp.PaymentMethod.' + paymentMethod)"
              >
                {{ paymentMethod }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.depositPayRef')" for="invoice-depositPayRef"
              >Deposit Pay Ref</label
            >
            <input
              type="text"
              class="form-control"
              name="depositPayRef"
              id="invoice-depositPayRef"
              data-cy="depositPayRef"
              :class="{ valid: !$v.invoice.depositPayRef.$invalid, invalid: $v.invoice.depositPayRef.$invalid }"
              v-model="$v.invoice.depositPayRef.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.isAlreadyPaied')" for="invoice-isAlreadyPaied"
              >Is Already Paied</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="isAlreadyPaied"
              id="invoice-isAlreadyPaied"
              data-cy="isAlreadyPaied"
              :class="{ valid: !$v.invoice.isAlreadyPaied.$invalid, invalid: $v.invoice.isAlreadyPaied.$invalid }"
              v-model="$v.invoice.isAlreadyPaied.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.paymentMethod')" for="invoice-paymentMethod"
              >Payment Method</label
            >
            <select
              class="form-control"
              name="paymentMethod"
              :class="{ valid: !$v.invoice.paymentMethod.$invalid, invalid: $v.invoice.paymentMethod.$invalid }"
              v-model="$v.invoice.paymentMethod.$model"
              id="invoice-paymentMethod"
              data-cy="paymentMethod"
            >
              <option
                v-for="paymentMethod in paymentMethodValues"
                :key="paymentMethod"
                v-bind:value="paymentMethod"
                v-bind:label="$t('workshopApp.PaymentMethod.' + paymentMethod)"
              >
                {{ paymentMethod }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.paymentRef')" for="invoice-paymentRef">Payment Ref</label>
            <input
              type="text"
              class="form-control"
              name="paymentRef"
              id="invoice-paymentRef"
              data-cy="paymentRef"
              :class="{ valid: !$v.invoice.paymentRef.$invalid, invalid: $v.invoice.paymentRef.$invalid }"
              v-model="$v.invoice.paymentRef.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.amount')" for="invoice-amount">Amount</label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="invoice-amount"
              data-cy="amount"
              :class="{ valid: !$v.invoice.amount.$invalid, invalid: $v.invoice.amount.$invalid }"
              v-model.number="$v.invoice.amount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.lastAmount')" for="invoice-lastAmount">Last Amount</label>
            <input
              type="number"
              class="form-control"
              name="lastAmount"
              id="invoice-lastAmount"
              data-cy="lastAmount"
              :class="{ valid: !$v.invoice.lastAmount.$invalid, invalid: $v.invoice.lastAmount.$invalid }"
              v-model.number="$v.invoice.lastAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.shippingFees')" for="invoice-shippingFees"
              >Shipping Fees</label
            >
            <input
              type="number"
              class="form-control"
              name="shippingFees"
              id="invoice-shippingFees"
              data-cy="shippingFees"
              :class="{ valid: !$v.invoice.shippingFees.$invalid, invalid: $v.invoice.shippingFees.$invalid }"
              v-model.number="$v.invoice.shippingFees.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.client')" for="invoice-client">Client</label>
            <select class="form-control" id="invoice-client" data-cy="client" name="client" v-model="invoice.client">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="invoice.client && clientOption.id === invoice.client.id ? invoice.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.fullName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="$t('workshopApp.invoice.attachments')" for="invoice-attachments">Attachments</label>
            <select
              class="form-control"
              id="invoice-attachments"
              data-cy="attachments"
              multiple
              name="attachments"
              v-if="invoice.attachments !== undefined"
              v-model="invoice.attachments"
            >
              <option
                v-bind:value="getSelected(invoice.attachments, attachmentsOption)"
                v-for="attachmentsOption in attachments"
                :key="attachmentsOption.id"
              >
                {{ attachmentsOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoice.workOrders')" for="invoice-workOrders">Work Orders</label>
            <select class="form-control" id="invoice-workOrders" data-cy="workOrders" name="workOrders" v-model="invoice.workOrders">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="invoice.workOrders && workOrdersOption.id === invoice.workOrders.id ? invoice.workOrders : workOrdersOption"
                v-for="workOrdersOption in workOrders"
                :key="workOrdersOption.id"
              >
                {{ workOrdersOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.invoice.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./invoice-update.component.ts"></script>
