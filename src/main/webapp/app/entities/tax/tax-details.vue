<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="tax">
        <h2 class="jh-entity-heading" data-cy="taxDetailsHeading">
          <span v-text="$t('workshopApp.tax.detail.title')">Tax</span> {{ tax.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.tax.name')">Name</span>
          </dt>
          <dd>
            <span>{{ tax.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.tax.taxValue')">Tax Value</span>
          </dt>
          <dd>
            <span>{{ tax.taxValue }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="tax.id" :to="{ name: 'TaxEdit', params: { taxId: tax.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./tax-details.component.ts"></script>
