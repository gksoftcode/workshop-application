package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gksoft.workshop.domain.enumeration.DiscountType;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrderDetails.
 */
@Entity
@Table(name = "purchase_order_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseOrderDetails implements Serializable {

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

    @OneToOne
    @JoinColumn(unique = true)
    private Tax tax;

    @ManyToOne
    @JsonIgnoreProperties(value = { "supplier", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private PurchaseOrder purchaseOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrderDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemNo() {
        return this.itemNo;
    }

    public PurchaseOrderDetails itemNo(Long itemNo) {
        this.setItemNo(itemNo);
        return this;
    }

    public void setItemNo(Long itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public PurchaseOrderDetails itemDesc(String itemDesc) {
        this.setItemDesc(itemDesc);
        return this;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Long getItemPrice() {
        return this.itemPrice;
    }

    public PurchaseOrderDetails itemPrice(Long itemPrice) {
        this.setItemPrice(itemPrice);
        return this;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Long getItemQty() {
        return this.itemQty;
    }

    public PurchaseOrderDetails itemQty(Long itemQty) {
        this.setItemQty(itemQty);
        return this;
    }

    public void setItemQty(Long itemQty) {
        this.itemQty = itemQty;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public PurchaseOrderDetails discountType(DiscountType discountType) {
        this.setDiscountType(discountType);
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Long getDiscount() {
        return this.discount;
    }

    public PurchaseOrderDetails discount(Long discount) {
        this.setDiscount(discount);
        return this;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public Long getTotalAmount() {
        return this.totalAmount;
    }

    public PurchaseOrderDetails totalAmount(Long totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Tax getTax() {
        return this.tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public PurchaseOrderDetails tax(Tax tax) {
        this.setTax(tax);
        return this;
    }

    public PurchaseOrder getPurchaseOrder() {
        return this.purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderDetails purchaseOrder(PurchaseOrder purchaseOrder) {
        this.setPurchaseOrder(purchaseOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrderDetails)) {
            return false;
        }
        return id != null && id.equals(((PurchaseOrderDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderDetails{" +
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
