package com.dit.hua.project36.leases.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RoleRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    private List<Long> privilegeIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getPrivilegeIds() {
        return privilegeIds;
    }

    public void setPrivilegeIds(List<Long> privilegeIds) {
        this.privilegeIds = privilegeIds;
    }
}
