<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="paymentCredit">
        <h2 class="jh-entity-heading" data-cy="paymentCreditDetailsHeading">
          <span v-text="$t('workshopApp.paymentCredit.detail.title')">PaymentCredit</span> {{ paymentCredit.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.paymentMethod')">Payment Method</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentMethod.' + paymentCredit.paymentMethod)">{{ paymentCredit.paymentMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.paymentRef')">Payment Ref</span>
          </dt>
          <dd>
            <span>{{ paymentCredit.paymentRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ paymentCredit.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.addDate')">Add Date</span>
          </dt>
          <dd>
            <span v-if="paymentCredit.addDate">{{ $d(Date.parse(paymentCredit.addDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.paymentStatus')">Payment Status</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentStatus.' + paymentCredit.paymentStatus)">{{ paymentCredit.paymentStatus }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.paymentDetails')">Payment Details</span>
          </dt>
          <dd>
            <span>{{ paymentCredit.paymentDetails }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.receiptNotes')">Receipt Notes</span>
          </dt>
          <dd>
            <span>{{ paymentCredit.receiptNotes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.attachments')">Attachments</span>
          </dt>
          <dd>
            <span v-for="(attachments, i) in paymentCredit.attachments" :key="attachments.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }">{{ attachments.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.paymentCredit.workOrders')">Work Orders</span>
          </dt>
          <dd>
            <div v-if="paymentCredit.workOrders">
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: paymentCredit.workOrders.id } }">{{
                paymentCredit.workOrders.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="paymentCredit.id"
          :to="{ name: 'PaymentCreditEdit', params: { paymentCreditId: paymentCredit.id } }"
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

<script lang="ts" src="./payment-credit-details.component.ts"></script>
