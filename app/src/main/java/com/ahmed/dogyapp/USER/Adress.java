package com.ahmed.dogyapp.USER;

public class Adress {

    private String adress, city, state, country;

    public Adress(String adress, String city, String state, String country) {
        this.adress = adress;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Adress() {
    }

    public String getAdress() {
        return adress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
