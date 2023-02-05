package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.entity.ERole;
import com.dit.hua.project36.leases.entity.Lease;
import com.dit.hua.project36.leases.entity.User;
import com.dit.hua.project36.leases.payload.request.LeaseAddDetailsRequest;
import com.dit.hua.project36.leases.payload.request.LeaseRequest;
import com.dit.hua.project36.leases.payload.response.MessageResponse;
import com.dit.hua.project36.leases.repository.LeaseRepository;
import com.dit.hua.project36.leases.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/leases")
public class LeaseController {

    @Autowired
    private LeaseRepository leaseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SHOW_LEASES')")
    ResponseEntity<?> getAll(HttpServletRequest request) {
        User user = userRepository.findByUsername(request.getUserPrincipal().getName()).orElseThrow();

        if (user.getRole().getName().equals(ERole.ROLE_LEASER.name())) {
            return ResponseEntity.ok(leaseRepository.findAllByLeaserId(user.getId()));
        }

        if (user.getRole().getName().equals(ERole.ROLE_TENANT.name())) {
            return ResponseEntity.ok(leaseRepository.findByTenantsId(user.getId()));
        }

        return ResponseEntity.ok(leaseRepository.findAll());
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('CREATE_LEASE')")
    ResponseEntity<?> save(HttpServletRequest request, @RequestBody LeaseRequest lease) {
        Lease newLease = new Lease();

        Optional<User> leaser = userRepository.findByUsername(request.getUserPrincipal().getName());
        if(leaser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: User with this id does not exist"));
        }

        List<User> userTenants = userRepository.findAllById(Arrays.asList(lease.getTenantIds()));

        List<User> tenants = userTenants.stream().filter(t -> t.getRole().getName().equals(ERole.ROLE_TENANT.name())).toList();

        if (tenants.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: You have not specified any tenants"));
        }

        newLease.setCity(lease.getCity());
        newLease.setCountry(lease.getCountry());
        newLease.setStreet(lease.getStreet());
        newLease.setElectricalId(lease.getElectricalId());
        newLease.setPostalCode(lease.getPostalCode());
        newLease.setAmount(lease.getAmount());
        newLease.setLeaser(leaser.get());
        newLease.setTenants(new HashSet<>(tenants));

        leaseRepository.save(newLease);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Lease created successfully!"));
    }


    @PutMapping("/{leaseId}/add-details")
    @PreAuthorize("hasAuthority('ADD_DETAILS_TO_LEASE')")
    ResponseEntity<?> addDetails(@PathVariable Long leaseId, @RequestBody LeaseAddDetailsRequest detailsRequest) {
        Optional<Lease> foundLease = leaseRepository.findById(leaseId);

        if (foundLease.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Lease with this id does not exist"));
        }

        Lease lease = foundLease.get();

        lease.setDetails(detailsRequest.getDetails());

        leaseRepository.save(lease);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Lease updated successfully!"));
    }

    @PutMapping("/{leaseId}/accept")
    @PreAuthorize("hasAuthority('ACCEPT_LEASE')")
    ResponseEntity<?> acceptLease(@PathVariable Long leaseId) {
        Optional<Lease> foundLease = leaseRepository.findById(leaseId);

        if (foundLease.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Lease with this id does not exist"));
        }

        Lease lease = foundLease.get();

        lease.setFinalized(true);

        leaseRepository.save(lease);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Lease accepted successfully!"));
    }

    @GetMapping("/{leaseId}")
    @PreAuthorize("hasAuthority('SHOW_LEASES')")
    ResponseEntity<?> get(@PathVariable Long leaseId) {
        Optional<Lease> lease = leaseRepository.findById(leaseId);

        if (lease.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Lease with this id does not exist"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(lease);

    }

}
