<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.serviceCategory.home.createOrEditLabel"
          data-cy="ServiceCategoryCreateUpdateHeading"
          v-text="$t('workshopApp.serviceCategory.home.createOrEditLabel')"
        >
          Create or edit a ServiceCategory
        </h2>
        <div>
          <div class="form-group" v-if="serviceCategory.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="serviceCategory.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.serviceCategory.categoryName')" for="service-category-categoryName"
              >Category Name</label
            >
            <input
              type="text"
              class="form-control"
              name="categoryName"
              id="service-category-categoryName"
              data-cy="categoryName"
              :class="{ valid: !$v.serviceCategory.categoryName.$invalid, invalid: $v.serviceCategory.categoryName.$invalid }"
              v-model="$v.serviceCategory.categoryName.$model"
            />
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
            :disabled="$v.serviceCategory.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./service-category-update.component.ts"></script>
