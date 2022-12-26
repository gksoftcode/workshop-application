<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="status">
        <h2 class="jh-entity-heading" data-cy="statusDetailsHeading">
          <span v-text="$t('workshopApp.status.detail.title')">Status</span> {{ status.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.status.name')">Name</span>
          </dt>
          <dd>
            <span>{{ status.name }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="status.id" :to="{ name: 'StatusEdit', params: { statusId: status.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./status-details.component.ts"></script>
