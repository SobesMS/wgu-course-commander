package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private final AssessmentRepository mAssessmentRepository;

    private final LiveData<List<AssessmentEntity>> mAllAssessments;

    public AssessmentViewModel(Application application) {
        super(application);
        mAssessmentRepository = new AssessmentRepository(application);
        mAllAssessments = mAssessmentRepository.getAllAssessments();
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return mAllAssessments; }

    public void insert(AssessmentEntity assessmentEntity) { mAssessmentRepository.insert(assessmentEntity); }

    public void update(AssessmentEntity assessmentEntity) { mAssessmentRepository.update(assessmentEntity); }

    public void deleteTermByID(int i) { mAssessmentRepository.deleteTermByID(i); }
}
