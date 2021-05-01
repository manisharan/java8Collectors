/*
 * Copyright (C) Lowe's Companies, Inc. All rights reserved.
 * This file is for internal use only at Lowe's Companies, Inc.
 */

package com.model;

import lombok.Data;

/**
 * @author msharan
 */
@Data
public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;
    private int age;
}

@Data
class Address {
    private String street;
    private String suite;
    private String city;
    private String zipcode;
    private Geo geo;
}

@Data
class Geo {
    private String lat;
    private String lng;
}

@Data
class Company {
    private String name;
    private String catchPhrase;
    private String bs;
}
