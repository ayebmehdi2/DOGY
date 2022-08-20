package com.ahmed.dogyapp;

public class Photo {

    private String photo;
    private String desc;
    private String id;

    public Photo() {
    }

    public Photo(String id, String photo, String desc) {
        this.photo = photo;
        this.id = id;
        this.desc = desc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
