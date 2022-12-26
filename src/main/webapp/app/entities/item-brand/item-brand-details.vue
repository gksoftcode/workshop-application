<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="itemBrand">
        <h2 class="jh-entity-heading" data-cy="itemBrandDetailsHeading">
          <span v-text="$t('workshopApp.itemBrand.detail.title')">ItemBrand</span> {{ itemBrand.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.itemBrand.name')">Name</span>
          </dt>
          <dd>
            <span>{{ itemBrand.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="itemBrand.id"
          :to="{ name: 'ItemBrandEdit', params: { itemBrandId: itemBrand.id } }"
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

<script lang="ts" src="./item-brand-details.component.ts"></script>
