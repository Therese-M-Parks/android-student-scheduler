package com.thereseparks.familyintouch.Model.Models;

public abstract class Family_Member {

    //variables
    private long id; //db var is called id //PK
    private int familyMember_Id;
    private String firstName;
    private String lastName;
    private int relation_Id; //FK
    private String phone_1;
    private String phone_2;
    private String email;
    private int birthday_Id; //FK
    private int address_Id; //FK

    public abstract int findRelationId(String relation);

    //Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFamilyMember_Id() {
        return familyMember_Id;
    }

    public void setFamilyMember_Id(int familyMember_Id) {
        this.familyMember_Id = familyMember_Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRelation_Id() {
        return relation_Id;
    }

    public void setRelation_Id(int relation_Id) {
        this.relation_Id = relation_Id;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }

    public String getPhone_2() {
        return phone_2;
    }

    public void setPhone_2(String phone_2) {
        this.phone_2 = phone_2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBirthday_Id() {
        return birthday_Id;
    }

    public void setBirthday_Id(int birthday_Id) {
        this.birthday_Id = birthday_Id;
    }

    public int getAddress_Id() {
        return address_Id;
    }

    public void setAddress_Id(int address_Id) {
        this.address_Id = address_Id;
    }

    //Generated toString()
    @Override
    public String toString() {
        return "Family_Member{" +
                "id=" + id +
                ", familyMember_Id=" + familyMember_Id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", relation_Id=" + relation_Id +
                ", phone_1='" + phone_1 + '\'' +
                ", phone_2='" + phone_2 + '\'' +
                ", email='" + email + '\'' +
                ", birthday_Id=" + birthday_Id +
                ", address_Id=" + address_Id +
                '}';
    }

}
