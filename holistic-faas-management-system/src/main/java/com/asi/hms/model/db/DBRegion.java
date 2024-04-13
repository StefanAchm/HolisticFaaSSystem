package com.asi.hms.model.db;

import com.asi.hms.enums.Provider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "function_types")
public class DBRegion {

    @Id
    @GeneratedValue
    private UUID id;

    private Provider provider;

    private String code;
    private String name;

    private String continent;
    private String country;
    private String city;







}
