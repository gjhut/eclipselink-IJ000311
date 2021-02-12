package com.eclipselink.test.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "test_table")

@NamedQuery(name = "jpaQuery", query = "select r from TestEntity r")
@NamedNativeQuery(name = "nativeSqlQuery", query = "select r.* from test_table r",resultClass = TestEntity.class)
public class TestEntity implements Serializable {

    @Id
    int     id;

    @Column
    private String naam;

    public String getNaam() {
        return naam;
    }
}
