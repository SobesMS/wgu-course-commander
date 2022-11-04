package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseRepository {
    private final CourseDAO courseDAO;
    private final AssessmentDAO assessmentDAO;
    private final LiveData<List<CourseEntity>> allCourses;

    public CourseRepository(Application application) {
        CourseCommDatabase database = CourseCommDatabase.getInstance(application);
        courseDAO = database.courseDAO();
        assessmentDAO = database.assessmentDAO();
        allCourses = courseDAO.getAllCourses();
    }

    public void insert(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> courseDAO.insert(courseEntity));
    }

    public void update(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> courseDAO.update(courseEntity));
    }

    public void delete(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> courseDAO.delete(courseEntity));
    }

    public void deleteUsingCourseID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> courseDAO.deleteUsingCourseID(i));
    }

    public void deleteLinkedCourses(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> courseDAO.deleteLinkedCourses(i));
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return allCourses; }

    public LiveData<List<CourseEntity>> getAllCoursesByUserID(String s) { return courseDAO.getAllCoursesByUserID(s); }

    public LiveData<List<CourseEntity>> getLinkedCourses(int i) { return courseDAO.getLinkedCourses(i); }

    public void deleteLinkedCoursesAndAssessments(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<CourseEntity> courseList = courseDAO.deleteLinkedAssessments(i);
                for (int id = 0; id < courseList.size(); id++) {
                    CourseEntity course = courseList.get(id);
                    assessmentDAO.deleteLinkedAssessments(course.getCourseID());
                }
                courseDAO.deleteLinkedCourses(i);
            }
        });
    }
}
