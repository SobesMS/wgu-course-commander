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

    public TermEntity(@NonNull String termTitle, String termStartDate, String termEndDate) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
    }

    @NonNull
    @Override
    public String toString() {
        return termTitle;
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
}
