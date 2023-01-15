package com.dit.hua.project36.leases.repository;

import com.dit.hua.project36.leases.entity.Lease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {

}