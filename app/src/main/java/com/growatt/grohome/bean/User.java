package com.growatt.grohome.bean;

import java.util.List;

/**
 * Describe :
 */
public class User {

    public int id;
    public String password;
    public int type;
    public String accountName;


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
