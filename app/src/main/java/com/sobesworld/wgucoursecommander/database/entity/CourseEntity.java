package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int courseID;
    private final String courseTitle;
    private final String courseStartDate;
    private final String courseProjectedEndDate;
    private final boolean courseEndAlert;
    private final int courseAlertID;
    private final String courseStatus;
    private final String courseMentorsName;
    private final String courseMentorsPhone;
    private final String courseMentorsEmail;
    private final String courseNotes;
    private final int termID;

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

    @NonNull
    @Override
    public String toString() {
        return "CourseEntity{" +
                "courseID=" + courseID +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseStartDate='" + courseStartDate + '\'' +
                ", courseProjectedEndDate='" + courseProjectedEndDate + '\'' +
                ", courseEndAlert=" + courseEndAlert +
                ", courseAlertID=" + courseAlertID +
                ", courseStatus='" + courseStatus + '\'' +
                ", courseMentorsName='" + courseMentorsName + '\'' +
                ", courseMentorsPhone='" + courseMentorsPhone + '\'' +
                ", courseMentorsEmail='" + courseMentorsEmail + '\'' +
                ", courseNotes='" + courseNotes + '\'' +
                ", termID=" + termID +
                '}';
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getCourseID() {
        return courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getCourseStartDate() {
        return courseStartDate;
    }

    public String getCourseProjectedEndDate() {
        return courseProjectedEndDate;
    }

    public boolean isCourseEndAlert() {
        return courseEndAlert;
    }

    public int getCourseAlertID() {
        return courseAlertID;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public String getCourseMentorsName() {
        return courseMentorsName;
    }

    public String getCourseMentorsPhone() {
        return courseMentorsPhone;
    }

    public String getCourseMentorsEmail() {
        return courseMentorsEmail;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public int getTermID() {
        return termID;
    }
}
