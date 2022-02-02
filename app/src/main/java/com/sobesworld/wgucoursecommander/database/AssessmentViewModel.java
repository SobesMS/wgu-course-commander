package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.Assessment;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<Assessment>> mAllAssessments;

    public AssessmentViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllAssessments = mRepository.getAllAssessments();
    }

    LiveData<List<Assessment>> getAllAssessments() { return mAllAssessments; }

    public void insert(Assessment assessment) { mRepository.insert(assessment); }

    public void update(Assessment assessment) { mRepository.update(assessment); }

    public void delete(Assessment assessment) { mRepository.delete(assessment); }
}
