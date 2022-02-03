package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class Repository {

    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private final AssessmentDAO mAssessmentDAO;
    private final LiveData<List<TermEntity>> mAllTerms;
    private final LiveData<List<CourseEntity>> mAllCourses;
    private final LiveData<List<AssessmentEntity>> mAllAssessments;

    public Repository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
        mAllTerms = mTermDAO.getAllTerms();
        mAllCourses = mCourseDAO.getAllCourses();
        mAllAssessments = mAssessmentDAO.getAllAssessments();
    }

    public LiveData<List<TermEntity>> getAllTerms() { return mAllTerms; }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return mAllAssessments; }

    public void insert(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.insert(termEntity);
        });
    }

    public void insert(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.insert(courseEntity);
        });
    }

    public void insert(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.insert(assessmentEntity);
        });
    }

    public void update(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.update(termEntity);
        });
    }

    public void update(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.update(courseEntity);
        });
    }

    public void update(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.update(assessmentEntity);
        });
    }

    public void delete(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mTermDAO.delete(termEntity);
        });
    }

    public void delete(CourseEntity courseEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mCourseDAO.delete(courseEntity);
        });
    }

    public void delete(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> {
            mAssessmentDAO.delete(assessmentEntity);
        });
    }
}
