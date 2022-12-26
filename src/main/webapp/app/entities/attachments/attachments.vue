<template>
  <div>
    <h2 id="page-heading" data-cy="AttachmentsHeading">
      <span v-text="$t('workshopApp.attachments.home.title')" id="attachments-heading">Attachments</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.attachments.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'AttachmentsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-attachments"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.attachments.home.createLabel')"> Create a new Attachments </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && attachments && attachments.length === 0">
      <span v-text="$t('workshopApp.attachments.home.notFound')">No attachments found</span>
    </div>
    <div class="table-responsive" v-if="attachments && attachments.length > 0">
      <table class="table table-striped" aria-describedby="attachments">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('workshopApp.attachments.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('attach')">
              <span v-text="$t('workshopApp.attachments.attach')">Attach</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'attach'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="attachments in attachments" :key="attachments.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }">{{ attachments.id }}</router-link>
            </td>
            <td>{{ attachments.name }}</td>
            <td>
              <a
                v-if="attachments.attach"
                v-on:click="openFile(attachments.attachContentType, attachments.attach)"
                v-text="$t('entity.action.open')"
                >open</a
              >
              <span v-if="attachments.attach">{{ attachments.attachContentType }}, {{ byteSize(attachments.attach) }}</span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'AttachmentsEdit', params: { attachmentsId: attachments.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(attachments)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="workshopApp.attachments.delete.question" data-cy="attachmentsDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-attachments-heading" v-text="$t('workshopApp.attachments.delete.question', { id: removeId })">
          Are you sure you want to delete this Attachments?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-attachments"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeAttachments()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="attachments && attachments.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./attachments.component.ts"></script>
