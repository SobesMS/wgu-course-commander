package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_table")
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assessmentID")
    private int assessmentID;

    @ColumnInfo(name = "assessmentTitle")
    private final String assessmentTitle;

    @ColumnInfo(name = "assessmentType")
    private final String assessmentType;

    @ColumnInfo(name = "assessmentGoalDate")
    private final String assessmentGoalDate;

    @ColumnInfo(name = "assessmentGoalAlert")
    private final boolean assessmentGoalAlert;

    @ColumnInfo(name = "assessmentAlertID")
    private final int assessmentAlertID;

    @ColumnInfo(name = "assessmentNotes")
    private final String assessmentNotes;

    @ColumnInfo(name = "assessmentLinkedCourseID")
    private final int assessmentLinkedCourseID;

    @ColumnInfo(name = "assessmentUserID")
    private final String assessmentUserID;

    public AssessmentEntity(@NonNull String assessmentTitle, String assessmentType, String assessmentGoalDate,
                            boolean assessmentGoalAlert, int assessmentAlertID, String assessmentNotes,
                            int assessmentLinkedCourseID, String assessmentUserID) {
        this.assessmentTitle = assessmentTitle;
        this.assessmentType = assessmentType;
        this.assessmentGoalDate = assessmentGoalDate;
        this.assessmentGoalAlert = assessmentGoalAlert;
        this.assessmentAlertID = assessmentAlertID;
        this.assessmentNotes = assessmentNotes;
        this.assessmentLinkedCourseID = assessmentLinkedCourseID;
        this.assessmentUserID = assessmentUserID;
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

    public String getAssessmentUserID() {
        return assessmentUserID;
    }
}
