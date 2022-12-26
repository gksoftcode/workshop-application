package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttachmentNotes.
 */
@Entity
@Table(name = "attachment_notes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentNotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_shared")
    private Boolean isShared;

    @Column(name = "action_date")
    private Instant actionDate;

    @Column(name = "note")
    private String note;

    @OneToOne
    @JoinColumn(unique = true)
    private Status status;

    @OneToOne
    @JoinColumn(unique = true)
    private Action action;

    @ManyToMany
    @JoinTable(
        name = "rel_attachment_notes__attachments",
        joinColumns = @JoinColumn(name = "attachment_notes_id"),
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

    public AttachmentNotes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsShared() {
        return this.isShared;
    }

    public AttachmentNotes isShared(Boolean isShared) {
        this.setIsShared(isShared);
        return this;
    }

    public void setIsShared(Boolean isShared) {
        this.isShared = isShared;
    }

    public Instant getActionDate() {
        return this.actionDate;
    }

    public AttachmentNotes actionDate(Instant actionDate) {
        this.setActionDate(actionDate);
        return this;
    }

    public void setActionDate(Instant actionDate) {
        this.actionDate = actionDate;
    }

    public String getNote() {
        return this.note;
    }

    public AttachmentNotes note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public AttachmentNotes status(Status status) {
        this.setStatus(status);
        return this;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public AttachmentNotes action(Action action) {
        this.setAction(action);
        return this;
    }

    public Set<Attachments> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(Set<Attachments> attachments) {
        this.attachments = attachments;
    }

    public AttachmentNotes attachments(Set<Attachments> attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public AttachmentNotes addAttachments(Attachments attachments) {
        this.attachments.add(attachments);
        attachments.getAttachmentNotes().add(this);
        return this;
    }

    public AttachmentNotes removeAttachments(Attachments attachments) {
        this.attachments.remove(attachments);
        attachments.getAttachmentNotes().remove(this);
        return this;
    }

    public WorkOrders getWorkOrders() {
        return this.workOrders;
    }

    public void setWorkOrders(WorkOrders workOrders) {
        this.workOrders = workOrders;
    }

    public AttachmentNotes workOrders(WorkOrders workOrders) {
        this.setWorkOrders(workOrders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentNotes)) {
            return false;
        }
        return id != null && id.equals(((AttachmentNotes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentNotes{" +
            "id=" + getId() +
            ", isShared='" + getIsShared() + "'" +
            ", actionDate='" + getActionDate() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
