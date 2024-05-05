package com.example.ocenus;

import java.util.ArrayList;
import java.util.List;

public class Przedmiot {
    private String courseName;
    private String subjectName;
    private Integer ECTS;
    private Integer semester;

    private List<Ocena> grades;

    public Przedmiot(String courseName, String subjectName, Integer ECTS, Integer semester) {
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.ECTS = ECTS;
        this.semester = semester;
        this.grades = new ArrayList<>();
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}
