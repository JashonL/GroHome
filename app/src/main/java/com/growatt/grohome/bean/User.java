package com.growatt.grohome.bean;

/**
 * Describe :
 */
public class User {

    public int id;
    public String password;
    public int type;
    public String accountName;
    public String url;
    public String email;
    public String userTuyaCode;//用户的国家区号，只有国内和法国，不能变


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserTuyaCode() {
        return userTuyaCode;
    }

    public void setUserTuyaCode(String userTuyaCode) {
        this.userTuyaCode = userTuyaCode;
    }
}
