package com.example.ecabs.Utils;

public class UserHelper {

    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
    String age;
    String contact;
    String address;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserHelper(String email,String name, String age, String contact, String address, String password){
        this.email = email;
        this.name = name;
        this.age = age;
        this.contact = contact;
        this.address = address;
        this.password = password;
    }
    public UserHelper(){

    }
}
