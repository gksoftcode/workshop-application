<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.attachmentNotes.home.createOrEditLabel"
          data-cy="AttachmentNotesCreateUpdateHeading"
          v-text="$t('workshopApp.attachmentNotes.home.createOrEditLabel')"
        >
          Create or edit a AttachmentNotes
        </h2>
        <div>
          <div class="form-group" v-if="attachmentNotes.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attachmentNotes.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.isShared')" for="attachment-notes-isShared"
              >Is Shared</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="isShared"
              id="attachment-notes-isShared"
              data-cy="isShared"
              :class="{ valid: !$v.attachmentNotes.isShared.$invalid, invalid: $v.attachmentNotes.isShared.$invalid }"
              v-model="$v.attachmentNotes.isShared.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.actionDate')" for="attachment-notes-actionDate"
              >Action Date</label
            >
            <div class="d-flex">
              <input
                id="attachment-notes-actionDate"
                data-cy="actionDate"
                type="datetime-local"
                class="form-control"
                name="actionDate"
                :class="{ valid: !$v.attachmentNotes.actionDate.$invalid, invalid: $v.attachmentNotes.actionDate.$invalid }"
                :value="convertDateTimeFromServer($v.attachmentNotes.actionDate.$model)"
                @change="updateInstantField('actionDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.note')" for="attachment-notes-note">Note</label>
            <input
              type="text"
              class="form-control"
              name="note"
              id="attachment-notes-note"
              data-cy="note"
              :class="{ valid: !$v.attachmentNotes.note.$invalid, invalid: $v.attachmentNotes.note.$invalid }"
              v-model="$v.attachmentNotes.note.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.status')" for="attachment-notes-status">Status</label>
            <select class="form-control" id="attachment-notes-status" data-cy="status" name="status" v-model="attachmentNotes.status">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  attachmentNotes.status && statusOption.id === attachmentNotes.status.id ? attachmentNotes.status : statusOption
                "
                v-for="statusOption in statuses"
                :key="statusOption.id"
              >
                {{ statusOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.action')" for="attachment-notes-action">Action</label>
            <select class="form-control" id="attachment-notes-action" data-cy="action" name="action" v-model="attachmentNotes.action">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  attachmentNotes.action && actionOption.id === attachmentNotes.action.id ? attachmentNotes.action : actionOption
                "
                v-for="actionOption in actions"
                :key="actionOption.id"
              >
                {{ actionOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachmentNotes.workOrders')" for="attachment-notes-workOrders"
              >Work Orders</label
            >
            <select
              class="form-control"
              id="attachment-notes-workOrders"
              data-cy="workOrders"
              name="workOrders"
              v-model="attachmentNotes.workOrders"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  attachmentNotes.workOrders && workOrdersOption.id === attachmentNotes.workOrders.id
                    ? attachmentNotes.workOrders
                    : workOrdersOption
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
            :disabled="$v.attachmentNotes.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./attachment-notes-update.component.ts"></script>
