<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="purchaseOrderDetails">
        <h2 class="jh-entity-heading" data-cy="purchaseOrderDetailsDetailsHeading">
          <span v-text="$t('workshopApp.purchaseOrderDetails.detail.title')">PurchaseOrderDetails</span> {{ purchaseOrderDetails.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.itemNo')">Item No</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.itemNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.itemDesc')">Item Desc</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.itemDesc }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.itemPrice')">Item Price</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.itemPrice }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.itemQty')">Item Qty</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.itemQty }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.discountType')">Discount Type</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.DiscountType.' + purchaseOrderDetails.discountType)">{{
              purchaseOrderDetails.discountType
            }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.discount')">Discount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.discount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.totalAmount')">Total Amount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrderDetails.totalAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.tax')">Tax</span>
          </dt>
          <dd>
            <div v-if="purchaseOrderDetails.tax">
              <router-link :to="{ name: 'TaxView', params: { taxId: purchaseOrderDetails.tax.id } }">{{
                purchaseOrderDetails.tax.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrderDetails.purchaseOrder')">Purchase Order</span>
          </dt>
          <dd>
            <div v-if="purchaseOrderDetails.purchaseOrder">
              <router-link :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: purchaseOrderDetails.purchaseOrder.id } }">{{
                purchaseOrderDetails.purchaseOrder.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="purchaseOrderDetails.id"
          :to="{ name: 'PurchaseOrderDetailsEdit', params: { purchaseOrderDetailsId: purchaseOrderDetails.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./purchase-order-details-details.component.ts"></script>
