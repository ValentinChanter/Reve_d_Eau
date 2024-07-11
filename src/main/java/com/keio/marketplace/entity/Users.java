package com.keio.marketplace.entity;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Users {
    private String email;
    private String password;
    private String name;
    private Boolean isAdmin;
    private int loyaltyPoints;
    private String cart;
    private long id;

    public Users() {

    }

    // Constructor when creating a user after fetching it from database (NOT for creating a new user - pw is not hashed)
    public Users(String email, String name, String password, Boolean isAdmin, int loyaltyPoints, String cart, long id) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.isAdmin = isAdmin;
        this.loyaltyPoints = loyaltyPoints;
        this.cart = cart;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getCart() {
        return cart;
    }

    public void setCart(String cart) {
        this.cart = cart;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        return id == users.id;
    }
}
