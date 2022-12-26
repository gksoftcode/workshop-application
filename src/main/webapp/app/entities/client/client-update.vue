<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.client.home.createOrEditLabel"
          data-cy="ClientCreateUpdateHeading"
          v-text="$t('workshopApp.client.home.createOrEditLabel')"
        >
          Create or edit a Client
        </h2>
        <div>
          <div class="form-group" v-if="client.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="client.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.client.fullName')" for="client-fullName">Full Name</label>
            <input
              type="text"
              class="form-control"
              name="fullName"
              id="client-fullName"
              data-cy="fullName"
              :class="{ valid: !$v.client.fullName.$invalid, invalid: $v.client.fullName.$invalid }"
              v-model="$v.client.fullName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.client.mobileNo')" for="client-mobileNo">Mobile No</label>
            <input
              type="text"
              class="form-control"
              name="mobileNo"
              id="client-mobileNo"
              data-cy="mobileNo"
              :class="{ valid: !$v.client.mobileNo.$invalid, invalid: $v.client.mobileNo.$invalid }"
              v-model="$v.client.mobileNo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.client.email')" for="client-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="client-email"
              data-cy="email"
              :class="{ valid: !$v.client.email.$invalid, invalid: $v.client.email.$invalid }"
              v-model="$v.client.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.client.phoneNumber')" for="client-phoneNumber">Phone Number</label>
            <input
              type="text"
              class="form-control"
              name="phoneNumber"
              id="client-phoneNumber"
              data-cy="phoneNumber"
              :class="{ valid: !$v.client.phoneNumber.$invalid, invalid: $v.client.phoneNumber.$invalid }"
              v-model="$v.client.phoneNumber.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('workshopApp.client.location')" for="client-location">Location</label>
            <select
              class="form-control"
              id="client-locations"
              data-cy="location"
              multiple
              name="location"
              v-if="client.locations !== undefined"
              v-model="client.locations"
            >
              <option
                v-bind:value="getSelected(client.locations, locationOption)"
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
            :disabled="$v.client.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./client-update.component.ts"></script>
