package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

public class AssessmentRepository {

    private final AssessmentDAO mAssessmentDAO;
    private final LiveData<List<AssessmentEntity>> mAllAssessments;

    AssessmentRepository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mAssessmentDAO = db.assessmentDAO();
        mAllAssessments = mAssessmentDAO.getAllAssessments();
    }

    LiveData<List<AssessmentEntity>> getAllAssessments() { return mAllAssessments; }

    LiveData<List<AssessmentEntity>> getLinkedAssessments(int i) { return mAssessmentDAO.getLinkedAssessments(i); }

    void insert(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mAssessmentDAO.insert(assessmentEntity));
    }

    void update(AssessmentEntity assessmentEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mAssessmentDAO.update(assessmentEntity));
    }

    void deleteTermByID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mAssessmentDAO.deleteAssessmentByID(i));
    }
}
