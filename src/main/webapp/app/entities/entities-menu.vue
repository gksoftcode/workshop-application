<template>
  <div>
    <b-dropdown-item to="/country">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.country')">Country</span>
    </b-dropdown-item>
    <b-dropdown-item to="/location">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.location')">Location</span>
    </b-dropdown-item>
    <b-dropdown-item to="/department">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.department')">Department</span>
    </b-dropdown-item>
    <b-dropdown-item to="/task">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.task')">Task</span>
    </b-dropdown-item>
    <b-dropdown-item to="/employee">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.employee')">Employee</span>
    </b-dropdown-item>
    <b-dropdown-item to="/job">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.job')">Job</span>
    </b-dropdown-item>
    <b-dropdown-item to="/job-history">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.jobHistory')">Job History</span>
    </b-dropdown-item>
    <b-dropdown-item to="/client">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.client')">Client</span>
    </b-dropdown-item>
    <b-dropdown-item to="/appintment">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.appintment')">Appintment</span>
    </b-dropdown-item>
    <b-dropdown-item to="/item-models">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.itemModels')">Item Models</span>
    </b-dropdown-item>
    <b-dropdown-item to="/item-brand">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.itemBrand')">Item Brand</span>
    </b-dropdown-item>
    <b-dropdown-item to="/action">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.action')">Action</span>
    </b-dropdown-item>
    <b-dropdown-item to="/status">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.status')">Status</span>
    </b-dropdown-item>
    <b-dropdown-item to="/work-orders">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.workOrders')">Work Orders</span>
    </b-dropdown-item>
    <b-dropdown-item to="/attachment-notes">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.attachmentNotes')">Attachment Notes</span>
    </b-dropdown-item>
    <b-dropdown-item to="/attachments">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.attachments')">Attachments</span>
    </b-dropdown-item>
    <b-dropdown-item to="/supplier">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.supplier')">Supplier</span>
    </b-dropdown-item>
    <b-dropdown-item to="/service-category">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.serviceCategory')">Service Category</span>
    </b-dropdown-item>
    <b-dropdown-item to="/tax">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.tax')">Tax</span>
    </b-dropdown-item>
    <b-dropdown-item to="/services">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.services')">Services</span>
    </b-dropdown-item>
    <b-dropdown-item to="/invoice">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.invoice')">Invoice</span>
    </b-dropdown-item>
    <b-dropdown-item to="/invoice-details">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.invoiceDetails')">Invoice Details</span>
    </b-dropdown-item>
    <b-dropdown-item to="/purchase-order">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.purchaseOrder')">Purchase Order</span>
    </b-dropdown-item>
    <b-dropdown-item to="/purchase-order-details">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.purchaseOrderDetails')">Purchase Order Details</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
