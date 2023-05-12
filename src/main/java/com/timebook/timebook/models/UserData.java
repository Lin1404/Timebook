package com.timebook.timebook.models;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;

public class UserData extends User{
    private String email;

    public UserData(String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
