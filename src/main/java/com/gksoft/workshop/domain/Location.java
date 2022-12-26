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
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "lang")
    private String lang;

    @Column(name = "lat")
    private String lat;

    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @ManyToMany(mappedBy = "locations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appintments", "locations" }, allowSetters = true)
    private Set<Client> clients = new HashSet<>();

    @ManyToMany(mappedBy = "locations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locations" }, allowSetters = true)
    private Set<Supplier> suppliers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public Location streetAddress(String streetAddress) {
        this.setStreetAddress(streetAddress);
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public Location stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getLang() {
        return this.lang;
    }

    public Location lang(String lang) {
        this.setLang(lang);
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLat() {
        return this.lat;
    }

    public Location lat(String lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Location country(Country country) {
        this.setCountry(country);
        return this;
    }

    public Set<Client> getClients() {
        return this.clients;
    }

    public void setClients(Set<Client> clients) {
        if (this.clients != null) {
            this.clients.forEach(i -> i.removeLocation(this));
        }
        if (clients != null) {
            clients.forEach(i -> i.addLocation(this));
        }
        this.clients = clients;
    }

    public Location clients(Set<Client> clients) {
        this.setClients(clients);
        return this;
    }

    public Location addClient(Client client) {
        this.clients.add(client);
        client.getLocations().add(this);
        return this;
    }

    public Location removeClient(Client client) {
        this.clients.remove(client);
        client.getLocations().remove(this);
        return this;
    }

    public Set<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        if (this.suppliers != null) {
            this.suppliers.forEach(i -> i.removeLocation(this));
        }
        if (suppliers != null) {
            suppliers.forEach(i -> i.addLocation(this));
        }
        this.suppliers = suppliers;
    }

    public Location suppliers(Set<Supplier> suppliers) {
        this.setSuppliers(suppliers);
        return this;
    }

    public Location addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.getLocations().add(this);
        return this;
    }

    public Location removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getLocations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", lang='" + getLang() + "'" +
            ", lat='" + getLat() + "'" +
            "}";
    }
}
