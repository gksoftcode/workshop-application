<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.services.home.createOrEditLabel"
          data-cy="ServicesCreateUpdateHeading"
          v-text="$t('workshopApp.services.home.createOrEditLabel')"
        >
          Create or edit a Services
        </h2>
        <div>
          <div class="form-group" v-if="services.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="services.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.name')" for="services-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="services-name"
              data-cy="name"
              :class="{ valid: !$v.services.name.$invalid, invalid: $v.services.name.$invalid }"
              v-model="$v.services.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.description')" for="services-description">Description</label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="services-description"
              data-cy="description"
              :class="{ valid: !$v.services.description.$invalid, invalid: $v.services.description.$invalid }"
              v-model="$v.services.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.cost')" for="services-cost">Cost</label>
            <input
              type="number"
              class="form-control"
              name="cost"
              id="services-cost"
              data-cy="cost"
              :class="{ valid: !$v.services.cost.$invalid, invalid: $v.services.cost.$invalid }"
              v-model.number="$v.services.cost.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.price')" for="services-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="services-price"
              data-cy="price"
              :class="{ valid: !$v.services.price.$invalid, invalid: $v.services.price.$invalid }"
              v-model.number="$v.services.price.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.discount')" for="services-discount">Discount</label>
            <input
              type="number"
              class="form-control"
              name="discount"
              id="services-discount"
              data-cy="discount"
              :class="{ valid: !$v.services.discount.$invalid, invalid: $v.services.discount.$invalid }"
              v-model.number="$v.services.discount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.notes')" for="services-notes">Notes</label>
            <input
              type="number"
              class="form-control"
              name="notes"
              id="services-notes"
              data-cy="notes"
              :class="{ valid: !$v.services.notes.$invalid, invalid: $v.services.notes.$invalid }"
              v-model.number="$v.services.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.discountType')" for="services-discountType"
              >Discount Type</label
            >
            <select
              class="form-control"
              name="discountType"
              :class="{ valid: !$v.services.discountType.$invalid, invalid: $v.services.discountType.$invalid }"
              v-model="$v.services.discountType.$model"
              id="services-discountType"
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
            <label class="form-control-label" v-text="$t('workshopApp.services.tax')" for="services-tax">Tax</label>
            <select class="form-control" id="services-tax" data-cy="tax" name="tax" v-model="services.tax">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="services.tax && taxOption.id === services.tax.id ? services.tax : taxOption"
                v-for="taxOption in taxes"
                :key="taxOption.id"
              >
                {{ taxOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.serviceCategory')" for="services-serviceCategory"
              >Service Category</label
            >
            <select
              class="form-control"
              id="services-serviceCategory"
              data-cy="serviceCategory"
              name="serviceCategory"
              v-model="services.serviceCategory"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  services.serviceCategory && serviceCategoryOption.id === services.serviceCategory.id
                    ? services.serviceCategory
                    : serviceCategoryOption
                "
                v-for="serviceCategoryOption in serviceCategories"
                :key="serviceCategoryOption.id"
              >
                {{ serviceCategoryOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.invoice')" for="services-invoice">Invoice</label>
            <select class="form-control" id="services-invoice" data-cy="invoice" name="invoice" v-model="services.invoice">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="services.invoice && invoiceOption.id === services.invoice.id ? services.invoice : invoiceOption"
                v-for="invoiceOption in invoices"
                :key="invoiceOption.id"
              >
                {{ invoiceOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.services.purchaseOrder')" for="services-purchaseOrder"
              >Purchase Order</label
            >
            <select
              class="form-control"
              id="services-purchaseOrder"
              data-cy="purchaseOrder"
              name="purchaseOrder"
              v-model="services.purchaseOrder"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  services.purchaseOrder && purchaseOrderOption.id === services.purchaseOrder.id
                    ? services.purchaseOrder
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
            :disabled="$v.services.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./services-update.component.ts"></script>
