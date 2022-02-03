package com.sobesworld.wgucoursecommander.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<AssessmentEntity>> mAllAssessments;

    public AssessmentViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllAssessments = mRepository.getAllAssessments();
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() { return mAllAssessments; }

    public void insert(AssessmentEntity assessmentEntity) { mRepository.insert(assessmentEntity); }

    public void update(AssessmentEntity assessmentEntity) { mRepository.update(assessmentEntity); }

    public void delete(AssessmentEntity assessmentEntity) { mRepository.delete(assessmentEntity); }
}
