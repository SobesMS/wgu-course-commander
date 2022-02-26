package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class AssessmentRepository {
    private final AssessmentDAO assessmentDAO;
    private final LiveData<List<AssessmentEntity>> allAssessments;

    public AssessmentRepository(Application application) {
        CourseCommDatabase database = CourseCommDatabase.getInstance(application);
        assessmentDAO = database.assessmentDAO();
        allAssessments = assessmentDAO.getAllAssessments();
    }

    public void insert(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.insert(assessmentEntity));
    }

    public void update(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.update(assessmentEntity));
    }

    public void delete(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.delete(assessmentEntity));
    }

    public void deleteUsingAssessmentID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.deleteUsingAssessmentID(i));
    }

    public void deleteLinkedAssessments(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> assessmentDAO.deleteLinkedAssessments(i));
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return allAssessments; }

    public LiveData<List<AssessmentEntity>> getLinkedAssessments(int i) { return assessmentDAO.getLinkedAssessments(i); }
}
