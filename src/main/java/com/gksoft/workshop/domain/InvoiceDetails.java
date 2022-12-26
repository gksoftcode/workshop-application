package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InvoiceDetails.
 */
@Entity
@Table(name = "invoice_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvoiceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_no")
    private Long itemNo;

    @Column(name = "item_desc")
    private String itemDesc;

    @Column(name = "item_price")
    private Long itemPrice;

    @Column(name = "item_qty")
    private Long itemQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "total_amount")
    private Long totalAmount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "client", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvoiceDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemNo() {
        return this.itemNo;
    }

    public InvoiceDetails itemNo(Long itemNo) {
        this.setItemNo(itemNo);
        return this;
    }

    public void setItemNo(Long itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public InvoiceDetails itemDesc(String itemDesc) {
        this.setItemDesc(itemDesc);
        return this;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Long getItemPrice() {
        return this.itemPrice;
    }

    public InvoiceDetails itemPrice(Long itemPrice) {
        this.setItemPrice(itemPrice);
        return this;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getItemQty() {
        return this.itemQty;
    }

    public InvoiceDetails itemQty(Long itemQty) {
        this.setItemQty(itemQty);
        return this;
    }

    public void setItemQty(Long itemQty) {
        this.itemQty = itemQty;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public InvoiceDetails discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public InvoiceDetails discount(Long discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getTotalAmount() {
        return this.totalAmount;
    }

    public InvoiceDetails totalAmount(Long totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceDetails invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDetails)) {
            return false;
        }
        return id != null && id.equals(((InvoiceDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDetails{" +
            "id=" + getId() +
            ", itemNo=" + getItemNo() +
            ", itemDesc='" + getItemDesc() + "'" +
            ", itemPrice=" + getItemPrice() +
            ", itemQty=" + getItemQty() +
            ", discountType='" + getDiscountType() + "'" +
            ", discount=" + getDiscount() +
            ", totalAmount=" + getTotalAmount() +
            "}";
    }
}
