package com.dit.hua.project36.leases.repository;

import com.dit.hua.project36.leases.entity.ERole;
import com.dit.hua.project36.leases.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}