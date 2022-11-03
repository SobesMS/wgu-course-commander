package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "courseID")
    private int courseID;

    @ColumnInfo(name = "courseTitle")
    private final String courseTitle;

    @ColumnInfo(name = "courseStartDate")
    private final String courseStartDate;

    @ColumnInfo(name = "courseStartAlert")
    private final boolean courseStartAlert;

    @ColumnInfo(name = "courseStartAlertID")
    private final int courseStartAlertID;

    @ColumnInfo(name = "courseEndDate")
    private final String courseEndDate;

    @ColumnInfo(name = "courseEndAlert")
    private final boolean courseEndAlert;

    @ColumnInfo(name = "courseEndAlertID")
    private final int courseEndAlertID;

    @ColumnInfo(name = "courseStatus")
    private final String courseStatus;

    @ColumnInfo(name = "courseMentorsName")
    private final String courseMentorsName;

    @ColumnInfo(name = "courseMentorsPhone")
    private final String courseMentorsPhone;

    @ColumnInfo(name = "courseMentorsEmail")
    private final String courseMentorsEmail;

    @ColumnInfo(name = "courseNotes")
    private final String courseNotes;

    @ColumnInfo(name = "courseLinkedTermID")
    private final int courseLinkedTermID;

    @ColumnInfo(name = "courseUserID")
    private final String courseUserID;

    public CourseEntity(String courseTitle, String courseStartDate, boolean courseStartAlert, int courseStartAlertID,
                        String courseEndDate, boolean courseEndAlert, int courseEndAlertID, String courseStatus,
                        String courseMentorsName, String courseMentorsPhone, String courseMentorsEmail,
                        String courseNotes, int courseLinkedTermID, String courseUserID) {
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
        this.courseUserID = courseUserID;
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

    public String getCourseUserID() {
        return courseUserID;
    }
}
