package com.sobesworld.wgucoursecommander.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class}, version = 1, exportSchema = false)
public abstract class CourseCommDatabase extends RoomDatabase {
    private static volatile CourseCommDatabase instance;
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized CourseCommDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), CourseCommDatabase.class, "course_comm_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            TermDAO termDAO = instance.termDAO();
            CourseDAO courseDAO = instance.courseDAO();
            AssessmentDAO assessmentDAO = instance.assessmentDAO();
            databaseWriteExecutor.execute(() -> {
                termDAO.insert(new TermEntity("Term Tatooine", "01/01/22","04/30/22"));
                termDAO.insert(new TermEntity("Term Coruscant", "05/01/22","08/31/22"));
                termDAO.insert(new TermEntity("Term Mustafar", "09/01/22","12/31/22"));
                courseDAO.insert(new CourseEntity("Jedi 101", "01/01/22", false, -1,
                        "04/30/22", false, -1, "plan to take","Luke Skywalker",
                        "555-555-5555", "lskywalker@starwars.com","These are course notes.",1));
                courseDAO.insert(new CourseEntity("Becoming a Jedi Master", "05/01/22", false, -1,
                        "08/31/22", false, -1, "in progress","Obi Wan Kenobi",
                        "555-555-6666", "owkenobi@starwars.com","These are course notes.",2));
                courseDAO.insert(new CourseEntity("History of the Empire", "09/01/22", false, -1,
                        "12/31/22", false, -1, "completed","Darth Vader",
                        "555-555-7777", "dvader@starwars.com","These are course notes.",3));
                assessmentDAO.insert(new AssessmentEntity("Path of the Jedi quiz","Objective", "04/15/22",
                        false, -1, "These are assessment notes.",1));
                assessmentDAO.insert(new AssessmentEntity("Train a Padawan","Performance", "08/15/22",
                        false, -1, "These are assessment notes.",2));
                assessmentDAO.insert(new AssessmentEntity("Famous Sith exam","Objective", "12/15/22",
                        false, -1, "These are assessment notes.",3));
            });
        }
    };
}
