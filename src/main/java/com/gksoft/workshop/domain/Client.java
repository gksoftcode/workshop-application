package com.gksoft.workshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "client", "workOrders" }, allowSetters = true)
    private Set<Appintment> appintments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_client__location",
        joinColumns = @JoinColumn(name = "client_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "clients", "suppliers" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Client id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Client fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public Client mobileNo(String mobileNo) {
        this.setMobileNo(mobileNo);
        return this;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return this.email;
    }

    public Client email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Client phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Appintment> getAppintments() {
        return this.appintments;
    }

    public void setAppintments(Set<Appintment> appintments) {
        if (this.appintments != null) {
            this.appintments.forEach(i -> i.setClient(null));
        }
        if (appintments != null) {
            appintments.forEach(i -> i.setClient(this));
        }
        this.appintments = appintments;
    }

    public Client appintments(Set<Appintment> appintments) {
        this.setAppintments(appintments);
        return this;
    }

    public Client addAppintment(Appintment appintment) {
        this.appintments.add(appintment);
        appintment.setClient(this);
        return this;
    }

    public Client removeAppintment(Appintment appintment) {
        this.appintments.remove(appintment);
        appintment.setClient(null);
        return this;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Client locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public Client addLocation(Location location) {
        this.locations.add(location);
        location.getClients().add(this);
        return this;
    }

    public Client removeLocation(Location location) {
        this.locations.remove(location);
        location.getClients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", mobileNo='" + getMobileNo() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
