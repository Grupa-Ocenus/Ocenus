package com.example.ocenus;

import java.util.List;

public class Przedmiot {
    private String subjectName;
    private Integer ECTS;
    private List<Ocena> grades;

    public Przedmiot(String subjectName, Integer ECTS, List<Ocena> grades) {
        this.subjectName = subjectName;
        this.ECTS = ECTS;
        this.grades = grades;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getECTS() {
        return ECTS;
    }

    public void setECTS(Integer ECTS) {
        this.ECTS = ECTS;
    }

    public List<Ocena> getGrades() {
        return grades;
    }

    public void setGrades(List<Ocena> grades) {
        this.grades = grades;
    }
}
