package com.dit.hua.project36.leases.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "leases")
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(max = 255)
    private String street;

    @NotBlank
    @Size(max = 255)
    private String city;

    @NotBlank
    @Size(max = 255)
    private String postalCode;

    @NotBlank
    @Size(max = 255)
    private String country;

    @NotBlank
    @Size(max = 10)
    private String electricalId;

    @Column(columnDefinition="TEXT")
    private String details;

    @Column(precision=10, scale=2)
    private Double amount;

    private Boolean isFinalized;

    @ManyToOne
    private User leaser;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "leases_tenants",
            joinColumns = @JoinColumn(name = "lease_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> tenants = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getElectricalId() {
        return electricalId;
    }

    public void setElectricalId(String electricalId) {
        this.electricalId = electricalId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getFinalized() {
        return isFinalized;
    }

    public void setFinalized(Boolean finalized) {
        isFinalized = finalized;
    }

    public User getLeaser() {
        return leaser;
    }

    public void setLeaser(User leaser) {
        this.leaser = leaser;
    }

    public Set<User> getTenants() {
        return tenants;
    }

    public void setTenants(Set<User> tenants) {
        this.tenants = tenants;
    }
}
