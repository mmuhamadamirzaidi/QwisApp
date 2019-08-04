package com.mmuhamadamirzaidi.qwisapp.Model;

public class User {

    private String Username, Email, Password, Role;

    public User() {
    }

    public User(String username, String email, String password, String role) {
        Username = username;
        Email = email;
        Password = password;
        Role = role;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}