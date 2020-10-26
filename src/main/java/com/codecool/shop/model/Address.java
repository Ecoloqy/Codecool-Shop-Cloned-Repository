package com.codecool.shop.model;

public class Address extends BaseModel {
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private int localNumber;

    public Address(String country, String city, String zipCode, String street, int localNumber) {
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.street = street;
        this.localNumber = localNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(int localNumber) {
        this.localNumber = localNumber;
    }

    @Override
    public String toString() {
        return String.format("id: %1$s, " +
                        "country: %2$s, " +
                        "city: %3$s, " +
                        "zipCode: %4$s, " +
                        "street: %5$s, " +
                        "localNumber: %6$d",
                this.id,
                this.country,
                this.city,
                this.zipCode,
                this.street,
                this.localNumber);
    }
}
