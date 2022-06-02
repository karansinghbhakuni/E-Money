package com.dev.e_money.model;

public class user {
     String email,name,phoneno,uid,balance;

    public user(String email, String name, String phoneno, String uid, String balance) {
        this.email = email;
        this.name = name;
        this.phoneno = phoneno;
        this.uid = uid;
        this.balance = balance;
    }

    public user() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
