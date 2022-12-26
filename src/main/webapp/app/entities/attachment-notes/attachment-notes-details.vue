<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="attachmentNotes">
        <h2 class="jh-entity-heading" data-cy="attachmentNotesDetailsHeading">
          <span v-text="$t('workshopApp.attachmentNotes.detail.title')">AttachmentNotes</span> {{ attachmentNotes.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.isShared')">Is Shared</span>
          </dt>
          <dd>
            <span>{{ attachmentNotes.isShared }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.actionDate')">Action Date</span>
          </dt>
          <dd>
            <span v-if="attachmentNotes.actionDate">{{ $d(Date.parse(attachmentNotes.actionDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.note')">Note</span>
          </dt>
          <dd>
            <span>{{ attachmentNotes.note }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.status')">Status</span>
          </dt>
          <dd>
            <div v-if="attachmentNotes.status">
              <router-link :to="{ name: 'StatusView', params: { statusId: attachmentNotes.status.id } }">{{
                attachmentNotes.status.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.action')">Action</span>
          </dt>
          <dd>
            <div v-if="attachmentNotes.action">
              <router-link :to="{ name: 'ActionView', params: { actionId: attachmentNotes.action.id } }">{{
                attachmentNotes.action.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.attachments')">Attachments</span>
          </dt>
          <dd>
            <span v-for="(attachments, i) in attachmentNotes.attachments" :key="attachments.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }">{{ attachments.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.attachmentNotes.workOrders')">Work Orders</span>
          </dt>
          <dd>
            <div v-if="attachmentNotes.workOrders">
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: attachmentNotes.workOrders.id } }">{{
                attachmentNotes.workOrders.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="attachmentNotes.id"
          :to="{ name: 'AttachmentNotesEdit', params: { attachmentNotesId: attachmentNotes.id } }"
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

<script lang="ts" src="./attachment-notes-details.component.ts"></script>
