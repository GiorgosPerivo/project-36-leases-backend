package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.entity.Privilege;
import com.dit.hua.project36.leases.entity.Role;
import com.dit.hua.project36.leases.payload.request.RoleRequest;
import com.dit.hua.project36.leases.payload.response.MessageResponse;
import com.dit.hua.project36.leases.repository.PrivilegeRepository;
import com.dit.hua.project36.leases.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeRepository privilegeRepository;

    @PostMapping("")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleRequest createRoleRequest) {
        if (roleRepository.existsByName(createRoleRequest.getName().toUpperCase())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Role name already exists!"));
        }


        Role newRole = new Role();

        newRole.setName(createRoleRequest.getName());

        List<Privilege> privileges = privilegeRepository.findAllById(createRoleRequest.getPrivilegeIds());

        newRole.setPrivileges(new HashSet<>(privileges));

        roleRepository.save(newRole);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Role created successfully!"));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<?> updateRole(@PathVariable Long roleId, @Valid @RequestBody RoleRequest updateRoleRequest) {

        Optional<Role> roleFound = roleRepository.findById(roleId);

        if (roleFound.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Role with this id does not exist"));
        }


        Role role = roleFound.get();

        role.setName(updateRoleRequest.getName().toUpperCase());

        List<Privilege> privileges = privilegeRepository.findAllById(updateRoleRequest.getPrivilegeIds());

        role.setPrivileges(new HashSet<>(privileges));

        roleRepository.save(role);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role updated successfully!"));
    }

    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    public ResponseEntity<?> removeRole(@PathVariable Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        if (role.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Role with this id does not exist"));
        }

        roleRepository.delete(role.get());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Role removed successfully!"));
    }

}
