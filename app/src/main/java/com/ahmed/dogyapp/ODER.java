package com.ahmed.dogyapp;

public class ODER {

    private String date, service, supplier;

    public ODER() {
    }

    public ODER(String date, String service, String supplier) {
        this.date = date;
        this.service = service;
        this.supplier = supplier;
    }



    public String getDate() {
        return date;
    }

    public String getService() {
        return service;
    }

    public String getSupplier() {
        return supplier;
    }
}
