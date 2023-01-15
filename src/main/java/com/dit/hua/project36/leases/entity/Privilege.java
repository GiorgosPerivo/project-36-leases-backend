package com.dit.hua.project36.leases.entity;

import javax.persistence.*;

@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 255)
    private EPrivilege name;

    public Privilege() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EPrivilege getName() {
        return name;
    }

    public void setName(EPrivilege name) {
        this.name = name;
    }
}
