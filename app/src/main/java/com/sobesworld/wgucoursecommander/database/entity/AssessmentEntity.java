package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_table")
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private String assessmentTitle;
    private String assessmentType;
    private String assessmentGoalDate;
    private boolean assessmentGoalAlert;
    private int assessmentAlertID;
    String assessmentNotes;
    private int courseID;

    // this constructor is for new database entries; auto-generates primary key
    public AssessmentEntity(@NonNull String assessmentTitle, String assessmentType, String assessmentGoalDate,
                            boolean assessmentGoalAlert, int assessmentAlertID, String assessmentNotes, int courseID) {
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentGoalAlert = assessmentGoalAlert;
        this.assessmentAlertID = assessmentAlertID;
        this.assessmentNotes = assessmentNotes;
        this.courseID = courseID;
    }

    // this constructor is for updating existing database entries; carries over primary key
    @Ignore
    public AssessmentEntity(int assessmentID, @NonNull String assessmentTitle, String assessmentType, String assessmentGoalDate,
                            boolean assessmentGoalAlert, int assessmentAlertID, String assessmentNotes, int courseID) {
        this.assessmentID = assessmentID;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentGoalAlert = assessmentGoalAlert;
        this.assessmentAlertID = assessmentAlertID;
        this.assessmentNotes = assessmentNotes;
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "AssessmentEntity{" +
                "assessmentID=" + assessmentID +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                ", assessmentGoalDate='" + assessmentGoalDate + '\'' +
                ", assessmentGoalAlert=" + assessmentGoalAlert +
                ", assessmentAlertID=" + assessmentAlertID +
                ", assessmentNotes='" + assessmentNotes + '\'' +
                ", courseID=" + courseID +
                '}';
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentGoalDate() {
        return assessmentGoalDate;
    }

    public void setAssessmentGoalDate(String assessmentGoalDate) {
        this.assessmentGoalDate = assessmentGoalDate;
    }

    public boolean isAssessmentGoalAlert() {
        return assessmentGoalAlert;
    }

    public void setAssessmentGoalAlert(boolean assessmentGoalAlert) {
        this.assessmentGoalAlert = assessmentGoalAlert;
    }

    public int getAssessmentAlertID() {
        return assessmentAlertID;
    }

    public void setAssessmentAlertID(int assessmentAlertID) {
        this.assessmentAlertID = assessmentAlertID;
    }

    public String getAssessmentNotes() {
        return assessmentNotes;
    }

    public void setAssessmentNotes(String assessmentNotes) {
        this.assessmentNotes = assessmentNotes;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
