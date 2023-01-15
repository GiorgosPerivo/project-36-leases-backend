package com.dit.hua.project36.leases.dao;

import com.dit.hua.project36.leases.entity.Lease;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class LeaseDAOImpl implements LeaseDAO {

    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public List<Lease> findAll() {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Lease", Lease.class);
        List<Lease> leases = query.getResultList();
        return leases;
    }

    @Override
    @Transactional
    public void save(Lease lease) {
       entityManager.merge(lease);
    }

    @Override
    @Transactional
    public Lease findById(int id) {
        return entityManager.find(Lease.class, id);
    }
}
