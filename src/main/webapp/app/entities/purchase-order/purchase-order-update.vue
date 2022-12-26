<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.purchaseOrder.home.createOrEditLabel"
          data-cy="PurchaseOrderCreateUpdateHeading"
          v-text="$t('workshopApp.purchaseOrder.home.createOrEditLabel')"
        >
          Create or edit a PurchaseOrder
        </h2>
        <div>
          <div class="form-group" v-if="purchaseOrder.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="purchaseOrder.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.invoiceDate')" for="purchase-order-invoiceDate"
              >Invoice Date</label
            >
            <div class="d-flex">
              <input
                id="purchase-order-invoiceDate"
                data-cy="invoiceDate"
                type="datetime-local"
                class="form-control"
                name="invoiceDate"
                :class="{ valid: !$v.purchaseOrder.invoiceDate.$invalid, invalid: $v.purchaseOrder.invoiceDate.$invalid }"
                :value="convertDateTimeFromServer($v.purchaseOrder.invoiceDate.$model)"
                @change="updateInstantField('invoiceDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.issueDate')" for="purchase-order-issueDate"
              >Issue Date</label
            >
            <div class="d-flex">
              <input
                id="purchase-order-issueDate"
                data-cy="issueDate"
                type="datetime-local"
                class="form-control"
                name="issueDate"
                :class="{ valid: !$v.purchaseOrder.issueDate.$invalid, invalid: $v.purchaseOrder.issueDate.$invalid }"
                :value="convertDateTimeFromServer($v.purchaseOrder.issueDate.$model)"
                @change="updateInstantField('issueDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.paymentTerms')" for="purchase-order-paymentTerms"
              >Payment Terms</label
            >
            <input
              type="number"
              class="form-control"
              name="paymentTerms"
              id="purchase-order-paymentTerms"
              data-cy="paymentTerms"
              :class="{ valid: !$v.purchaseOrder.paymentTerms.$invalid, invalid: $v.purchaseOrder.paymentTerms.$invalid }"
              v-model.number="$v.purchaseOrder.paymentTerms.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.discount')" for="purchase-order-discount"
              >Discount</label
            >
            <input
              type="number"
              class="form-control"
              name="discount"
              id="purchase-order-discount"
              data-cy="discount"
              :class="{ valid: !$v.purchaseOrder.discount.$invalid, invalid: $v.purchaseOrder.discount.$invalid }"
              v-model.number="$v.purchaseOrder.discount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.notes')" for="purchase-order-notes">Notes</label>
            <input
              type="number"
              class="form-control"
              name="notes"
              id="purchase-order-notes"
              data-cy="notes"
              :class="{ valid: !$v.purchaseOrder.notes.$invalid, invalid: $v.purchaseOrder.notes.$invalid }"
              v-model.number="$v.purchaseOrder.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.discountType')" for="purchase-order-discountType"
              >Discount Type</label
            >
            <select
              class="form-control"
              name="discountType"
              :class="{ valid: !$v.purchaseOrder.discountType.$invalid, invalid: $v.purchaseOrder.discountType.$invalid }"
              v-model="$v.purchaseOrder.discountType.$model"
              id="purchase-order-discountType"
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
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.depositAmount')" for="purchase-order-depositAmount"
              >Deposit Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="depositAmount"
              id="purchase-order-depositAmount"
              data-cy="depositAmount"
              :class="{ valid: !$v.purchaseOrder.depositAmount.$invalid, invalid: $v.purchaseOrder.depositAmount.$invalid }"
              v-model.number="$v.purchaseOrder.depositAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.isDepositPaied')" for="purchase-order-isDepositPaied"
              >Is Deposit Paied</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="isDepositPaied"
              id="purchase-order-isDepositPaied"
              data-cy="isDepositPaied"
              :class="{ valid: !$v.purchaseOrder.isDepositPaied.$invalid, invalid: $v.purchaseOrder.isDepositPaied.$invalid }"
              v-model="$v.purchaseOrder.isDepositPaied.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.depositMethod')" for="purchase-order-depositMethod"
              >Deposit Method</label
            >
            <select
              class="form-control"
              name="depositMethod"
              :class="{ valid: !$v.purchaseOrder.depositMethod.$invalid, invalid: $v.purchaseOrder.depositMethod.$invalid }"
              v-model="$v.purchaseOrder.depositMethod.$model"
              id="purchase-order-depositMethod"
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
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.depositPayRef')" for="purchase-order-depositPayRef"
              >Deposit Pay Ref</label
            >
            <input
              type="text"
              class="form-control"
              name="depositPayRef"
              id="purchase-order-depositPayRef"
              data-cy="depositPayRef"
              :class="{ valid: !$v.purchaseOrder.depositPayRef.$invalid, invalid: $v.purchaseOrder.depositPayRef.$invalid }"
              v-model="$v.purchaseOrder.depositPayRef.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.isAlreadyPaied')" for="purchase-order-isAlreadyPaied"
              >Is Already Paied</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="isAlreadyPaied"
              id="purchase-order-isAlreadyPaied"
              data-cy="isAlreadyPaied"
              :class="{ valid: !$v.purchaseOrder.isAlreadyPaied.$invalid, invalid: $v.purchaseOrder.isAlreadyPaied.$invalid }"
              v-model="$v.purchaseOrder.isAlreadyPaied.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.paymentMethod')" for="purchase-order-paymentMethod"
              >Payment Method</label
            >
            <select
              class="form-control"
              name="paymentMethod"
              :class="{ valid: !$v.purchaseOrder.paymentMethod.$invalid, invalid: $v.purchaseOrder.paymentMethod.$invalid }"
              v-model="$v.purchaseOrder.paymentMethod.$model"
              id="purchase-order-paymentMethod"
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
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.paymentRef')" for="purchase-order-paymentRef"
              >Payment Ref</label
            >
            <input
              type="text"
              class="form-control"
              name="paymentRef"
              id="purchase-order-paymentRef"
              data-cy="paymentRef"
              :class="{ valid: !$v.purchaseOrder.paymentRef.$invalid, invalid: $v.purchaseOrder.paymentRef.$invalid }"
              v-model="$v.purchaseOrder.paymentRef.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.amount')" for="purchase-order-amount">Amount</label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="purchase-order-amount"
              data-cy="amount"
              :class="{ valid: !$v.purchaseOrder.amount.$invalid, invalid: $v.purchaseOrder.amount.$invalid }"
              v-model.number="$v.purchaseOrder.amount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.lastAmount')" for="purchase-order-lastAmount"
              >Last Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="lastAmount"
              id="purchase-order-lastAmount"
              data-cy="lastAmount"
              :class="{ valid: !$v.purchaseOrder.lastAmount.$invalid, invalid: $v.purchaseOrder.lastAmount.$invalid }"
              v-model.number="$v.purchaseOrder.lastAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.shippingFees')" for="purchase-order-shippingFees"
              >Shipping Fees</label
            >
            <input
              type="number"
              class="form-control"
              name="shippingFees"
              id="purchase-order-shippingFees"
              data-cy="shippingFees"
              :class="{ valid: !$v.purchaseOrder.shippingFees.$invalid, invalid: $v.purchaseOrder.shippingFees.$invalid }"
              v-model.number="$v.purchaseOrder.shippingFees.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.supplier')" for="purchase-order-supplier"
              >Supplier</label
            >
            <select class="form-control" id="purchase-order-supplier" data-cy="supplier" name="supplier" v-model="purchaseOrder.supplier">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  purchaseOrder.supplier && supplierOption.id === purchaseOrder.supplier.id ? purchaseOrder.supplier : supplierOption
                "
                v-for="supplierOption in suppliers"
                :key="supplierOption.id"
              >
                {{ supplierOption.fullName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="$t('workshopApp.purchaseOrder.attachments')" for="purchase-order-attachments">Attachments</label>
            <select
              class="form-control"
              id="purchase-order-attachments"
              data-cy="attachments"
              multiple
              name="attachments"
              v-if="purchaseOrder.attachments !== undefined"
              v-model="purchaseOrder.attachments"
            >
              <option
                v-bind:value="getSelected(purchaseOrder.attachments, attachmentsOption)"
                v-for="attachmentsOption in attachments"
                :key="attachmentsOption.id"
              >
                {{ attachmentsOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrder.workOrders')" for="purchase-order-workOrders"
              >Work Orders</label
            >
            <select
              class="form-control"
              id="purchase-order-workOrders"
              data-cy="workOrders"
              name="workOrders"
              v-model="purchaseOrder.workOrders"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  purchaseOrder.workOrders && workOrdersOption.id === purchaseOrder.workOrders.id
                    ? purchaseOrder.workOrders
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
            :disabled="$v.purchaseOrder.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./purchase-order-update.component.ts"></script>
