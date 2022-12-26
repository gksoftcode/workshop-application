<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="client">
        <h2 class="jh-entity-heading" data-cy="clientDetailsHeading">
          <span v-text="$t('workshopApp.client.detail.title')">Client</span> {{ client.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.client.fullName')">Full Name</span>
          </dt>
          <dd>
            <span>{{ client.fullName }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.client.mobileNo')">Mobile No</span>
          </dt>
          <dd>
            <span>{{ client.mobileNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.client.email')">Email</span>
          </dt>
          <dd>
            <span>{{ client.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.client.phoneNumber')">Phone Number</span>
          </dt>
          <dd>
            <span>{{ client.phoneNumber }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.client.location')">Location</span>
          </dt>
          <dd>
            <span v-for="(location, i) in client.locations" :key="location.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'LocationView', params: { locationId: location.id } }">{{ location.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="client.id" :to="{ name: 'ClientEdit', params: { clientId: client.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./client-details.component.ts"></script>
