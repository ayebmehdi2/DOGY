package com.ahmed.dogyapp;

public class SERVICES {

    private boolean traning = false, hotel = false, vetirinary = false;
    private String time, date;
    private String adress, number, supllierId;

    public SERVICES(){ }

    public SERVICES(boolean traning, boolean hotel, boolean vetirinary, String time, String date,
                    String adress, String number, String supllierId) {
        this.traning = traning;
        this.hotel = hotel;
        this.vetirinary = vetirinary;
        this.time = time;
        this.date = date;
        this.adress = adress;
        this.number = number;
        this.supllierId = supllierId;
    }

    public String getSupllierId() {
        return supllierId;
    }

    public void setSupllierId(String supllierId) {
        this.supllierId = supllierId;
    }

    public void setTraning(boolean traning) {
        this.traning = traning;
    }

    public void setHotel(boolean hotel) {
        this.hotel = hotel;
    }

    public void setVetirinary(boolean vetirinary) {
        this.vetirinary = vetirinary;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isTraning() {
        return traning;
    }

    public boolean isHotel() {
        return hotel;
    }

    public boolean isVetirinary() {
        return vetirinary;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getAdress() {
        return adress;
    }

    public String getNumber() {
        return number;
    }

}
