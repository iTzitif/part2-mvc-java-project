package com.university.referral.model;

public abstract class User {
    protected String userID;
    protected String username;
    protected String password;
    protected String name;
    protected String email;
    protected String phone;

    public User(String userID, String username, String password,
                String name, String email, String phone) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public abstract void login();
    public abstract void logout();
    public abstract void updateProfile();

    public String getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }

    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
}