package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Appintment.
 */
@Entity
@Table(name = "appintment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appintment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "app_date")
    private Instant appDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appintments", "locations" }, allowSetters = true)
    private Client client;

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

    public Appintment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getAppDate() {
        return this.appDate;
    }

    public Appintment appDate(Instant appDate) {
        this.setAppDate(appDate);
        return this;
    }

    public void setAppDate(Instant appDate) {
        this.appDate = appDate;
    }

    public String getNotes() {
        return this.notes;
    }

    public Appintment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Appintment client(Client client) {
        this.setClient(client);
        return this;
    }

    public WorkOrders getWorkOrders() {
        return this.workOrders;
    }

    public void setWorkOrders(WorkOrders workOrders) {
        this.workOrders = workOrders;
    }

    public Appintment workOrders(WorkOrders workOrders) {
        this.setWorkOrders(workOrders);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appintment)) {
            return false;
        }
        return id != null && id.equals(((Appintment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appintment{" +
            "id=" + getId() +
            ", appDate='" + getAppDate() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
