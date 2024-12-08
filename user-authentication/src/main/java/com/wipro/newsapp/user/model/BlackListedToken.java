package com.wipro.newsapp.user.model;

import jakarta.persistence.*;

@Entity
@Table(name = "black_listed_tokens")
public class BlackListedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long token_id;

    private String token;

    public BlackListedToken() {

    }

    public BlackListedToken(long token_id, String token) {
        super();
        this.token_id = token_id;
        this.token = token;
    }

    public long getToken_id() {
        return token_id;
    }

    public void setToken_id(long token_id) {
        this.token_id = token_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
