package com.dit.hua.project36.leases.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PrivilegeRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
