package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_table")
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int assessmentID;
    private final String assessmentTitle;
    private final String assessmentType;
    private final String assessmentGoalDate;
    private final boolean assessmentGoalAlert;
    private final int assessmentAlertID;
    private final String assessmentNotes;
    private final int courseID;

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

    @NonNull
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

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public String getAssessmentGoalDate() {
        return assessmentGoalDate;
    }

    public boolean isAssessmentGoalAlert() {
        return assessmentGoalAlert;
    }

    public int getAssessmentAlertID() {
        return assessmentAlertID;
    }

    public String getAssessmentNotes() {
        return assessmentNotes;
    }

    public int getCourseID() {
        return courseID;
    }
}
