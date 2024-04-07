package com.example.ocenus;

import java.util.ArrayList;
import java.util.List;

public class Kierunek {
    private String courseName;
    private List<Przedmiot> subjects;
    public Kierunek(String courseName) {
        this.courseName = courseName;
        this.subjects = new ArrayList<>();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Przedmiot> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Przedmiot> subjects) {
        this.subjects = subjects;
    }



}
