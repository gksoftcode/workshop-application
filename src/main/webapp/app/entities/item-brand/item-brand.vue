<template>
  <div>
    <h2 id="page-heading" data-cy="ItemBrandHeading">
      <span v-text="$t('workshopApp.itemBrand.home.title')" id="item-brand-heading">Item Brands</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('workshopApp.itemBrand.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ItemBrandCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-item-brand"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('workshopApp.itemBrand.home.createLabel')"> Create a new Item Brand </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && itemBrands && itemBrands.length === 0">
      <span v-text="$t('workshopApp.itemBrand.home.notFound')">No itemBrands found</span>
    </div>
    <div class="table-responsive" v-if="itemBrands && itemBrands.length > 0">
      <table class="table table-striped" aria-describedby="itemBrands">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('workshopApp.itemBrand.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="itemBrand in itemBrands" :key="itemBrand.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ItemBrandView', params: { itemBrandId: itemBrand.id } }">{{ itemBrand.id }}</router-link>
            </td>
            <td>{{ itemBrand.name }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ItemBrandView', params: { itemBrandId: itemBrand.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ItemBrandEdit', params: { itemBrandId: itemBrand.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(itemBrand)"
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
        ><span id="workshopApp.itemBrand.delete.question" data-cy="itemBrandDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-itemBrand-heading" v-text="$t('workshopApp.itemBrand.delete.question', { id: removeId })">
          Are you sure you want to delete this Item Brand?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-itemBrand"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeItemBrand()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="itemBrands && itemBrands.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./item-brand.component.ts"></script>
