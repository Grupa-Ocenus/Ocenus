package com.example.ocenus;

public class Uzytkownik {

    private String login, password;
    private DaneUzytkownika dane;

    private String imageURL;

    public Uzytkownik()
    {}
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Uzytkownik(String imageURL) {
        this.imageURL = imageURL;
    }

    public Uzytkownik(String login, String password, DaneUzytkownika dane) {
        this.login = login;
        this.password = password;
        this.dane = dane;
    }

    public DaneUzytkownika getDane() {
        return dane;
    }

    public void setDane(DaneUzytkownika dane) {
        this.dane = dane;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}