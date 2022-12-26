<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.appintment.home.createOrEditLabel"
          data-cy="AppintmentCreateUpdateHeading"
          v-text="$t('workshopApp.appintment.home.createOrEditLabel')"
        >
          Create or edit a Appintment
        </h2>
        <div>
          <div class="form-group" v-if="appintment.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="appintment.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.appintment.appDate')" for="appintment-appDate">App Date</label>
            <div class="d-flex">
              <input
                id="appintment-appDate"
                data-cy="appDate"
                type="datetime-local"
                class="form-control"
                name="appDate"
                :class="{ valid: !$v.appintment.appDate.$invalid, invalid: $v.appintment.appDate.$invalid }"
                :value="convertDateTimeFromServer($v.appintment.appDate.$model)"
                @change="updateInstantField('appDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.appintment.notes')" for="appintment-notes">Notes</label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="appintment-notes"
              data-cy="notes"
              :class="{ valid: !$v.appintment.notes.$invalid, invalid: $v.appintment.notes.$invalid }"
              v-model="$v.appintment.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.appintment.client')" for="appintment-client">Client</label>
            <select class="form-control" id="appintment-client" data-cy="client" name="client" v-model="appintment.client">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="appintment.client && clientOption.id === appintment.client.id ? appintment.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.appintment.workOrders')" for="appintment-workOrders"
              >Work Orders</label
            >
            <select class="form-control" id="appintment-workOrders" data-cy="workOrders" name="workOrders" v-model="appintment.workOrders">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  appintment.workOrders && workOrdersOption.id === appintment.workOrders.id ? appintment.workOrders : workOrdersOption
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
            :disabled="$v.appintment.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./appintment-update.component.ts"></script>
