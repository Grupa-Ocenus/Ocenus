package com.example.ocenus;

import java.util.ArrayList;
import java.util.List;

public class Uzytkownik {

    private String login, password;
    private DaneUzytkownika dane;
    private List<Kierunek> courses;
    private List<Ocena> grades;

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
        this.courses = new ArrayList<>();
        this.grades = new ArrayList<>();
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

    public List<Kierunek> getCourses() {
        return courses;
    }

    public List<Ocena> getGrades() {
        return grades;
    }

}