package com.sobesworld.wgucoursecommander.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sobesworld.wgucoursecommander.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.dao.TermDAO;
import com.sobesworld.wgucoursecommander.entities.Assessment;
import com.sobesworld.wgucoursecommander.entities.Course;
import com.sobesworld.wgucoursecommander.entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1, exportSchema = false)
public abstract class CourseCommDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile CourseCommDatabaseBuilder INSTANCE;

    static CourseCommDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE==null) {
            synchronized (CourseCommDatabaseBuilder.class) {
                if (INSTANCE==null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CourseCommDatabaseBuilder.class, "CourseCommDatabase.db")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
