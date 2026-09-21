package com.example.entity;

public class Vo {
    private Integer uid;
    private String uname;
    private String mail;

    public Integer getUid() { return uid; }
    public void setUid(Integer uid) { this.uid = uid; }
    public String getUname() { return uname; }
    public void setUname(String uname) { this.uname = uname; }
    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    @Override
    public String toString() {
        return "Vo{uid=" + uid + ", uname='" + uname + "', mail='" + mail + "'}";
    }
}