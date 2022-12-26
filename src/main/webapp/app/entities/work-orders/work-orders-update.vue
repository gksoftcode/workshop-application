<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.workOrders.home.createOrEditLabel"
          data-cy="WorkOrdersCreateUpdateHeading"
          v-text="$t('workshopApp.workOrders.home.createOrEditLabel')"
        >
          Create or edit a WorkOrders
        </h2>
        <div>
          <div class="form-group" v-if="workOrders.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="workOrders.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.title')" for="work-orders-title">Title</label>
            <input
              type="text"
              class="form-control"
              name="title"
              id="work-orders-title"
              data-cy="title"
              :class="{ valid: !$v.workOrders.title.$invalid, invalid: $v.workOrders.title.$invalid }"
              v-model="$v.workOrders.title.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.description')" for="work-orders-description"
              >Description</label
            >
            <input
              type="text"
              class="form-control"
              name="description"
              id="work-orders-description"
              data-cy="description"
              :class="{ valid: !$v.workOrders.description.$invalid, invalid: $v.workOrders.description.$invalid }"
              v-model="$v.workOrders.description.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.startDate')" for="work-orders-startDate">Start Date</label>
            <div class="d-flex">
              <input
                id="work-orders-startDate"
                data-cy="startDate"
                type="datetime-local"
                class="form-control"
                name="startDate"
                :class="{ valid: !$v.workOrders.startDate.$invalid, invalid: $v.workOrders.startDate.$invalid }"
                :value="convertDateTimeFromServer($v.workOrders.startDate.$model)"
                @change="updateInstantField('startDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.endDate')" for="work-orders-endDate">End Date</label>
            <div class="d-flex">
              <input
                id="work-orders-endDate"
                data-cy="endDate"
                type="datetime-local"
                class="form-control"
                name="endDate"
                :class="{ valid: !$v.workOrders.endDate.$invalid, invalid: $v.workOrders.endDate.$invalid }"
                :value="convertDateTimeFromServer($v.workOrders.endDate.$model)"
                @change="updateInstantField('endDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.budget')" for="work-orders-budget">Budget</label>
            <input
              type="number"
              class="form-control"
              name="budget"
              id="work-orders-budget"
              data-cy="budget"
              :class="{ valid: !$v.workOrders.budget.$invalid, invalid: $v.workOrders.budget.$invalid }"
              v-model.number="$v.workOrders.budget.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.itemSerial')" for="work-orders-itemSerial"
              >Item Serial</label
            >
            <input
              type="text"
              class="form-control"
              name="itemSerial"
              id="work-orders-itemSerial"
              data-cy="itemSerial"
              :class="{ valid: !$v.workOrders.itemSerial.$invalid, invalid: $v.workOrders.itemSerial.$invalid }"
              v-model="$v.workOrders.itemSerial.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.isWaranty')" for="work-orders-isWaranty">Is Waranty</label>
            <input
              type="checkbox"
              class="form-check"
              name="isWaranty"
              id="work-orders-isWaranty"
              data-cy="isWaranty"
              :class="{ valid: !$v.workOrders.isWaranty.$invalid, invalid: $v.workOrders.isWaranty.$invalid }"
              v-model="$v.workOrders.isWaranty.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.notes')" for="work-orders-notes">Notes</label>
            <input
              type="text"
              class="form-control"
              name="notes"
              id="work-orders-notes"
              data-cy="notes"
              :class="{ valid: !$v.workOrders.notes.$invalid, invalid: $v.workOrders.notes.$invalid }"
              v-model="$v.workOrders.notes.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.status')" for="work-orders-status">Status</label>
            <select class="form-control" id="work-orders-status" data-cy="status" name="status" v-model="workOrders.status">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="workOrders.status && statusOption.id === workOrders.status.id ? workOrders.status : statusOption"
                v-for="statusOption in statuses"
                :key="statusOption.id"
              >
                {{ statusOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.client')" for="work-orders-client">Client</label>
            <select class="form-control" id="work-orders-client" data-cy="client" name="client" v-model="workOrders.client">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="workOrders.client && clientOption.id === workOrders.client.id ? workOrders.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.fullName }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.itemModels')" for="work-orders-itemModels"
              >Item Models</label
            >
            <select class="form-control" id="work-orders-itemModels" data-cy="itemModels" name="itemModels" v-model="workOrders.itemModels">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  workOrders.itemModels && itemModelsOption.id === workOrders.itemModels.id ? workOrders.itemModels : itemModelsOption
                "
                v-for="itemModelsOption in itemModels"
                :key="itemModelsOption.id"
              >
                {{ itemModelsOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.workOrders.itemBrand')" for="work-orders-itemBrand">Item Brand</label>
            <select class="form-control" id="work-orders-itemBrand" data-cy="itemBrand" name="itemBrand" v-model="workOrders.itemBrand">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  workOrders.itemBrand && itemBrandOption.id === workOrders.itemBrand.id ? workOrders.itemBrand : itemBrandOption
                "
                v-for="itemBrandOption in itemBrands"
                :key="itemBrandOption.id"
              >
                {{ itemBrandOption.name }}
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
            :disabled="$v.workOrders.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./work-orders-update.component.ts"></script>
