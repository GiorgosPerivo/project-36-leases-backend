package com.dit.hua.project36.leases.repository;

import com.dit.hua.project36.leases.entity.EPrivilege;
import com.dit.hua.project36.leases.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findByName(EPrivilege name);
}