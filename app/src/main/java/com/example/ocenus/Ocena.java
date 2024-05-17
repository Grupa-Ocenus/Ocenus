package com.example.ocenus;

public class Ocena {
    private String courseName;
    private String subjectName;
    private String name;
    private Integer grade;
    private RodzajOceny gradeType;
    private Integer ECTS;

    public Ocena(String courseName,String subjectName, String name, Integer grade, RodzajOceny gradeType) {
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.name = name;
        this.grade = grade;
        this.gradeType = gradeType;
       // this.ECTS = ECTS;
    }

    public Integer getECTS(){return ECTS; }
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
