<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="serviceCategory">
        <h2 class="jh-entity-heading" data-cy="serviceCategoryDetailsHeading">
          <span v-text="$t('workshopApp.serviceCategory.detail.title')">ServiceCategory</span> {{ serviceCategory.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.serviceCategory.categoryName')">Category Name</span>
          </dt>
          <dd>
            <span>{{ serviceCategory.categoryName }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="serviceCategory.id"
          :to="{ name: 'ServiceCategoryEdit', params: { serviceCategoryId: serviceCategory.id } }"
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

<script lang="ts" src="./service-category-details.component.ts"></script>
