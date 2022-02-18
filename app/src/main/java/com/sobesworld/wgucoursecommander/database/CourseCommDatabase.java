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

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile CourseCommDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CourseCommDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CourseCommDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CourseCommDatabase.class,
                            "course_comm_database").addCallback(sRoomDatabaseCallback).build();

                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // comment out following block to keep data through app restarts
            databaseWriteExecutor.execute(() -> {
                TermDAO termDAO = INSTANCE.termDAO();
                TermEntity term = new TermEntity(1,"Term 1", "01/01/22","06/30/22",
                        "These are term notes.");
                termDAO.insert(term);
                term = new TermEntity(2,"Term 2", "01/01/22","06/30/22",
                        "These are term notes.");
                termDAO.insert(term);
                term = new TermEntity(3,"Term 3", "01/01/22","06/30/22",
                        "These are term notes.");
                termDAO.insert(term);

                CourseDAO courseDAO = INSTANCE.courseDAO();
                CourseEntity course = new CourseEntity(1,"C123", "01/01/22",
                        "06/30/22", false,-1, "in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",1);
                courseDAO.insert(course);
                course = new CourseEntity(2,"C456", "01/01/22",
                        "06/30/22", false,-1, "in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",2);
                courseDAO.insert(course);
                course = new CourseEntity(3,"C789", "01/01/22",
                        "06/30/22", false,-1, "in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",3);
                courseDAO.insert(course);

                AssessmentDAO assessmentDAO = INSTANCE.assessmentDAO();
                AssessmentEntity assessment = new AssessmentEntity(1,"Bake a cake","objective",
                        "06/30/22", false, -1, "These are assessment notes.",
                        1);
                assessmentDAO.insert(assessment);
                assessment = new AssessmentEntity(2,"Build a model","objective",
                        "06/30/22", false, -1, "These are assessment notes.",
                        2);
                assessmentDAO.insert(assessment);
                assessment = new AssessmentEntity(3,"Braise short ribs","objective",
                        "06/30/22", false, -1, "These are assessment notes.",
                        3);
                assessmentDAO.insert(assessment);
            });
        }
    };
}
