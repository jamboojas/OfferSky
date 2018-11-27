package com.example.abhiraj.offersky.model;

/**
 * Created by Abhiraj on 18-04-2017.
 */

public class User {

    private String name;
    private String age;
    private String gender;
    private String phone;
    private String uid;
    private String email;

    public User() {
    }

    public User(String name, String age, String gender, String uid) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
