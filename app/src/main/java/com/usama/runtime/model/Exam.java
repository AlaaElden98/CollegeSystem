package com.usama.runtime.model;

public class Exam {
    private String Chapter_one, Chapter_two, Chapter_three, Chapter_four, Chapter_five, Chapter_sex, degree_of_exam, department, level, num_of_question, subject;

    public Exam(String chapter_one, String chapter_two, String chapter_three, String chapter_four, String chapter_five, String chapter_sex, String degree_of_exam, String department, String level, String num_of_question, String subject) {
        Chapter_one = chapter_one;
        Chapter_two = chapter_two;
        Chapter_three = chapter_three;
        Chapter_four = chapter_four;
        Chapter_five = chapter_five;
        Chapter_sex = chapter_sex;
        this.degree_of_exam = degree_of_exam;
        this.department = department;
        this.level = level;
        this.num_of_question = num_of_question;
        this.subject = subject;
    }

    public Exam() {
    }

    public String getChapter_one() {
        return Chapter_one;
    }

    public void setChapter_one(String chapter_one) {
        Chapter_one = chapter_one;
    }

    public String getChapter_two() {
        return Chapter_two;
    }

    public void setChapter_two(String chapter_two) {
        Chapter_two = chapter_two;
    }

    public String getChapter_three() {
        return Chapter_three;
    }

    public void setChapter_three(String chapter_three) {
        Chapter_three = chapter_three;
    }

    public String getChapter_four() {
        return Chapter_four;
    }

    public void setChapter_four(String chapter_four) {
        Chapter_four = chapter_four;
    }

    public String getChapter_five() {
        return Chapter_five;
    }

    public void setChapter_five(String chapter_five) {
        Chapter_five = chapter_five;
    }

    public String getChapter_sex() {
        return Chapter_sex;
    }

    public void setChapter_sex(String chapter_sex) {
        Chapter_sex = chapter_sex;
    }

    public String getDegree_of_exam() {
        return degree_of_exam;
    }

    public void setDegree_of_exam(String degree_of_exam) {
        this.degree_of_exam = degree_of_exam;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getNum_of_question() {
        return num_of_question;
    }

    public void setNum_of_question(String num_of_question) {
        this.num_of_question = num_of_question;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
