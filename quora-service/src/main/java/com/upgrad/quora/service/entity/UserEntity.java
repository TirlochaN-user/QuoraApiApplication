package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="USER",schema="quora")
public class UserEntity implements Serializable {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="UUID")
    @Size(max=200)
    @NotNull
    private String uuid;

    @Column(name="FIRST_NAME")
    @Size(max=30)
    @NotNull
    private String fname;

    @Column(name="LAST_NAME")
    @Size(max=30)
    @NotNull
    private String lname;

    @Column(name="USERNAME")
    @Size(max=30)
    @NotNull
    private String username;

    @Column(name="EMAIL")
    @Size(max=50)
    @NotNull
    private String email;

    @Column(name="PASSWORD")
    @Size(max=255)
    @NotNull
    private String password;


    @Column(name="SALT")
    @Size(max=200)
    @NotNull
    private String salt;

    @Column(name="COUNTRY")
    @Size(max=30)
    private String country;

    @Column(name="ABOUT_ME")
    @Size(max=50)
    private String aboutMe;

    @Column(name="DOB")
    @Size(max=30)
    private String dob;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Column(name="ROLE")
    @Size(max=30)
    private String role;

    @Column(name="CONTACT_NUMBER")
    @Size(max=30)
    @NotNull
    private String contactNumber;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
