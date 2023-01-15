package com.dit.hua.project36.leases.dao;

import com.dit.hua.project36.leases.entity.Lease;

import java.util.List;

public interface LeaseDAO {

    public List<Lease> findAll();
    public void save(Lease course);

    public Lease findById(int id);
}
