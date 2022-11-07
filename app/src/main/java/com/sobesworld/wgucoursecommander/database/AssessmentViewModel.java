package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private final AssessmentRepository assessmentRepository;
    private final LiveData<List<AssessmentEntity>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = new AssessmentRepository(application);
        allAssessments = assessmentRepository.getAllAssessments();
    }

    public void insert(AssessmentEntity assessmentEntity) { assessmentRepository.insert(assessmentEntity); }

    public void update(AssessmentEntity assessmentEntity) { assessmentRepository.update(assessmentEntity); }

    public void delete(AssessmentEntity assessmentEntity) { assessmentRepository.delete(assessmentEntity); }

    public void deleteUsingAssessmentID(int i) { assessmentRepository.deleteUsingAssessmentID(i); }

    public void deleteLinkedAssessments(int i) { assessmentRepository.deleteLinkedAssessments(i); }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return allAssessments; }

    public LiveData<List<AssessmentEntity>> getAllAssessmentsByUserID(String s) { return assessmentRepository.getAllAssessmentsByUserID(s); }

    public void deleteAllAssessmentsByUserID(String s) { assessmentRepository.deleteAllAssessmentsByUserID(s); }

    public LiveData<List<AssessmentEntity>> getLinkedAssessments(int i) { return assessmentRepository.getLinkedAssessments(i); }

    public LiveData<List<AssessmentEntity>> assessmentSearch(String s, String id) { return assessmentRepository.assessmentSearch(s, id); }
}
