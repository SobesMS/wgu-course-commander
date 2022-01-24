package com.sobesworld.wgucoursecommander.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessmentID;

    private String assessmentTitle;
    private String assessmentType;
    private String assessmentGoalDate;
    private boolean assessmentGoalAlert;
    private int courseID;

    public Assessment(int assessmentID, String assessmentTitle, String assessmentType, String assessmentGoalDate, boolean assessmentGoalAlert, int courseID) {
        this.assessmentID = assessmentID;
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentGoalAlert = assessmentGoalAlert;
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID=" + assessmentID +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", assessmentType='" + assessmentType + '\'' +
                ", assessmentGoalDate='" + assessmentGoalDate + '\'' +
                ", assessmentGoalAlert=" + assessmentGoalAlert +
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

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }
}
