<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="services">
        <h2 class="jh-entity-heading" data-cy="servicesDetailsHeading">
          <span v-text="$t('workshopApp.services.detail.title')">Services</span> {{ services.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.services.name')">Name</span>
          </dt>
          <dd>
            <span>{{ services.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.description')">Description</span>
          </dt>
          <dd>
            <span>{{ services.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.cost')">Cost</span>
          </dt>
          <dd>
            <span>{{ services.cost }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.price')">Price</span>
          </dt>
          <dd>
            <span>{{ services.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.discount')">Discount</span>
          </dt>
          <dd>
            <span>{{ services.discount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.notes')">Notes</span>
          </dt>
          <dd>
            <span>{{ services.notes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.discountType')">Discount Type</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.DiscountType.' + services.discountType)">{{ services.discountType }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.tax')">Tax</span>
          </dt>
          <dd>
            <div v-if="services.tax">
              <router-link :to="{ name: 'TaxView', params: { taxId: services.tax.id } }">{{ services.tax.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.serviceCategory')">Service Category</span>
          </dt>
          <dd>
            <div v-if="services.serviceCategory">
              <router-link :to="{ name: 'ServiceCategoryView', params: { serviceCategoryId: services.serviceCategory.id } }">{{
                services.serviceCategory.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.invoice')">Invoice</span>
          </dt>
          <dd>
            <div v-if="services.invoice">
              <router-link :to="{ name: 'InvoiceView', params: { invoiceId: services.invoice.id } }">{{ services.invoice.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.services.purchaseOrder')">Purchase Order</span>
          </dt>
          <dd>
            <div v-if="services.purchaseOrder">
              <router-link :to="{ name: 'PurchaseOrderView', params: { purchaseOrderId: services.purchaseOrder.id } }">{{
                services.purchaseOrder.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="services.id" :to="{ name: 'ServicesEdit', params: { servicesId: services.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./services-details.component.ts"></script>
