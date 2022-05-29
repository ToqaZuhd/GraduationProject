package com.example.graduationproject.Model;

import java.io.Serializable;

public class user implements Serializable {
    private int userId;
    private String Name;
    private String phoneNum;
    private String password;
    private String role;
    private int score;
    private String image;
    private String email;

    public user() {
    }

    public user(int userId, String name, String phoneNum, String password, String role, int score, String image, String email) {
        this.userId = userId;
        Name = name;
        this.phoneNum = phoneNum;
        this.password = password;
        this.role = role;
        this.score = score;
        this.image = image;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "user{" +
                "userId=" + userId +
                ", Name='" + Name + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", score=" + score +
                ", image='" + image + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
