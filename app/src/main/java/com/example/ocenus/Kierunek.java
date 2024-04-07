package com.example.ocenus;

import java.util.List;

public class Kierunek {
    private String courseName;
    private List<Przedmiot> subjects;
    public Kierunek(String courseName, List<Przedmiot> subjects) {
        this.courseName = courseName;
        this.subjects = subjects;
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
