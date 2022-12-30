<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="purchaseOrder">
        <h2 class="jh-entity-heading" data-cy="purchaseOrderDetailsHeading">
          <span v-text="$t('workshopApp.purchaseOrder.detail.title')">PurchaseOrder</span> {{ purchaseOrder.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.invoiceDate')">Invoice Date</span>
          </dt>
          <dd>
            <span v-if="purchaseOrder.invoiceDate">{{ $d(Date.parse(purchaseOrder.invoiceDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.issueDate')">Issue Date</span>
          </dt>
          <dd>
            <span v-if="purchaseOrder.issueDate">{{ $d(Date.parse(purchaseOrder.issueDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.paymentTerms')">Payment Terms</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.paymentTerms }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.discount')">Discount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.discount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.notes')">Notes</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.notes }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.discountType')">Discount Type</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.DiscountType.' + purchaseOrder.discountType)">{{ purchaseOrder.discountType }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.depositAmount')">Deposit Amount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.depositAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.isDepositPaied')">Is Deposit Paied</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.isDepositPaied }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.depositMethod')">Deposit Method</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentMethod.' + purchaseOrder.depositMethod)">{{ purchaseOrder.depositMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.depositPayRef')">Deposit Pay Ref</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.depositPayRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.isAlreadyPaied')">Is Already Paied</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.isAlreadyPaied }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.paymentMethod')">Payment Method</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentMethod.' + purchaseOrder.paymentMethod)">{{ purchaseOrder.paymentMethod }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.paymentStatus')">Payment Status</span>
          </dt>
          <dd>
            <span v-text="$t('workshopApp.PaymentStatus.' + purchaseOrder.paymentStatus)">{{ purchaseOrder.paymentStatus }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.paymentRef')">Payment Ref</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.paymentRef }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.lastAmount')">Last Amount</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.lastAmount }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.shippingFees')">Shipping Fees</span>
          </dt>
          <dd>
            <span>{{ purchaseOrder.shippingFees }}</span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.supplier')">Supplier</span>
          </dt>
          <dd>
            <div v-if="purchaseOrder.supplier">
              <router-link :to="{ name: 'SupplierView', params: { supplierId: purchaseOrder.supplier.id } }">{{
                purchaseOrder.supplier.name
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.attachments')">Attachments</span>
          </dt>
          <dd>
            <span v-for="(attachments, i) in purchaseOrder.attachments" :key="attachments.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'AttachmentsView', params: { attachmentsId: attachments.id } }">{{ attachments.name }}</router-link>
            </span>
          </dd>
          <dt>
            <span v-text="$t('workshopApp.purchaseOrder.workOrders')">Work Orders</span>
          </dt>
          <dd>
            <div v-if="purchaseOrder.workOrders">
              <router-link :to="{ name: 'WorkOrdersView', params: { workOrdersId: purchaseOrder.workOrders.id } }">{{
                purchaseOrder.workOrders.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="purchaseOrder.id"
          :to="{ name: 'PurchaseOrderEdit', params: { purchaseOrderId: purchaseOrder.id } }"
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

<script lang="ts" src="./purchase-order-details.component.ts"></script>
