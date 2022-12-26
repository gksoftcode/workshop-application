<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="supplier">
        <h2 class="jh-entity-heading" data-cy="supplierDetailsHeading">
          <span v-text="$t('workshopApp.supplier.detail.title')">Supplier</span> {{ supplier.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.supplier.name')">Name</span>
          </dt>
          <dd>
            <span>{{ supplier.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.supplier.email')">Email</span>
          </dt>
          <dd>
            <span>{{ supplier.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.supplier.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ supplier.phone }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.supplier.mobilePone')">Mobile Pone</span>
          </dt>
          <dd>
            <span>{{ supplier.mobilePone }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.supplier.location')">Location</span>
          </dt>
          <dd>
            <span v-for="(location, i) in supplier.locations" :key="location.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'LocationView', params: { locationId: location.id } }">{{ location.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="supplier.id" :to="{ name: 'SupplierEdit', params: { supplierId: supplier.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./supplier-details.component.ts"></script>
