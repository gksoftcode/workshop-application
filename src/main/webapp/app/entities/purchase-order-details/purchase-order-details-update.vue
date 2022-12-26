<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.purchaseOrderDetails.home.createOrEditLabel"
          data-cy="PurchaseOrderDetailsCreateUpdateHeading"
          v-text="$t('workshopApp.purchaseOrderDetails.home.createOrEditLabel')"
        >
          Create or edit a PurchaseOrderDetails
        </h2>
        <div>
          <div class="form-group" v-if="purchaseOrderDetails.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="purchaseOrderDetails.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrderDetails.itemNo')" for="purchase-order-details-itemNo"
              >Item No</label
            >
            <input
              type="number"
              class="form-control"
              name="itemNo"
              id="purchase-order-details-itemNo"
              data-cy="itemNo"
              :class="{ valid: !$v.purchaseOrderDetails.itemNo.$invalid, invalid: $v.purchaseOrderDetails.itemNo.$invalid }"
              v-model.number="$v.purchaseOrderDetails.itemNo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrderDetails.itemDesc')" for="purchase-order-details-itemDesc"
              >Item Desc</label
            >
            <input
              type="text"
              class="form-control"
              name="itemDesc"
              id="purchase-order-details-itemDesc"
              data-cy="itemDesc"
              :class="{ valid: !$v.purchaseOrderDetails.itemDesc.$invalid, invalid: $v.purchaseOrderDetails.itemDesc.$invalid }"
              v-model="$v.purchaseOrderDetails.itemDesc.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('workshopApp.purchaseOrderDetails.itemPrice')"
              for="purchase-order-details-itemPrice"
              >Item Price</label
            >
            <input
              type="number"
              class="form-control"
              name="itemPrice"
              id="purchase-order-details-itemPrice"
              data-cy="itemPrice"
              :class="{ valid: !$v.purchaseOrderDetails.itemPrice.$invalid, invalid: $v.purchaseOrderDetails.itemPrice.$invalid }"
              v-model.number="$v.purchaseOrderDetails.itemPrice.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrderDetails.itemQty')" for="purchase-order-details-itemQty"
              >Item Qty</label
            >
            <input
              type="number"
              class="form-control"
              name="itemQty"
              id="purchase-order-details-itemQty"
              data-cy="itemQty"
              :class="{ valid: !$v.purchaseOrderDetails.itemQty.$invalid, invalid: $v.purchaseOrderDetails.itemQty.$invalid }"
              v-model.number="$v.purchaseOrderDetails.itemQty.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('workshopApp.purchaseOrderDetails.discountType')"
              for="purchase-order-details-discountType"
              >Discount Type</label
            >
            <select
              class="form-control"
              name="discountType"
              :class="{ valid: !$v.purchaseOrderDetails.discountType.$invalid, invalid: $v.purchaseOrderDetails.discountType.$invalid }"
              v-model="$v.purchaseOrderDetails.discountType.$model"
              id="purchase-order-details-discountType"
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
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrderDetails.discount')" for="purchase-order-details-discount"
              >Discount</label
            >
            <input
              type="number"
              class="form-control"
              name="discount"
              id="purchase-order-details-discount"
              data-cy="discount"
              :class="{ valid: !$v.purchaseOrderDetails.discount.$invalid, invalid: $v.purchaseOrderDetails.discount.$invalid }"
              v-model.number="$v.purchaseOrderDetails.discount.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('workshopApp.purchaseOrderDetails.totalAmount')"
              for="purchase-order-details-totalAmount"
              >Total Amount</label
            >
            <input
              type="number"
              class="form-control"
              name="totalAmount"
              id="purchase-order-details-totalAmount"
              data-cy="totalAmount"
              :class="{ valid: !$v.purchaseOrderDetails.totalAmount.$invalid, invalid: $v.purchaseOrderDetails.totalAmount.$invalid }"
              v-model.number="$v.purchaseOrderDetails.totalAmount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.purchaseOrderDetails.tax')" for="purchase-order-details-tax"
              >Tax</label
            >
            <select class="form-control" id="purchase-order-details-tax" data-cy="tax" name="tax" v-model="purchaseOrderDetails.tax">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  purchaseOrderDetails.tax && taxOption.id === purchaseOrderDetails.tax.id ? purchaseOrderDetails.tax : taxOption
                "
                v-for="taxOption in taxes"
                :key="taxOption.id"
              >
                {{ taxOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="$t('workshopApp.purchaseOrderDetails.purchaseOrder')"
              for="purchase-order-details-purchaseOrder"
              >Purchase Order</label
            >
            <select
              class="form-control"
              id="purchase-order-details-purchaseOrder"
              data-cy="purchaseOrder"
              name="purchaseOrder"
              v-model="purchaseOrderDetails.purchaseOrder"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  purchaseOrderDetails.purchaseOrder && purchaseOrderOption.id === purchaseOrderDetails.purchaseOrder.id
                    ? purchaseOrderDetails.purchaseOrder
                    : purchaseOrderOption
                "
                v-for="purchaseOrderOption in purchaseOrders"
                :key="purchaseOrderOption.id"
              >
                {{ purchaseOrderOption.id }}
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
            :disabled="$v.purchaseOrderDetails.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./purchase-order-details-update.component.ts"></script>
