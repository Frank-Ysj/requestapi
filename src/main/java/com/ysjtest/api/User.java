package com.ysjtest.api;

public class User {
    private String phone;
    private String password;
    private String nickname;
    private int state;
    private String sign;
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getSign() {
        return sign;
    }

 /*   @Override
    public String toString() {
        return "NickName [name=" + this.getNickname() + "]";
    }*/

}
