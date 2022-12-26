import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import CountryService from './country/country.service';
import LocationService from './location/location.service';
import DepartmentService from './department/department.service';
import TaskService from './task/task.service';
import EmployeeService from './employee/employee.service';
import JobService from './job/job.service';
import JobHistoryService from './job-history/job-history.service';
import ClientService from './client/client.service';
import AppintmentService from './appintment/appintment.service';
import ItemModelsService from './item-models/item-models.service';
import ItemBrandService from './item-brand/item-brand.service';
import ActionService from './action/action.service';
import StatusService from './status/status.service';
import WorkOrdersService from './work-orders/work-orders.service';
import AttachmentNotesService from './attachment-notes/attachment-notes.service';
import AttachmentsService from './attachments/attachments.service';
import SupplierService from './supplier/supplier.service';
import ServiceCategoryService from './service-category/service-category.service';
import TaxService from './tax/tax.service';
import ServicesService from './services/services.service';
import InvoiceService from './invoice/invoice.service';
import InvoiceDetailsService from './invoice-details/invoice-details.service';
import PurchaseOrderService from './purchase-order/purchase-order.service';
import PurchaseOrderDetailsService from './purchase-order-details/purchase-order-details.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('countryService') private countryService = () => new CountryService();
  @Provide('locationService') private locationService = () => new LocationService();
  @Provide('departmentService') private departmentService = () => new DepartmentService();
  @Provide('taskService') private taskService = () => new TaskService();
  @Provide('employeeService') private employeeService = () => new EmployeeService();
  @Provide('jobService') private jobService = () => new JobService();
  @Provide('jobHistoryService') private jobHistoryService = () => new JobHistoryService();
  @Provide('clientService') private clientService = () => new ClientService();
  @Provide('appintmentService') private appintmentService = () => new AppintmentService();
  @Provide('itemModelsService') private itemModelsService = () => new ItemModelsService();
  @Provide('itemBrandService') private itemBrandService = () => new ItemBrandService();
  @Provide('actionService') private actionService = () => new ActionService();
  @Provide('statusService') private statusService = () => new StatusService();
  @Provide('workOrdersService') private workOrdersService = () => new WorkOrdersService();
  @Provide('attachmentNotesService') private attachmentNotesService = () => new AttachmentNotesService();
  @Provide('attachmentsService') private attachmentsService = () => new AttachmentsService();
  @Provide('supplierService') private supplierService = () => new SupplierService();
  @Provide('serviceCategoryService') private serviceCategoryService = () => new ServiceCategoryService();
  @Provide('taxService') private taxService = () => new TaxService();
  @Provide('servicesService') private servicesService = () => new ServicesService();
  @Provide('invoiceService') private invoiceService = () => new InvoiceService();
  @Provide('invoiceDetailsService') private invoiceDetailsService = () => new InvoiceDetailsService();
  @Provide('purchaseOrderService') private purchaseOrderService = () => new PurchaseOrderService();
  @Provide('purchaseOrderDetailsService') private purchaseOrderDetailsService = () => new PurchaseOrderDetailsService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
