package com.example.apiassignment.Model;

public class Address {
    private String addressID;
    private String name;
    private String street;
    private String suburb;
    private String city;

    public Address() {

    }

    public Address(String addressID, String name, String street, String suburb, String city) {
        this.addressID = addressID;
        this.name = name;
        this.street = street;
        this.suburb = suburb;
        this.city = city;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
