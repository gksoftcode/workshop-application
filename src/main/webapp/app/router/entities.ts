import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

// prettier-ignore
const Country = () => import('@/entities/country/country.vue');
// prettier-ignore
const CountryUpdate = () => import('@/entities/country/country-update.vue');
// prettier-ignore
const CountryDetails = () => import('@/entities/country/country-details.vue');
// prettier-ignore
const Location = () => import('@/entities/location/location.vue');
// prettier-ignore
const LocationUpdate = () => import('@/entities/location/location-update.vue');
// prettier-ignore
const LocationDetails = () => import('@/entities/location/location-details.vue');
// prettier-ignore
const Department = () => import('@/entities/department/department.vue');
// prettier-ignore
const DepartmentUpdate = () => import('@/entities/department/department-update.vue');
// prettier-ignore
const DepartmentDetails = () => import('@/entities/department/department-details.vue');
// prettier-ignore
const Task = () => import('@/entities/task/task.vue');
// prettier-ignore
const TaskUpdate = () => import('@/entities/task/task-update.vue');
// prettier-ignore
const TaskDetails = () => import('@/entities/task/task-details.vue');
// prettier-ignore
const Employee = () => import('@/entities/employee/employee.vue');
// prettier-ignore
const EmployeeUpdate = () => import('@/entities/employee/employee-update.vue');
// prettier-ignore
const EmployeeDetails = () => import('@/entities/employee/employee-details.vue');
// prettier-ignore
const Job = () => import('@/entities/job/job.vue');
// prettier-ignore
const JobUpdate = () => import('@/entities/job/job-update.vue');
// prettier-ignore
const JobDetails = () => import('@/entities/job/job-details.vue');
// prettier-ignore
const JobHistory = () => import('@/entities/job-history/job-history.vue');
// prettier-ignore
const JobHistoryUpdate = () => import('@/entities/job-history/job-history-update.vue');
// prettier-ignore
const JobHistoryDetails = () => import('@/entities/job-history/job-history-details.vue');
// prettier-ignore
const Client = () => import('@/entities/client/client.vue');
// prettier-ignore
const ClientUpdate = () => import('@/entities/client/client-update.vue');
// prettier-ignore
const ClientDetails = () => import('@/entities/client/client-details.vue');
// prettier-ignore
const Appintment = () => import('@/entities/appintment/appintment.vue');
// prettier-ignore
const AppintmentUpdate = () => import('@/entities/appintment/appintment-update.vue');
// prettier-ignore
const AppintmentDetails = () => import('@/entities/appintment/appintment-details.vue');
// prettier-ignore
const ItemModels = () => import('@/entities/item-models/item-models.vue');
// prettier-ignore
const ItemModelsUpdate = () => import('@/entities/item-models/item-models-update.vue');
// prettier-ignore
const ItemModelsDetails = () => import('@/entities/item-models/item-models-details.vue');
// prettier-ignore
const ItemBrand = () => import('@/entities/item-brand/item-brand.vue');
// prettier-ignore
const ItemBrandUpdate = () => import('@/entities/item-brand/item-brand-update.vue');
// prettier-ignore
const ItemBrandDetails = () => import('@/entities/item-brand/item-brand-details.vue');
// prettier-ignore
const Action = () => import('@/entities/action/action.vue');
// prettier-ignore
const ActionUpdate = () => import('@/entities/action/action-update.vue');
// prettier-ignore
const ActionDetails = () => import('@/entities/action/action-details.vue');
// prettier-ignore
const Status = () => import('@/entities/status/status.vue');
// prettier-ignore
const StatusUpdate = () => import('@/entities/status/status-update.vue');
// prettier-ignore
const StatusDetails = () => import('@/entities/status/status-details.vue');
// prettier-ignore
const WorkOrders = () => import('@/entities/work-orders/work-orders.vue');
// prettier-ignore
const WorkOrdersUpdate = () => import('@/entities/work-orders/work-orders-update.vue');
// prettier-ignore
const WorkOrdersDetails = () => import('@/entities/work-orders/work-orders-details.vue');
// prettier-ignore
const AttachmentNotes = () => import('@/entities/attachment-notes/attachment-notes.vue');
// prettier-ignore
const AttachmentNotesUpdate = () => import('@/entities/attachment-notes/attachment-notes-update.vue');
// prettier-ignore
const AttachmentNotesDetails = () => import('@/entities/attachment-notes/attachment-notes-details.vue');
// prettier-ignore
const Attachments = () => import('@/entities/attachments/attachments.vue');
// prettier-ignore
const AttachmentsUpdate = () => import('@/entities/attachments/attachments-update.vue');
// prettier-ignore
const AttachmentsDetails = () => import('@/entities/attachments/attachments-details.vue');
// prettier-ignore
const Supplier = () => import('@/entities/supplier/supplier.vue');
// prettier-ignore
const SupplierUpdate = () => import('@/entities/supplier/supplier-update.vue');
// prettier-ignore
const SupplierDetails = () => import('@/entities/supplier/supplier-details.vue');
// prettier-ignore
const ServiceCategory = () => import('@/entities/service-category/service-category.vue');
// prettier-ignore
const ServiceCategoryUpdate = () => import('@/entities/service-category/service-category-update.vue');
// prettier-ignore
const ServiceCategoryDetails = () => import('@/entities/service-category/service-category-details.vue');
// prettier-ignore
const Tax = () => import('@/entities/tax/tax.vue');
// prettier-ignore
const TaxUpdate = () => import('@/entities/tax/tax-update.vue');
// prettier-ignore
const TaxDetails = () => import('@/entities/tax/tax-details.vue');
// prettier-ignore
const Services = () => import('@/entities/services/services.vue');
// prettier-ignore
const ServicesUpdate = () => import('@/entities/services/services-update.vue');
// prettier-ignore
const ServicesDetails = () => import('@/entities/services/services-details.vue');
// prettier-ignore
const Invoice = () => import('@/entities/invoice/invoice.vue');
// prettier-ignore
const InvoiceUpdate = () => import('@/entities/invoice/invoice-update.vue');
// prettier-ignore
const InvoiceDetails = () => import('@/entities/invoice/invoice-details.vue');
// prettier-ignore
const InvoiceDetails = () => import('@/entities/invoice-details/invoice-details.vue');
// prettier-ignore
const InvoiceDetailsUpdate = () => import('@/entities/invoice-details/invoice-details-update.vue');
// prettier-ignore
const InvoiceDetailsDetails = () => import('@/entities/invoice-details/invoice-details-details.vue');
// prettier-ignore
const PurchaseOrder = () => import('@/entities/purchase-order/purchase-order.vue');
// prettier-ignore
const PurchaseOrderUpdate = () => import('@/entities/purchase-order/purchase-order-update.vue');
// prettier-ignore
const PurchaseOrderDetails = () => import('@/entities/purchase-order/purchase-order-details.vue');
// prettier-ignore
const PurchaseOrderDetails = () => import('@/entities/purchase-order-details/purchase-order-details.vue');
// prettier-ignore
const PurchaseOrderDetailsUpdate = () => import('@/entities/purchase-order-details/purchase-order-details-update.vue');
// prettier-ignore
const PurchaseOrderDetailsDetails = () => import('@/entities/purchase-order-details/purchase-order-details-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'country',
      name: 'Country',
      component: Country,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/new',
      name: 'CountryCreate',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/edit',
      name: 'CountryEdit',
      component: CountryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'country/:countryId/view',
      name: 'CountryView',
      component: CountryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location',
      name: 'Location',
      component: Location,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/new',
      name: 'LocationCreate',
      component: LocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/:locationId/edit',
      name: 'LocationEdit',
      component: LocationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'location/:locationId/view',
      name: 'LocationView',
      component: LocationDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'department',
      name: 'Department',
      component: Department,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'department/new',
      name: 'DepartmentCreate',
      component: DepartmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'department/:departmentId/edit',
      name: 'DepartmentEdit',
      component: DepartmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'department/:departmentId/view',
      name: 'DepartmentView',
      component: DepartmentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task',
      name: 'Task',
      component: Task,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/new',
      name: 'TaskCreate',
      component: TaskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/:taskId/edit',
      name: 'TaskEdit',
      component: TaskUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'task/:taskId/view',
      name: 'TaskView',
      component: TaskDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'employee',
      name: 'Employee',
      component: Employee,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'employee/new',
      name: 'EmployeeCreate',
      component: EmployeeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'employee/:employeeId/edit',
      name: 'EmployeeEdit',
      component: EmployeeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'employee/:employeeId/view',
      name: 'EmployeeView',
      component: EmployeeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job',
      name: 'Job',
      component: Job,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job/new',
      name: 'JobCreate',
      component: JobUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job/:jobId/edit',
      name: 'JobEdit',
      component: JobUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job/:jobId/view',
      name: 'JobView',
      component: JobDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job-history',
      name: 'JobHistory',
      component: JobHistory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job-history/new',
      name: 'JobHistoryCreate',
      component: JobHistoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job-history/:jobHistoryId/edit',
      name: 'JobHistoryEdit',
      component: JobHistoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'job-history/:jobHistoryId/view',
      name: 'JobHistoryView',
      component: JobHistoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client',
      name: 'Client',
      component: Client,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/new',
      name: 'ClientCreate',
      component: ClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/:clientId/edit',
      name: 'ClientEdit',
      component: ClientUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client/:clientId/view',
      name: 'ClientView',
      component: ClientDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'appintment',
      name: 'Appintment',
      component: Appintment,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'appintment/new',
      name: 'AppintmentCreate',
      component: AppintmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'appintment/:appintmentId/edit',
      name: 'AppintmentEdit',
      component: AppintmentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'appintment/:appintmentId/view',
      name: 'AppintmentView',
      component: AppintmentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-models',
      name: 'ItemModels',
      component: ItemModels,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-models/new',
      name: 'ItemModelsCreate',
      component: ItemModelsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-models/:itemModelsId/edit',
      name: 'ItemModelsEdit',
      component: ItemModelsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-models/:itemModelsId/view',
      name: 'ItemModelsView',
      component: ItemModelsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-brand',
      name: 'ItemBrand',
      component: ItemBrand,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-brand/new',
      name: 'ItemBrandCreate',
      component: ItemBrandUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-brand/:itemBrandId/edit',
      name: 'ItemBrandEdit',
      component: ItemBrandUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'item-brand/:itemBrandId/view',
      name: 'ItemBrandView',
      component: ItemBrandDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'action',
      name: 'Action',
      component: Action,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'action/new',
      name: 'ActionCreate',
      component: ActionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'action/:actionId/edit',
      name: 'ActionEdit',
      component: ActionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'action/:actionId/view',
      name: 'ActionView',
      component: ActionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'status',
      name: 'Status',
      component: Status,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'status/new',
      name: 'StatusCreate',
      component: StatusUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'status/:statusId/edit',
      name: 'StatusEdit',
      component: StatusUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'status/:statusId/view',
      name: 'StatusView',
      component: StatusDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'work-orders',
      name: 'WorkOrders',
      component: WorkOrders,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'work-orders/new',
      name: 'WorkOrdersCreate',
      component: WorkOrdersUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'work-orders/:workOrdersId/edit',
      name: 'WorkOrdersEdit',
      component: WorkOrdersUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'work-orders/:workOrdersId/view',
      name: 'WorkOrdersView',
      component: WorkOrdersDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachment-notes',
      name: 'AttachmentNotes',
      component: AttachmentNotes,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachment-notes/new',
      name: 'AttachmentNotesCreate',
      component: AttachmentNotesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachment-notes/:attachmentNotesId/edit',
      name: 'AttachmentNotesEdit',
      component: AttachmentNotesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachment-notes/:attachmentNotesId/view',
      name: 'AttachmentNotesView',
      component: AttachmentNotesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachments',
      name: 'Attachments',
      component: Attachments,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachments/new',
      name: 'AttachmentsCreate',
      component: AttachmentsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachments/:attachmentsId/edit',
      name: 'AttachmentsEdit',
      component: AttachmentsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'attachments/:attachmentsId/view',
      name: 'AttachmentsView',
      component: AttachmentsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'supplier',
      name: 'Supplier',
      component: Supplier,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'supplier/new',
      name: 'SupplierCreate',
      component: SupplierUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'supplier/:supplierId/edit',
      name: 'SupplierEdit',
      component: SupplierUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'supplier/:supplierId/view',
      name: 'SupplierView',
      component: SupplierDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-category',
      name: 'ServiceCategory',
      component: ServiceCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-category/new',
      name: 'ServiceCategoryCreate',
      component: ServiceCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-category/:serviceCategoryId/edit',
      name: 'ServiceCategoryEdit',
      component: ServiceCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'service-category/:serviceCategoryId/view',
      name: 'ServiceCategoryView',
      component: ServiceCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tax',
      name: 'Tax',
      component: Tax,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tax/new',
      name: 'TaxCreate',
      component: TaxUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tax/:taxId/edit',
      name: 'TaxEdit',
      component: TaxUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'tax/:taxId/view',
      name: 'TaxView',
      component: TaxDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'services',
      name: 'Services',
      component: Services,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'services/new',
      name: 'ServicesCreate',
      component: ServicesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'services/:servicesId/edit',
      name: 'ServicesEdit',
      component: ServicesUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'services/:servicesId/view',
      name: 'ServicesView',
      component: ServicesDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice',
      name: 'Invoice',
      component: Invoice,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/new',
      name: 'InvoiceCreate',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/edit',
      name: 'InvoiceEdit',
      component: InvoiceUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice/:invoiceId/view',
      name: 'InvoiceView',
      component: InvoiceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-details',
      name: 'InvoiceDetails',
      component: InvoiceDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-details/new',
      name: 'InvoiceDetailsCreate',
      component: InvoiceDetailsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-details/:invoiceDetailsId/edit',
      name: 'InvoiceDetailsEdit',
      component: InvoiceDetailsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'invoice-details/:invoiceDetailsId/view',
      name: 'InvoiceDetailsView',
      component: InvoiceDetailsDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order',
      name: 'PurchaseOrder',
      component: PurchaseOrder,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order/new',
      name: 'PurchaseOrderCreate',
      component: PurchaseOrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order/:purchaseOrderId/edit',
      name: 'PurchaseOrderEdit',
      component: PurchaseOrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order/:purchaseOrderId/view',
      name: 'PurchaseOrderView',
      component: PurchaseOrderDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order-details',
      name: 'PurchaseOrderDetails',
      component: PurchaseOrderDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order-details/new',
      name: 'PurchaseOrderDetailsCreate',
      component: PurchaseOrderDetailsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order-details/:purchaseOrderDetailsId/edit',
      name: 'PurchaseOrderDetailsEdit',
      component: PurchaseOrderDetailsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'purchase-order-details/:purchaseOrderDetailsId/view',
      name: 'PurchaseOrderDetailsView',
      component: PurchaseOrderDetailsDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
