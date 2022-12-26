<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.action.home.createOrEditLabel"
          data-cy="ActionCreateUpdateHeading"
          v-text="$t('workshopApp.action.home.createOrEditLabel')"
        >
          Create or edit a Action
        </h2>
        <div>
          <div class="form-group" v-if="action.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="action.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.action.name')" for="action-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="action-name"
              data-cy="name"
              :class="{ valid: !$v.action.name.$invalid, invalid: $v.action.name.$invalid }"
              v-model="$v.action.name.$model"
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
            :disabled="$v.action.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./action-update.component.ts"></script>
