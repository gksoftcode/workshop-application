package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Services.
 */
@Entity
@Table(name = "services")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Services implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Long cost;

    @Column(name = "price")
    private Long price;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "notes")
    private Long notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @OneToOne
    @JoinColumn(unique = true)
    private Tax tax;

    @ManyToOne
    private ServiceCategory serviceCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = { "client", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private Invoice invoice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "supplier", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Services id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Services name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Services description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCost() {
        return this.cost;
    }

    public Services cost(Long cost) {
        this.setCost(cost);
        return this;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Long getPrice() {
        return this.price;
    }

    public Services price(Long price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public Services discount(Long discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getNotes() {
        return this.notes;
    }

    public Services notes(Long notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(Long notes) {
        this.notes = notes;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public Services discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Tax getTax() {
        return this.tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Services tax(Tax tax) {
        this.setTax(tax);
        return this;
    }

    public ServiceCategory getServiceCategory() {
        return this.serviceCategory;
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public Services serviceCategory(ServiceCategory serviceCategory) {
        this.setServiceCategory(serviceCategory);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Services invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Services purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Services)) {
            return false;
        }
        return id != null && id.equals(((Services) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Services{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", cost=" + getCost() +
            ", price=" + getPrice() +
            ", discount=" + getDiscount() +
            ", notes=" + getNotes() +
            ", discountType='" + getDiscountType() + "'" +
            "}";
    }
}
