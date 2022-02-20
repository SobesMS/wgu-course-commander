package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_table")
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    private int termID;
    private final String termTitle;
    private final String termStartDate;
    private final String termEndDate;
    private final String termNotes;

    public TermEntity(@NonNull String termTitle, String termStartDate, String termEndDate, String termNotes) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termNotes = termNotes;
    }

    @NonNull
    @Override
    public String toString() {
        return "TermEntity{" +
                "termID=" + termID +
                ", termTitle='" + termTitle + '\'' +
                ", termStartDate='" + termStartDate + '\'' +
                ", termEndDate='" + termEndDate + '\'' +
                ", termNotes='" + termNotes + '\'' +
                '}';
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public int getTermID() {
        return termID;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public String getTermStartDate() {
        return termStartDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public String getTermNotes() {
        return termNotes;
    }
}
