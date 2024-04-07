package com.example.ocenus;

public class Ocena {
    private String courseName;
    private String name;
    private Integer grade;

    public Ocena(String courseName, String name, Integer grade) {
        this.courseName = courseName;
        this.name = name;
        this.grade = grade;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
