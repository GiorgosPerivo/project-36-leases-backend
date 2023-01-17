package com.dit.hua.project36.leases.payload.request;

import com.dit.hua.project36.leases.entity.User;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LeaseRequest {
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

    private String details;

    @Column(precision=10, scale=2)
    private Double amount;

    @NotBlank
    private Long[] tenantIds;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long[] getTenantIds() {
        return tenantIds;
    }

    public void setTenantIds(Long[] tenantIds) {
        this.tenantIds = tenantIds;
    }
}
