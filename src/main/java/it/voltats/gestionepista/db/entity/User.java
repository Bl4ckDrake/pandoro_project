package it.voltats.gestionepista.db.entity;

import java.util.Date;

public class User {
    private int id;
    private String name;
    private String surname;
    private String cf;
    private String email;
    private String phoneNumber;
    private Date birthdate;

    public User(String name, String surname, String cf, String email, String phoneNumber, Date birthdate) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthdate = birthdate;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date newDate) {
        this.birthdate = newDate;
    }
}
