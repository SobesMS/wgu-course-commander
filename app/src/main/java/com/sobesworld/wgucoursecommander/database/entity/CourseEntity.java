package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private String courseTitle;
    private String courseStartDate;
    private String courseProjectedEndDate;
    private boolean courseEndAlert;
    private int courseAlertID;
    private String courseStatus;
    private String courseMentorsName;
    private String courseMentorsPhone;
    private String courseMentorsEmail;
    private String courseNotes;
    private int termID;

    // this constructor is for new database entries; auto-generates primary key
    public CourseEntity(@NonNull String courseTitle, String courseStartDate, String courseProjectedEndDate,
                        boolean courseEndAlert, int courseAlertID, String courseStatus, String courseMentorsName,
                        String courseMentorsPhone, String courseMentorsEmail, String courseNotes, int termID) {
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseProjectedEndDate = courseProjectedEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseAlertID = courseAlertID;
        this.courseStatus = courseStatus;
        this.courseMentorsName = courseMentorsName;
        this.courseMentorsPhone = courseMentorsPhone;
        this.courseMentorsEmail = courseMentorsEmail;
        this.courseNotes = courseNotes;
        this.termID = termID;
    }

    // this constructor is for updating existing database entries; carries over primary key
    @Ignore
    public CourseEntity(int courseID, @NonNull String courseTitle, String courseStartDate, String courseProjectedEndDate,
                        boolean courseEndAlert, int courseAlertID, String courseStatus, String courseMentorsName,
                        String courseMentorsPhone, String courseMentorsEmail, String courseNotes, int termID) {
        this.courseID = courseID;
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseProjectedEndDate = courseProjectedEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseAlertID = courseAlertID;
        this.courseStatus = courseStatus;
        this.courseMentorsName = courseMentorsName;
        this.courseMentorsPhone = courseMentorsPhone;
        this.courseMentorsEmail = courseMentorsEmail;
        this.courseNotes = courseNotes;
        this.termID = termID;
    }

    @NonNull
    @Override
    public String toString() {
        return courseTitle;
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

    public int getCourseAlertID() {
        return courseAlertID;
    }

    public void setCourseAlertID(int courseAlertID) {
        this.courseAlertID = courseAlertID;
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
