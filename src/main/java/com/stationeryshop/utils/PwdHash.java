package com.stationeryshop.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PwdHash {
    private static final int SALT_ROUNDS = 10;
    private String pwd;
    private String hash;
    public PwdHash(String pwd){
        this.pwd = pwd;
    }
    public String getHash(){
        hash = BCrypt.hashpw(pwd, BCrypt.gensalt(SALT_ROUNDS));
        return hash;
    }
    public boolean verify(String pwd_hash){
        return BCrypt.checkpw(pwd, pwd_hash);
    }
}
