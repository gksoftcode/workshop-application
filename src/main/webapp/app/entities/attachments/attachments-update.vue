<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="workshopApp.attachments.home.createOrEditLabel"
          data-cy="AttachmentsCreateUpdateHeading"
          v-text="$t('workshopApp.attachments.home.createOrEditLabel')"
        >
          Create or edit a Attachments
        </h2>
        <div>
          <div class="form-group" v-if="attachments.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="attachments.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachments.name')" for="attachments-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="attachments-name"
              data-cy="name"
              :class="{ valid: !$v.attachments.name.$invalid, invalid: $v.attachments.name.$invalid }"
              v-model="$v.attachments.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachments.attach')" for="attachments-attach">Attach</label>
            <div>
              <div v-if="attachments.attach" class="form-text text-danger clearfix">
                <a
                  class="pull-left"
                  v-on:click="openFile(attachments.attachContentType, attachments.attach)"
                  v-text="$t('entity.action.open')"
                  >open</a
                ><br />
                <span class="pull-left">{{ attachments.attachContentType }}, {{ byteSize(attachments.attach) }}</span>
                <button
                  type="button"
                  v-on:click="
                    attachments.attach = null;
                    attachments.attachContentType = null;
                  "
                  class="btn btn-secondary btn-xs pull-right"
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                </button>
              </div>
              <input
                type="file"
                ref="file_attach"
                id="file_attach"
                data-cy="attach"
                v-on:change="setFileData($event, attachments, 'attach', false)"
                v-text="$t('entity.action.addblob')"
              />
            </div>
            <input
              type="hidden"
              class="form-control"
              name="attach"
              id="attachments-attach"
              data-cy="attach"
              :class="{ valid: !$v.attachments.attach.$invalid, invalid: $v.attachments.attach.$invalid }"
              v-model="$v.attachments.attach.$model"
            />
            <input
              type="hidden"
              class="form-control"
              name="attachContentType"
              id="attachments-attachContentType"
              v-model="attachments.attachContentType"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('workshopApp.attachments.attachmentNotes')" for="attachments-attachmentNotes"
              >Attachment Notes</label
            >
            <select
              class="form-control"
              id="attachments-attachmentNotes"
              data-cy="attachmentNotes"
              name="attachmentNotes"
              v-model="attachments.attachmentNotes"
            >
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  attachments.attachmentNotes && attachmentNotesOption.id === attachments.attachmentNotes.id
                    ? attachments.attachmentNotes
                    : attachmentNotesOption
                "
                v-for="attachmentNotesOption in attachmentNotes"
                :key="attachmentNotesOption.id"
              >
                {{ attachmentNotesOption.id }}
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
            :disabled="$v.attachments.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./attachments-update.component.ts"></script>
