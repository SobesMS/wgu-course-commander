package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_table")
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    private int termID;
    private String termTitle;
    private String termStartDate;
    private String termEndDate;
    String termNotes;

    // this constructor is for new database entries; auto-generates primary key
    public TermEntity(@NonNull String termTitle, String termStartDate, String termEndDate, String termNotes) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termNotes = termNotes;
    }

    // this constructor is for updating existing database entries; carries over primary key
    @Ignore
    public TermEntity(int termID, @NonNull String termTitle, String termStartDate, String termEndDate, String termNotes) {
        this.termID = termID;
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termNotes = termNotes;
    }

    @NonNull
    @Override
    public String toString() {
        return termTitle;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public String getTermStartDate() {
        return termStartDate;
    }

    public void setTermStartDate(String termStartDate) {
        this.termStartDate = termStartDate;
    }

    public String getTermEndDate() {
        return termEndDate;
    }

    public void setTermEndDate(String termEndDate) {
        this.termEndDate = termEndDate;
    }

    public String getTermNotes() {
        return termNotes;
    }

    public void setTermNotes(String termNotes) {
        this.termNotes = termNotes;
    }
}
