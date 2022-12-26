<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appintment">
        <h2 class="jh-entity-heading" data-cy="appintmentDetailsHeading">
          <span v-text="$t('workshopApp.appintment.detail.title')">Appintment</span> {{ appintment.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.appintment.appDate')">App Date</span>
          </dt>
          <dd>
            <span v-if="appintment.appDate">{{ $d(Date.parse(appintment.appDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.appintment.notes')">Notes</span>
          </dt>
          <dd>
            <span>{{ appintment.notes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.appintment.client')">Client</span>
          </dt>
          <dd>
            <div v-if="appintment.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: appintment.client.id } }">{{ appintment.client.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.appintment.workOrders')">Work Orders</span>
          </dt>
          <dd>
            <div v-if="appintment.workOrders">
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: appintment.workOrders.id } }">{{
                appintment.workOrders.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="appintment.id"
          :to="{ name: 'AppintmentEdit', params: { appintmentId: appintment.id } }"
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

<script lang="ts" src="./appintment-details.component.ts"></script>
