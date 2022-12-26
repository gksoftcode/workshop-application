<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="invoiceDetails">
        <h2 class="jh-entity-heading" data-cy="invoiceDetailsDetailsHeading">
          <span v-text="$t('workshopApp.invoiceDetails.detail.title')">InvoiceDetails</span> {{ invoiceDetails.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.itemNo')">Item No</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.itemNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.itemDesc')">Item Desc</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.itemDesc }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.itemPrice')">Item Price</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.itemPrice }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.itemQty')">Item Qty</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.itemQty }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.discountType')">Discount Type</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.DiscountType.' + invoiceDetails.discountType)">{{ invoiceDetails.discountType }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.discount')">Discount</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.discount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.totalAmount')">Total Amount</span>
          </dt>
          <dd>
            <span>{{ invoiceDetails.totalAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoiceDetails.invoice')">Invoice</span>
          </dt>
          <dd>
            <div v-if="invoiceDetails.invoice">
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: invoiceDetails.invoice.id } }">{{
                invoiceDetails.invoice.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="invoiceDetails.id"
          :to="{ name: 'InvoiceDetailsEdit', params: { invoiceDetailsId: invoiceDetails.id } }"
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

<script lang="ts" src="./invoice-details-details.component.ts"></script>
