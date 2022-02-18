package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseRepository {

    private final CourseDAO mCourseDAO;
    private final LiveData<List<CourseEntity>> mAllCourses;

    CourseRepository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mCourseDAO = db.courseDAO();
        mAllCourses = mCourseDAO.getAllCourses();
    }

    LiveData<List<CourseEntity>> getAllCourses() {
        return mAllCourses;
    }

    LiveData<List<CourseEntity>> getLinkedCourses(int i) {
        return mCourseDAO.getLinkedCourses(i);
    }

    void insert(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.insert(courseEntity);
        });
    }

    void update(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.update(courseEntity);
        });
    }

    void deleteTermByID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.deleteCourseByID(i);
        });
    }
}
