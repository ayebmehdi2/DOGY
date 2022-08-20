package com.ahmed.dogyapp.USER;

public class ProfilData {


    private String UID;
    private String username;
    private String email;
    private String password;
    private String image;
    private String phone;

    public ProfilData() {
    }

    public ProfilData(String UID, String username, String email, String password, String img, String phone) {
        this.username = username;
        this.email = email;
        this.password = password;
        image = img;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
