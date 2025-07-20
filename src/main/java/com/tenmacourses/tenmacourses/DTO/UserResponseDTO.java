package com.tenmacourses.tenmacourses.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tenmacourses.tenmacourses.Entity.Users;
import com.tenmacourses.tenmacourses.Enums.Role;

import java.math.BigDecimal;

public class UserResponseDTO {
    private int id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private BigDecimal balance ;
    private Role role;

    public UserResponseDTO(Users user) {
        this.id = user.getId();
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.balance = user.getBalance();

    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UserResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}