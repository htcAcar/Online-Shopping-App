package com.example.tba_osa_2019;

public class Seller {
    int seller_ID;
    String email;
    String password;

    public Seller(Integer id, String email, String password) {

        this.email = email;
        this.password = password;
        this.seller_ID = id;
    }
}
