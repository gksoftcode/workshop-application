<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="attachments">
        <h2 class="jh-entity-heading" data-cy="attachmentsDetailsHeading">
          <span v-text="$t('workshopApp.attachments.detail.title')">Attachments</span> {{ attachments.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.attachments.name')">Name</span>
          </dt>
          <dd>
            <span>{{ attachments.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachments.attach')">Attach</span>
          </dt>
          <dd>
            <div v-if="attachments.attach">
              <a v-on:click="openFile(attachments.attachContentType, attachments.attach)" v-text="$t('entity.action.open')">open</a>
              {{ attachments.attachContentType }}, {{ byteSize(attachments.attach) }}
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachments.attachmentNotes')">Attachment Notes</span>
          </dt>
          <dd>
            <div v-if="attachments.attachmentNotes">
              <router-link :to="{ name: 'AttachmentNotesView', params: { attachmentNotesId: attachments.attachmentNotes.id } }">{{
                attachments.attachmentNotes.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="attachments.id"
          :to="{ name: 'AttachmentsEdit', params: { attachmentsId: attachments.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./attachments-details.component.ts"></script>
