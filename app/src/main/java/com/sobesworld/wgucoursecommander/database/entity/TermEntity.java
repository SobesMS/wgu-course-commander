package com.sobesworld.wgucoursecommander.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "term_table")
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "termID")
    private int termID;

    @ColumnInfo(name = "termTitle")
    private final String termTitle;

    @ColumnInfo(name = "termStartDate")
    private final String termStartDate;

    @ColumnInfo(name = "termEndDate")
    private final String termEndDate;

    @ColumnInfo(name = "termUserID")
    private final String termUserID;

    public TermEntity(@NonNull String termTitle, String termStartDate, String termEndDate, String termUserID) {
        this.termTitle = termTitle;
        this.termStartDate = termStartDate;
        this.termEndDate = termEndDate;
        this.termUserID = termUserID;
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

    public String getTermUserID() {
        return termUserID;
    }
}
