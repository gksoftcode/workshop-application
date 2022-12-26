package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Attachments.
 */
@Entity
@Table(name = "attachments")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attachments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "attach")
    private byte[] attach;

    @Column(name = "attach_content_type")
    private String attachContentType;

    @ManyToMany(mappedBy = "attachments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "status", "action", "attachments", "workOrders" }, allowSetters = true)
    private Set<AttachmentNotes> attachmentNotes = new HashSet<>();

    @ManyToMany(mappedBy = "attachments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private Set<Invoice> invoices = new HashSet<>();

    @ManyToMany(mappedBy = "attachments")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "supplier", "invoiceDetails", "services", "attachments", "workOrders" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachments id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Attachments name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAttach() {
        return this.attach;
    }

    public Attachments attach(byte[] attach) {
        this.setAttach(attach);
        return this;
    }

    public void setAttach(byte[] attach) {
        this.attach = attach;
    }

    public String getAttachContentType() {
        return this.attachContentType;
    }

    public Attachments attachContentType(String attachContentType) {
        this.attachContentType = attachContentType;
        return this;
    }

    public void setAttachContentType(String attachContentType) {
        this.attachContentType = attachContentType;
    }

    public Set<AttachmentNotes> getAttachmentNotes() {
        return this.attachmentNotes;
    }

    public void setAttachmentNotes(Set<AttachmentNotes> attachmentNotes) {
        if (this.attachmentNotes != null) {
            this.attachmentNotes.forEach(i -> i.removeAttachments(this));
        }
        if (attachmentNotes != null) {
            attachmentNotes.forEach(i -> i.addAttachments(this));
        }
        this.attachmentNotes = attachmentNotes;
    }

    public Attachments attachmentNotes(Set<AttachmentNotes> attachmentNotes) {
        this.setAttachmentNotes(attachmentNotes);
        return this;
    }

    public Attachments addAttachmentNotes(AttachmentNotes attachmentNotes) {
        this.attachmentNotes.add(attachmentNotes);
        attachmentNotes.getAttachments().add(this);
        return this;
    }

    public Attachments removeAttachmentNotes(AttachmentNotes attachmentNotes) {
        this.attachmentNotes.remove(attachmentNotes);
        attachmentNotes.getAttachments().remove(this);
        return this;
    }

    public Set<Invoice> getInvoices() {
        return this.invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        if (this.invoices != null) {
            this.invoices.forEach(i -> i.removeAttachments(this));
        }
        if (invoices != null) {
            invoices.forEach(i -> i.addAttachments(this));
        }
        this.invoices = invoices;
    }

    public Attachments invoices(Set<Invoice> invoices) {
        this.setInvoices(invoices);
        return this;
    }

    public Attachments addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
        invoice.getAttachments().add(this);
        return this;
    }

    public Attachments removeInvoice(Invoice invoice) {
        this.invoices.remove(invoice);
        invoice.getAttachments().remove(this);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        if (this.purchaseOrders != null) {
            this.purchaseOrders.forEach(i -> i.removeAttachments(this));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.addAttachments(this));
        }
        this.purchaseOrders = purchaseOrders;
    }

    public Attachments purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public Attachments addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.getAttachments().add(this);
        return this;
    }

    public Attachments removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.getAttachments().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachments)) {
            return false;
        }
        return id != null && id.equals(((Attachments) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachments{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", attach='" + getAttach() + "'" +
            ", attachContentType='" + getAttachContentType() + "'" +
            "}";
    }
}
