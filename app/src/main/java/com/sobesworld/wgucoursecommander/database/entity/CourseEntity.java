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
    private final boolean courseStartAlert;
    private final int courseStartAlertID;
    private final String courseEndDate;
    private final boolean courseEndAlert;
    private final int courseEndAlertID;
    private final String courseStatus;
    private final String courseMentorsName;
    private final String courseMentorsPhone;
    private final String courseMentorsEmail;
    private final String courseNotes;
    private final int courseLinkedTermID;

    public CourseEntity(String courseTitle, String courseStartDate, boolean courseStartAlert, int courseStartAlertID,
                        String courseEndDate, boolean courseEndAlert, int courseEndAlertID, String courseStatus, String courseMentorsName,
                        String courseMentorsPhone, String courseMentorsEmail, String courseNotes, int courseLinkedTermID) {
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseStartAlert = courseStartAlert;
        this.courseStartAlertID = courseStartAlertID;
        this.courseEndDate = courseEndDate;
        this.courseEndAlert = courseEndAlert;
        this.courseEndAlertID = courseEndAlertID;
        this.courseStatus = courseStatus;
        this.courseMentorsName = courseMentorsName;
        this.courseMentorsPhone = courseMentorsPhone;
        this.courseMentorsEmail = courseMentorsEmail;
        this.courseNotes = courseNotes;
        this.courseLinkedTermID = courseLinkedTermID;
    }

    @NonNull
    @Override
    public String toString() {
        return courseTitle;
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

    public boolean isCourseStartAlert() {
        return courseStartAlert;
    }

    public int getCourseStartAlertID() {
        return courseStartAlertID;
    }

    public String getCourseEndDate() {
        return courseEndDate;
    }

    public boolean isCourseEndAlert() {
        return courseEndAlert;
    }

    public int getCourseEndAlertID() {
        return courseEndAlertID;
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

    public int getCourseLinkedTermID() {
        return courseLinkedTermID;
    }
}
