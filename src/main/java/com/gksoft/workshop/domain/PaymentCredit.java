package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.workshop.domain.enumeration.PaymentMethod;
import com.gksoft.workshop.domain.enumeration.PaymentStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PaymentCredit.
 */
@Entity
@Table(name = "payment_credit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCredit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "payment_ref")
    private String paymentRef;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "add_date")
    private Instant addDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "payment_details")
    private String paymentDetails;

    @Column(name = "receipt_notes")
    private String receiptNotes;

    @OneToMany(mappedBy = "paymentCredit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jobs", "manager", "department", "workOrders", "paymentCredit" }, allowSetters = true)
    private Set<Employee> collectedBies = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_payment_credit__attachments",
        joinColumns = @JoinColumn(name = "payment_credit_id"),
        inverseJoinColumns = @JoinColumn(name = "attachments_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "attachmentNotes", "invoices", "purchaseOrders", "paymentCredits" }, allowSetters = true)
    private Set<Attachments> attachments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "status",
            "client",
            "itemModels",
            "itemBrand",
            "assignedStaffs",
            "appintments",
            "attachmentNotes",
            "invoices",
            "purchaseOrders",
            "paymentCredits",
        },
        allowSetters = true
    )
    private WorkOrders workOrders;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PaymentCredit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public PaymentCredit paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentRef() {
        return this.paymentRef;
    }

    public PaymentCredit paymentRef(String paymentRef) {
        this.setPaymentRef(paymentRef);
        return this;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    public Long getAmount() {
        return this.amount;
    }

    public PaymentCredit amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Instant getAddDate() {
        return this.addDate;
    }

    public PaymentCredit addDate(Instant addDate) {
        this.setAddDate(addDate);
        return this;
    }

    public void setAddDate(Instant addDate) {
        this.addDate = addDate;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public PaymentCredit paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentDetails() {
        return this.paymentDetails;
    }

    public PaymentCredit paymentDetails(String paymentDetails) {
        this.setPaymentDetails(paymentDetails);
        return this;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getReceiptNotes() {
        return this.receiptNotes;
    }

    public PaymentCredit receiptNotes(String receiptNotes) {
        this.setReceiptNotes(receiptNotes);
        return this;
    }

    public void setReceiptNotes(String receiptNotes) {
        this.receiptNotes = receiptNotes;
    }

    public Set<Employee> getCollectedBies() {
        return this.collectedBies;
    }

    public void setCollectedBies(Set<Employee> employees) {
        if (this.collectedBies != null) {
            this.collectedBies.forEach(i -> i.setPaymentCredit(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setPaymentCredit(this));
        }
        this.collectedBies = employees;
    }

    public PaymentCredit collectedBies(Set<Employee> employees) {
        this.setCollectedBies(employees);
        return this;
    }

    public PaymentCredit addCollectedBy(Employee employee) {
        this.collectedBies.add(employee);
        employee.setPaymentCredit(this);
        return this;
    }

    public PaymentCredit removeCollectedBy(Employee employee) {
        this.collectedBies.remove(employee);
        employee.setPaymentCredit(null);
        return this;
    }

    public Set<Attachments> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachments> attachments) {
        this.attachments = attachments;
    }

    public PaymentCredit attachments(Set<Attachments> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public PaymentCredit addAttachments(Attachments attachments) {
        this.attachments.add(attachments);
        attachments.getPaymentCredits().add(this);
        return this;
    }

    public PaymentCredit removeAttachments(Attachments attachments) {
        this.attachments.remove(attachments);
        attachments.getPaymentCredits().remove(this);
        return this;
    }

    public WorkOrders getWorkOrders() {
        return this.workOrders;
    }

    public void setWorkOrders(WorkOrders workOrders) {
        this.workOrders = workOrders;
    }

    public PaymentCredit workOrders(WorkOrders workOrders) {
        this.setWorkOrders(workOrders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentCredit)) {
            return false;
        }
        return id != null && id.equals(((PaymentCredit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCredit{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentRef='" + getPaymentRef() + "'" +
            ", amount=" + getAmount() +
            ", addDate='" + getAddDate() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentDetails='" + getPaymentDetails() + "'" +
            ", receiptNotes='" + getReceiptNotes() + "'" +
            "}";
    }
}
