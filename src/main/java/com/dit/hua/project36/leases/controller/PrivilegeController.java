package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.entity.EPrivilege;
import com.dit.hua.project36.leases.entity.Privilege;
import com.dit.hua.project36.leases.entity.User;
import com.dit.hua.project36.leases.payload.request.PrivilegeRequest;
import com.dit.hua.project36.leases.payload.response.MessageResponse;
import com.dit.hua.project36.leases.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/privileges")
public class PrivilegeController {

    @Autowired
    PrivilegeRepository privilegeRepository;

    @PostMapping("")
    @PreAuthorize("hasAuthority('MANAGE_PRIVILEGES')")
    public ResponseEntity<?> createPrivilege(@Valid @RequestBody PrivilegeRequest createPrivilegeRequest) {
        if (privilegeRepository.existsByName(createPrivilegeRequest.getName().toUpperCase())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Error: Privilege name already exists!"));
        }


        Privilege newPrivilege = new Privilege();

        newPrivilege.setName(createPrivilegeRequest.getName());

        privilegeRepository.save(newPrivilege);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Privilege created successfully!"));
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('MANAGE_PRIVILEGES')")
    public ResponseEntity<?> getPrivileges() {
        return ResponseEntity.ok(privilegeRepository.findAll());
    }

    @PutMapping("/{privilegeId}")
    @PreAuthorize("hasAuthority('MANAGE_PRIVILEGES')")
    public ResponseEntity<?> updatePrivilege(@PathVariable Long privilegeId, @Valid @RequestBody PrivilegeRequest updatePrivilegeRequest) {

        Optional<Privilege> privilegeFound = privilegeRepository.findById(privilegeId);

        if (privilegeFound.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Privilege with this id does not exist"));
        }

        Privilege privilege = privilegeFound.get();

        privilege.setName(updatePrivilegeRequest.getName().toUpperCase());

        privilegeRepository.save(privilege);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Privilege update successfully!"));
    }

    @DeleteMapping("/{privilegeId}")
    @PreAuthorize("hasAuthority('MANAGE_PRIVILEGES')")
    public ResponseEntity<?> removePrivilege(@PathVariable Long privilegeId) {
        Optional<Privilege> privilege = privilegeRepository.findById(privilegeId);

        if (privilege.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Privilege with this id does not exist"));
        }

        privilegeRepository.delete(privilege.get());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Privilege removed successfully!"));
    }

}
