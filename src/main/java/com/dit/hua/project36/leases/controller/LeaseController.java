package com.dit.hua.project36.leases.controller;

import com.dit.hua.project36.leases.dao.LeaseDAO;
import com.dit.hua.project36.leases.entity.Lease;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/leases")
public class LeaseController {

    @Autowired
    private LeaseDAO leaseDAO;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SHOW_LEASES')")
    List<Lease> getAll() {
        return leaseDAO.findAll();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('CREATE_LEASE')")
    Lease save(@RequestBody Lease lease) {
        lease.setId(0);
        leaseDAO.save(lease);
        return lease;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SHOW_LEASES')")
    Lease get(@PathVariable int id) {
        return leaseDAO.findById(id);
    }

}
