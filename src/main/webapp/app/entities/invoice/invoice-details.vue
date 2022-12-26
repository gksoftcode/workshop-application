<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="invoice">
        <h2 class="jh-entity-heading" data-cy="invoiceDetailsHeading">
          <span v-text="$t('workshopApp.invoice.detail.title')">Invoice</span> {{ invoice.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.invoice.invoiceDate')">Invoice Date</span>
          </dt>
          <dd>
            <span v-if="invoice.invoiceDate">{{ $d(Date.parse(invoice.invoiceDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.issueDate')">Issue Date</span>
          </dt>
          <dd>
            <span v-if="invoice.issueDate">{{ $d(Date.parse(invoice.issueDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.paymentTerms')">Payment Terms</span>
          </dt>
          <dd>
            <span>{{ invoice.paymentTerms }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.discount')">Discount</span>
          </dt>
          <dd>
            <span>{{ invoice.discount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.notes')">Notes</span>
          </dt>
          <dd>
            <span>{{ invoice.notes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.discountType')">Discount Type</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.DiscountType.' + invoice.discountType)">{{ invoice.discountType }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.depositAmount')">Deposit Amount</span>
          </dt>
          <dd>
            <span>{{ invoice.depositAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.isDepositPaied')">Is Deposit Paied</span>
          </dt>
          <dd>
            <span>{{ invoice.isDepositPaied }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.depositMethod')">Deposit Method</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentMethod.' + invoice.depositMethod)">{{ invoice.depositMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.depositPayRef')">Deposit Pay Ref</span>
          </dt>
          <dd>
            <span>{{ invoice.depositPayRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.isAlreadyPaied')">Is Already Paied</span>
          </dt>
          <dd>
            <span>{{ invoice.isAlreadyPaied }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.paymentMethod')">Payment Method</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentMethod.' + invoice.paymentMethod)">{{ invoice.paymentMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.paymentRef')">Payment Ref</span>
          </dt>
          <dd>
            <span>{{ invoice.paymentRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ invoice.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.lastAmount')">Last Amount</span>
          </dt>
          <dd>
            <span>{{ invoice.lastAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.shippingFees')">Shipping Fees</span>
          </dt>
          <dd>
            <span>{{ invoice.shippingFees }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.client')">Client</span>
          </dt>
          <dd>
            <div v-if="invoice.client">
              <router-link :to="{ name: 'ClientView', params: { clientId: invoice.client.id } }">{{ invoice.client.fullName }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.attachments')">Attachments</span>
          </dt>
          <dd>
            <span v-for="(attachments, i) in invoice.attachments" :key="attachments.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }">{{ attachments.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.invoice.workOrders')">Work Orders</span>
          </dt>
          <dd>
            <div v-if="invoice.workOrders">
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: invoice.workOrders.id } }">{{
                invoice.workOrders.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="invoice.id" :to="{ name: 'InvoiceEdit', params: { invoiceId: invoice.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./invoice-details.component.ts"></script>
