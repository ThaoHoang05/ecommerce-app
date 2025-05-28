package com.stationeryshop.model;


import java.sql.Date;

public class User {
    private String user_id; //varchar(5)
    private String username;
    private String pwd_hash;
    private String role;
    private Date created_at;
    private Date updated_at;
    public User(){}
    public User(String user_id, String username, String pawd_hash, String role, Date created_at, Date updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.pwd_hash = pawd_hash;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    public User(String user_id, String username, String role) {
        this.user_id = user_id;
        this.username = username;
        this.role = role;
    }
    public User(String user_id, String username, String role, Date created_at, Date updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getPwd_hash() {return pwd_hash;}
    public void setPwd_hash(String pwd) {
        this.pwd_hash = pwd;        //Sau nay viet them class de ma hoa pwd
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Date getCreated_at() {
        return created_at;
    }
    public Date getUpdated_at() {
        return updated_at;
    }
}
