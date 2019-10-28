package com.example.tba_osa_2019;


import java.util.Date;

/**
 * Created by yonas on 4/20/2019.
 */

public class Customer {

    String email;
    String password;
    String dob;
    String address;
    Integer ID;

    public Customer(Integer id, String email, String password, String dob, String address) {

        this.email = email;
        this.password = password;
        this.dob = dob;
        this.password = password;
        this.address = address;

        this.ID = id;
    }

}