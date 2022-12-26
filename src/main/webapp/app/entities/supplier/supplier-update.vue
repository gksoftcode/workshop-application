<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.supplier.home.createOrEditLabel"
          data-cy="SupplierCreateUpdateHeading"
          v-text="$t('workshopApp.supplier.home.createOrEditLabel')"
        >
          Create or edit a Supplier
        </h2>
        <div>
          <div class="form-group" v-if="supplier.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="supplier.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.supplier.name')" for="supplier-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="supplier-name"
              data-cy="name"
              :class="{ valid: !$v.supplier.name.$invalid, invalid: $v.supplier.name.$invalid }"
              v-model="$v.supplier.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.supplier.email')" for="supplier-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="supplier-email"
              data-cy="email"
              :class="{ valid: !$v.supplier.email.$invalid, invalid: $v.supplier.email.$invalid }"
              v-model="$v.supplier.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.supplier.phone')" for="supplier-phone">Phone</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="supplier-phone"
              data-cy="phone"
              :class="{ valid: !$v.supplier.phone.$invalid, invalid: $v.supplier.phone.$invalid }"
              v-model="$v.supplier.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.supplier.mobilePone')" for="supplier-mobilePone">Mobile Pone</label>
            <input
              type="text"
              class="form-control"
              name="mobilePone"
              id="supplier-mobilePone"
              data-cy="mobilePone"
              :class="{ valid: !$v.supplier.mobilePone.$invalid, invalid: $v.supplier.mobilePone.$invalid }"
              v-model="$v.supplier.mobilePone.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('workshopApp.supplier.location')" for="supplier-location">Location</label>
            <select
              class="form-control"
              id="supplier-locations"
              data-cy="location"
              multiple
              name="location"
              v-if="supplier.locations !== undefined"
              v-model="supplier.locations"
            >
              <option
                v-bind:value="getSelected(supplier.locations, locationOption)"
                v-for="locationOption in locations"
                :key="locationOption.id"
              >
                {{ locationOption.name }}
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
            :disabled="$v.supplier.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./supplier-update.component.ts"></script>
