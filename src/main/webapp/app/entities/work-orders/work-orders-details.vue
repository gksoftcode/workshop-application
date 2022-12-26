<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="workOrders">
        <h2 class="jh-entity-heading" data-cy="workOrdersDetailsHeading">
          <span v-text="$t('workshopApp.workOrders.detail.title')">WorkOrders</span> {{ workOrders.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.workOrders.title')">Title</span>
          </dt>
          <dd>
            <span>{{ workOrders.title }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.description')">Description</span>
          </dt>
          <dd>
            <span>{{ workOrders.description }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.startDate')">Start Date</span>
          </dt>
          <dd>
            <span v-if="workOrders.startDate">{{ $d(Date.parse(workOrders.startDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.endDate')">End Date</span>
          </dt>
          <dd>
            <span v-if="workOrders.endDate">{{ $d(Date.parse(workOrders.endDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.budget')">Budget</span>
          </dt>
          <dd>
            <span>{{ workOrders.budget }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.itemSerial')">Item Serial</span>
          </dt>
          <dd>
            <span>{{ workOrders.itemSerial }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.isWaranty')">Is Waranty</span>
          </dt>
          <dd>
            <span>{{ workOrders.isWaranty }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.notes')">Notes</span>
          </dt>
          <dd>
            <span>{{ workOrders.notes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.status')">Status</span>
          </dt>
          <dd>
            <div v-if="workOrders.status">
              <router-link :to="{ name: 'StatusView', params: { statusId: workOrders.status.id } }">{{
                workOrders.status.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.client')">Client</span>
          </dt>
          <dd>
            <div v-if="workOrders.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: workOrders.client.id } }">{{
                workOrders.client.fullName
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.itemModels')">Item Models</span>
          </dt>
          <dd>
            <div v-if="workOrders.itemModels">
              <router-link :to="{ name: 'ItemModelsView', params: { itemModelsId: workOrders.itemModels.id } }">{{
                workOrders.itemModels.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.workOrders.itemBrand')">Item Brand</span>
          </dt>
          <dd>
            <div v-if="workOrders.itemBrand">
              <router-link :to="{ name: 'ItemBrandView', params: { itemBrandId: workOrders.itemBrand.id } }">{{
                workOrders.itemBrand.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="workOrders.id"
          :to="{ name: 'WorkOrdersEdit', params: { workOrdersId: workOrders.id } }"
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

<script lang="ts" src="./work-orders-details.component.ts"></script>
