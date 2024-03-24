package com.example.ocenus;

public class DaneUzytkownika {
   private String name;
   private String surname;

    public DaneUzytkownika(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public DaneUzytkownika() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
