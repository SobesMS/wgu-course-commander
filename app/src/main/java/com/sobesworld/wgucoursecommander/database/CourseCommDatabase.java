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
                termDAO.insert(new TermEntity("Term 1", "01/01/22","06/30/22"));
                termDAO.insert(new TermEntity("Term 2", "01/01/22","06/30/22"));
                termDAO.insert(new TermEntity("Term 3", "01/01/22","06/30/22"));
                courseDAO.insert(new CourseEntity("C123", "01/01/22", "06/30/22", false,
                        -1, "in progress","John Doe", "216-555-5555",
                        "john.doe@wgu.edu","These are course notes",1));
                courseDAO.insert(new CourseEntity("C456", "01/01/22", "06/30/22", false,
                        -1, "completed","John Doe", "216-555-5555",
                        "john.doe@wgu.edu","These are course notes",2));
                courseDAO.insert(new CourseEntity("C789", "01/01/22", "06/30/22", false,
                        -1, "plan to take","John Doe", "216-555-5555",
                        "john.doe@wgu.edu","These are course notes",3));
                assessmentDAO.insert(new AssessmentEntity("Bake a cake","objective", "06/30/22",
                        false, -1, "These are assessment notes.",1));
                assessmentDAO.insert(new AssessmentEntity("Build a model","objective", "06/30/22",
                        false, -1, "These are assessment notes.",2));
                assessmentDAO.insert(new AssessmentEntity("Braise short ribs","objective", "06/30/22",
                        false, -1, "These are assessment notes.",3));
            });
        }
    };
}
