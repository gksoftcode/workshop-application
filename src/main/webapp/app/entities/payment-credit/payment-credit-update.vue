<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.paymentCredit.home.createOrEditLabel"
          data-cy="PaymentCreditCreateUpdateHeading"
          v-text="$t('workshopApp.paymentCredit.home.createOrEditLabel')"
        >
          Create or edit a PaymentCredit
        </h2>
        <div>
          <div class="form-group" v-if="paymentCredit.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="paymentCredit.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.paymentMethod')" for="payment-credit-paymentMethod"
              >Payment Method</label
            >
            <select
              class="form-control"
              name="paymentMethod"
              :class="{ valid: !$v.paymentCredit.paymentMethod.$invalid, invalid: $v.paymentCredit.paymentMethod.$invalid }"
              v-model="$v.paymentCredit.paymentMethod.$model"
              id="payment-credit-paymentMethod"
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
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.paymentRef')" for="payment-credit-paymentRef"
              >Payment Ref</label
            >
            <input
              type="text"
              class="form-control"
              name="paymentRef"
              id="payment-credit-paymentRef"
              data-cy="paymentRef"
              :class="{ valid: !$v.paymentCredit.paymentRef.$invalid, invalid: $v.paymentCredit.paymentRef.$invalid }"
              v-model="$v.paymentCredit.paymentRef.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.amount')" for="payment-credit-amount">Amount</label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="payment-credit-amount"
              data-cy="amount"
              :class="{ valid: !$v.paymentCredit.amount.$invalid, invalid: $v.paymentCredit.amount.$invalid }"
              v-model.number="$v.paymentCredit.amount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.addDate')" for="payment-credit-addDate">Add Date</label>
            <div class="d-flex">
              <input
                id="payment-credit-addDate"
                data-cy="addDate"
                type="datetime-local"
                class="form-control"
                name="addDate"
                :class="{ valid: !$v.paymentCredit.addDate.$invalid, invalid: $v.paymentCredit.addDate.$invalid }"
                :value="convertDateTimeFromServer($v.paymentCredit.addDate.$model)"
                @change="updateInstantField('addDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.paymentStatus')" for="payment-credit-paymentStatus"
              >Payment Status</label
            >
            <select
              class="form-control"
              name="paymentStatus"
              :class="{ valid: !$v.paymentCredit.paymentStatus.$invalid, invalid: $v.paymentCredit.paymentStatus.$invalid }"
              v-model="$v.paymentCredit.paymentStatus.$model"
              id="payment-credit-paymentStatus"
              data-cy="paymentStatus"
            >
              <option
                v-for="paymentStatus in paymentStatusValues"
                :key="paymentStatus"
                v-bind:value="paymentStatus"
                v-bind:label="$t('workshopApp.PaymentStatus.' + paymentStatus)"
              >
                {{ paymentStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.paymentDetails')" for="payment-credit-paymentDetails"
              >Payment Details</label
            >
            <input
              type="text"
              class="form-control"
              name="paymentDetails"
              id="payment-credit-paymentDetails"
              data-cy="paymentDetails"
              :class="{ valid: !$v.paymentCredit.paymentDetails.$invalid, invalid: $v.paymentCredit.paymentDetails.$invalid }"
              v-model="$v.paymentCredit.paymentDetails.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.receiptNotes')" for="payment-credit-receiptNotes"
              >Receipt Notes</label
            >
            <input
              type="text"
              class="form-control"
              name="receiptNotes"
              id="payment-credit-receiptNotes"
              data-cy="receiptNotes"
              :class="{ valid: !$v.paymentCredit.receiptNotes.$invalid, invalid: $v.paymentCredit.receiptNotes.$invalid }"
              v-model="$v.paymentCredit.receiptNotes.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('workshopApp.paymentCredit.attachments')" for="payment-credit-attachments">Attachments</label>
            <select
              class="form-control"
              id="payment-credit-attachments"
              data-cy="attachments"
              multiple
              name="attachments"
              v-if="paymentCredit.attachments !== undefined"
              v-model="paymentCredit.attachments"
            >
              <option
                v-bind:value="getSelected(paymentCredit.attachments, attachmentsOption)"
                v-for="attachmentsOption in attachments"
                :key="attachmentsOption.id"
              >
                {{ attachmentsOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.paymentCredit.workOrders')" for="payment-credit-workOrders"
              >Work Orders</label
            >
            <select
              class="form-control"
              id="payment-credit-workOrders"
              data-cy="workOrders"
              name="workOrders"
              v-model="paymentCredit.workOrders"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  paymentCredit.workOrders && workOrdersOption.id === paymentCredit.workOrders.id
                    ? paymentCredit.workOrders
                    : workOrdersOption
                "
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
            :disabled="$v.paymentCredit.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./payment-credit-update.component.ts"></script>
