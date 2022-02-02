package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.Assessment;
import com.sobesworld.wgucoursecommander.database.entity.Course;
import com.sobesworld.wgucoursecommander.database.entity.Term;

import java.util.List;

class Repository {

    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private final AssessmentDAO mAssessmentDAO;
    private final LiveData<List<Term>> mAllTerms;
    private final LiveData<List<Course>> mAllCourses;
    private final LiveData<List<Assessment>> mAllAssessments;

    Repository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
        mAllTerms = mTermDAO.getAllTerms();
        mAllCourses = mCourseDAO.getAllCourses();
        mAllAssessments = mAssessmentDAO.getAllAssessments();
    }

    LiveData<List<Term>> getAllTerms() { return mAllTerms; }

    LiveData<List<Course>> getAllCourses() { return mAllCourses; }

    LiveData<List<Assessment>> getAllAssessments() { return mAllAssessments; }

    void insert(Term term) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.insert(term);
        });
    }

    void insert(Course course) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.insert(course);
        });
    }

    void insert(Assessment assessment) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.insert(assessment);
        });
    }

    void update(Term term) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.update(term);
        });
    }

    void update(Course course) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.update(course);
        });
    }

    void update(Assessment assessment) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.update(assessment);
        });
    }

    void delete(Term term) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.delete(term);
        });
    }

    void delete(Course course) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.delete(course);
        });
    }

    void delete(Assessment assessment) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.delete(assessment);
        });
    }
}
