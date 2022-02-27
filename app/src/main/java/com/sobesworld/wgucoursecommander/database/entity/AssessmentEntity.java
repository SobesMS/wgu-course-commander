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
    private final int assessmentLinkedCourseID;

    public AssessmentEntity(@NonNull String assessmentTitle, String assessmentType, String assessmentGoalDate,
                            boolean assessmentGoalAlert, int assessmentAlertID, String assessmentNotes,
                            int assessmentLinkedCourseID) {
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentGoalAlert = assessmentGoalAlert;
        this.assessmentAlertID = assessmentAlertID;
        this.assessmentNotes = assessmentNotes;
        this.assessmentLinkedCourseID = assessmentLinkedCourseID;
    }

    @NonNull
    @Override
    public String toString() {
        return assessmentTitle;
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

    public int getAssessmentLinkedCourseID() {
        return assessmentLinkedCourseID;
    }
}
