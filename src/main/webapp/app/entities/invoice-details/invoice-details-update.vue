<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.invoiceDetails.home.createOrEditLabel"
          data-cy="InvoiceDetailsCreateUpdateHeading"
          v-text="$t('workshopApp.invoiceDetails.home.createOrEditLabel')"
        >
          Create or edit a InvoiceDetails
        </h2>
        <div>
          <div class="form-group" v-if="invoiceDetails.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="invoiceDetails.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.itemNo')" for="invoice-details-itemNo">Item No</label>
            <input
              type="number"
              class="form-control"
              name="itemNo"
              id="invoice-details-itemNo"
              data-cy="itemNo"
              :class="{ valid: !$v.invoiceDetails.itemNo.$invalid, invalid: $v.invoiceDetails.itemNo.$invalid }"
              v-model.number="$v.invoiceDetails.itemNo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.itemDesc')" for="invoice-details-itemDesc"
              >Item Desc</label
            >
            <input
              type="text"
              class="form-control"
              name="itemDesc"
              id="invoice-details-itemDesc"
              data-cy="itemDesc"
              :class="{ valid: !$v.invoiceDetails.itemDesc.$invalid, invalid: $v.invoiceDetails.itemDesc.$invalid }"
              v-model="$v.invoiceDetails.itemDesc.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.itemPrice')" for="invoice-details-itemPrice"
              >Item Price</label
            >
            <input
              type="number"
              class="form-control"
              name="itemPrice"
              id="invoice-details-itemPrice"
              data-cy="itemPrice"
              :class="{ valid: !$v.invoiceDetails.itemPrice.$invalid, invalid: $v.invoiceDetails.itemPrice.$invalid }"
              v-model.number="$v.invoiceDetails.itemPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.itemQty')" for="invoice-details-itemQty"
              >Item Qty</label
            >
            <input
              type="number"
              class="form-control"
              name="itemQty"
              id="invoice-details-itemQty"
              data-cy="itemQty"
              :class="{ valid: !$v.invoiceDetails.itemQty.$invalid, invalid: $v.invoiceDetails.itemQty.$invalid }"
              v-model.number="$v.invoiceDetails.itemQty.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.discountType')" for="invoice-details-discountType"
              >Discount Type</label
            >
            <select
              class="form-control"
              name="discountType"
              :class="{ valid: !$v.invoiceDetails.discountType.$invalid, invalid: $v.invoiceDetails.discountType.$invalid }"
              v-model="$v.invoiceDetails.discountType.$model"
              id="invoice-details-discountType"
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
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.discount')" for="invoice-details-discount"
              >Discount</label
            >
            <input
              type="number"
              class="form-control"
              name="discount"
              id="invoice-details-discount"
              data-cy="discount"
              :class="{ valid: !$v.invoiceDetails.discount.$invalid, invalid: $v.invoiceDetails.discount.$invalid }"
              v-model.number="$v.invoiceDetails.discount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.totalAmount')" for="invoice-details-totalAmount"
              >Total Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="totalAmount"
              id="invoice-details-totalAmount"
              data-cy="totalAmount"
              :class="{ valid: !$v.invoiceDetails.totalAmount.$invalid, invalid: $v.invoiceDetails.totalAmount.$invalid }"
              v-model.number="$v.invoiceDetails.totalAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.invoiceDetails.invoice')" for="invoice-details-invoice"
              >Invoice</label
            >
            <select class="form-control" id="invoice-details-invoice" data-cy="invoice" name="invoice" v-model="invoiceDetails.invoice">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  invoiceDetails.invoice && invoiceOption.id === invoiceDetails.invoice.id ? invoiceDetails.invoice : invoiceOption
                "
                v-for="invoiceOption in invoices"
                :key="invoiceOption.id"
              >
                {{ invoiceOption.id }}
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
            :disabled="$v.invoiceDetails.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./invoice-details-update.component.ts"></script>
