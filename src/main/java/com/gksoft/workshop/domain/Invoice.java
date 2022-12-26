package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Invoice.
 */
@Entity
@Table(name = "invoice")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_date")
    private Instant invoiceDate;

    @Column(name = "issue_date")
    private Instant issueDate;

    @Column(name = "payment_terms")
    private Long paymentTerms;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "notes")
    private Long notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "deposit_amount")
    private Long depositAmount;

    @Column(name = "is_deposit_paied")
    private Boolean isDepositPaied;

    @Enumerated(EnumType.STRING)
    @Column(name = "deposit_method")
    private PaymentMethod depositMethod;

    @Column(name = "deposit_pay_ref")
    private String depositPayRef;

    @Column(name = "is_already_paied")
    private Boolean isAlreadyPaied;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_ref")
    private String paymentRef;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "last_amount")
    private Long lastAmount;

    @Column(name = "shipping_fees")
    private Long shippingFees;

    @JsonIgnoreProperties(value = { "appintments", "locations" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Client client;

    @OneToMany(mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "invoice" }, allowSetters = true)
    private Set<InvoiceDetails> invoiceDetails = new HashSet<>();

    @OneToMany(mappedBy = "invoice")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tax", "serviceCategory", "invoice", "purchaseOrder" }, allowSetters = true)
    private Set<Services> services = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_invoice__attachments",
        joinColumns = @JoinColumn(name = "invoice_id"),
        inverseJoinColumns = @JoinColumn(name = "attachments_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachmentNotes", "invoices", "purchaseOrders" }, allowSetters = true)
    private Set<Attachments> attachments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "status", "client", "itemModels", "itemBrand", "assignedStaffs", "appintments", "attachmentNotes", "invoices", "purchaseOrders",
        },
        allowSetters = true
    )
    private WorkOrders workOrders;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Invoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInvoiceDate() {
        return this.invoiceDate;
    }

    public Invoice invoiceDate(Instant invoiceDate) {
        this.setInvoiceDate(invoiceDate);
        return this;
    }

    public void setInvoiceDate(Instant invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public Invoice issueDate(Instant issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Long getPaymentTerms() {
        return this.paymentTerms;
    }

    public Invoice paymentTerms(Long paymentTerms) {
        this.setPaymentTerms(paymentTerms);
        return this;
    }

    public void setPaymentTerms(Long paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public Invoice discount(Long discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getNotes() {
        return this.notes;
    }

    public Invoice notes(Long notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(Long notes) {
        this.notes = notes;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Invoice discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Long getDepositAmount() {
        return this.depositAmount;
    }

    public Invoice depositAmount(Long depositAmount) {
        this.setDepositAmount(depositAmount);
        return this;
    }

    public void setDepositAmount(Long depositAmount) {
        this.depositAmount = depositAmount;
    }

    public Boolean getIsDepositPaied() {
        return this.isDepositPaied;
    }

    public Invoice isDepositPaied(Boolean isDepositPaied) {
        this.setIsDepositPaied(isDepositPaied);
        return this;
    }

    public void setIsDepositPaied(Boolean isDepositPaied) {
        this.isDepositPaied = isDepositPaied;
    }

    public PaymentMethod getDepositMethod() {
        return this.depositMethod;
    }

    public Invoice depositMethod(PaymentMethod depositMethod) {
        this.setDepositMethod(depositMethod);
        return this;
    }

    public void setDepositMethod(PaymentMethod depositMethod) {
        this.depositMethod = depositMethod;
    }

    public String getDepositPayRef() {
        return this.depositPayRef;
    }

    public Invoice depositPayRef(String depositPayRef) {
        this.setDepositPayRef(depositPayRef);
        return this;
    }

    public void setDepositPayRef(String depositPayRef) {
        this.depositPayRef = depositPayRef;
    }

    public Boolean getIsAlreadyPaied() {
        return this.isAlreadyPaied;
    }

    public Invoice isAlreadyPaied(Boolean isAlreadyPaied) {
        this.setIsAlreadyPaied(isAlreadyPaied);
        return this;
    }

    public void setIsAlreadyPaied(Boolean isAlreadyPaied) {
        this.isAlreadyPaied = isAlreadyPaied;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public Invoice paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentRef() {
        return this.paymentRef;
    }

    public Invoice paymentRef(String paymentRef) {
        this.setPaymentRef(paymentRef);
        return this;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Invoice amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getLastAmount() {
        return this.lastAmount;
    }

    public Invoice lastAmount(Long lastAmount) {
        this.setLastAmount(lastAmount);
        return this;
    }

    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }

    public Long getShippingFees() {
        return this.shippingFees;
    }

    public Invoice shippingFees(Long shippingFees) {
        this.setShippingFees(shippingFees);
        return this;
    }

    public void setShippingFees(Long shippingFees) {
        this.shippingFees = shippingFees;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Invoice client(Client client) {
        this.setClient(client);
        return this;
    }

    public Set<InvoiceDetails> getInvoiceDetails() {
        return this.invoiceDetails;
    }

    public void setInvoiceDetails(Set<InvoiceDetails> invoiceDetails) {
        if (this.invoiceDetails != null) {
            this.invoiceDetails.forEach(i -> i.setInvoice(null));
        }
        if (invoiceDetails != null) {
            invoiceDetails.forEach(i -> i.setInvoice(this));
        }
        this.invoiceDetails = invoiceDetails;
    }

    public Invoice invoiceDetails(Set<InvoiceDetails> invoiceDetails) {
        this.setInvoiceDetails(invoiceDetails);
        return this;
    }

    public Invoice addInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails.add(invoiceDetails);
        invoiceDetails.setInvoice(this);
        return this;
    }

    public Invoice removeInvoiceDetails(InvoiceDetails invoiceDetails) {
        this.invoiceDetails.remove(invoiceDetails);
        invoiceDetails.setInvoice(null);
        return this;
    }

    public Set<Services> getServices() {
        return this.services;
    }

    public void setServices(Set<Services> services) {
        if (this.services != null) {
            this.services.forEach(i -> i.setInvoice(null));
        }
        if (services != null) {
            services.forEach(i -> i.setInvoice(this));
        }
        this.services = services;
    }

    public Invoice services(Set<Services> services) {
        this.setServices(services);
        return this;
    }

    public Invoice addServices(Services services) {
        this.services.add(services);
        services.setInvoice(this);
        return this;
    }

    public Invoice removeServices(Services services) {
        this.services.remove(services);
        services.setInvoice(null);
        return this;
    }

    public Set<Attachments> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachments> attachments) {
        this.attachments = attachments;
    }

    public Invoice attachments(Set<Attachments> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public Invoice addAttachments(Attachments attachments) {
        this.attachments.add(attachments);
        attachments.getInvoices().add(this);
        return this;
    }

    public Invoice removeAttachments(Attachments attachments) {
        this.attachments.remove(attachments);
        attachments.getInvoices().remove(this);
        return this;
    }

    public WorkOrders getWorkOrders() {
        return this.workOrders;
    }

    public void setWorkOrders(WorkOrders workOrders) {
        this.workOrders = workOrders;
    }

    public Invoice workOrders(WorkOrders workOrders) {
        this.setWorkOrders(workOrders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", paymentTerms=" + getPaymentTerms() +
            ", discount=" + getDiscount() +
            ", notes=" + getNotes() +
            ", discountType='" + getDiscountType() + "'" +
            ", depositAmount=" + getDepositAmount() +
            ", isDepositPaied='" + getIsDepositPaied() + "'" +
            ", depositMethod='" + getDepositMethod() + "'" +
            ", depositPayRef='" + getDepositPayRef() + "'" +
            ", isAlreadyPaied='" + getIsAlreadyPaied() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentRef='" + getPaymentRef() + "'" +
            ", amount=" + getAmount() +
            ", lastAmount=" + getLastAmount() +
            ", shippingFees=" + getShippingFees() +
            "}";
    }
}
