package com.sobesworld.wgucoursecommander.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @PrimaryKey(autoGenerate = true)
    private int courseID;

    private String courseTitle;
    private String courseStartDate;
    private boolean courseStartAlert;
    private String courseProjectedEndDate;
    private boolean courseEndAlert;
    private String courseStatus;
    private String courseMentorsName;
    private String courseMentorsPhone;
    private String courseMentorsEmail;
    private String courseNotes;
    private int termID;

    public Course(int courseID, String courseTitle, String courseStartDate, boolean courseStartAlert, String courseProjectedEndDate, boolean courseEndAlert, String courseStatus, String courseMentorsName, String courseMentorsPhone, String courseMentorsEmail, String courseNotes, int termID) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseStartAlert = courseStartAlert;
        this.courseProjectedEndDate = courseProjectedEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseStatus = courseStatus;
        this.courseMentorsName = courseMentorsName;
        this.courseMentorsPhone = courseMentorsPhone;
        this.courseMentorsEmail = courseMentorsEmail;
        this.courseNotes = courseNotes;
        this.termID = termID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseStartDate='" + courseStartDate + '\'' +
                ", courseStartAlert=" + courseStartAlert +
                ", courseProjectedEndDate='" + courseProjectedEndDate + '\'' +
                ", courseEndAlert=" + courseEndAlert +
                ", courseStatus='" + courseStatus + '\'' +
                ", courseMentorsName='" + courseMentorsName + '\'' +
                ", courseMentorsPhone='" + courseMentorsPhone + '\'' +
                ", courseMentorsEmail='" + courseMentorsEmail + '\'' +
                ", courseNotes='" + courseNotes + '\'' +
                ", termID=" + termID +
                '}';
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public boolean isCourseStartAlert() {
        return courseStartAlert;
    }

    public void setCourseStartAlert(boolean courseStartAlert) {
        this.courseStartAlert = courseStartAlert;
    }

    public String getCourseProjectedEndDate() {
        return courseProjectedEndDate;
    }

    public void setCourseProjectedEndDate(String courseProjectedEndDate) {
        this.courseProjectedEndDate = courseProjectedEndDate;
    }

    public boolean isCourseEndAlert() {
        return courseEndAlert;
    }

    public void setCourseEndAlert(boolean courseEndAlert) {
        this.courseEndAlert = courseEndAlert;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getCourseMentorsName() {
        return courseMentorsName;
    }

    public void setCourseMentorsName(String courseMentorsName) {
        this.courseMentorsName = courseMentorsName;
    }

    public String getCourseMentorsPhone() {
        return courseMentorsPhone;
    }

    public void setCourseMentorsPhone(String courseMentorsPhone) {
        this.courseMentorsPhone = courseMentorsPhone;
    }

    public String getCourseMentorsEmail() {
        return courseMentorsEmail;
    }

    public void setCourseMentorsEmail(String courseMentorsEmail) {
        this.courseMentorsEmail = courseMentorsEmail;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(String courseNotes) {
        this.courseNotes = courseNotes;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }
}
