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

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            /*databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                TermDAO termDAO = INSTANCE.termDAO();
                termDAO.deleteAll();
                CourseDAO courseDAO = INSTANCE.courseDAO();
                courseDAO.deleteAll();
                AssessmentDAO assessmentDAO = INSTANCE.assessmentDAO();
                assessmentDAO.deleteAll();

                TermEntity term1 = new TermEntity(1,"Term 1", "01/01/2022","06/30/2022");
                termDAO.insert(term1);
                TermEntity term2 = new TermEntity(2,"Term 2", "01/01/2022","06/30/2022");
                termDAO.insert(term2);
                TermEntity term3 = new TermEntity(3,"Term 3", "01/01/2022","06/30/2022");
                termDAO.insert(term3);

                CourseEntity course1 = new CourseEntity(1,"C123", "01/01/2022",false,
                        "06/30/2022", false,"in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",1);
                courseDAO.insert(course1);
                CourseEntity course2 = new CourseEntity(2,"C456", "01/01/2022",false,
                        "06/30/2022", false,"in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",2);
                courseDAO.insert(course2);
                CourseEntity course3 = new CourseEntity(3,"C789", "01/01/2022",false,
                        "06/30/2022", false,"in progress","John Doe",
                        "216-555-5555", "john.doe@wgu.edu","These are course notes",3);
                courseDAO.insert(course3);

                AssessmentEntity assessment1 = new AssessmentEntity(1,"Bake a cake","Objective",
                        "06/30/2022", true,1);
                assessmentDAO.insert(assessment1);
                AssessmentEntity assessment2 = new AssessmentEntity(2,"Build a model","Objective",
                        "06/30/2022", true,2);
                assessmentDAO.insert(assessment2);
                AssessmentEntity assessment3 = new AssessmentEntity(3,"Braise short ribs","Objective",
                        "06/30/2022", true,3);
                assessmentDAO.insert(assessment3);
            });*/
        }
    };
}
