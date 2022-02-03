package com.sobesworld.wgucoursecommander.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<TermEntity>> mAllTerms;

    public TermViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllTerms = mRepository.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() { return mAllTerms; }

    public void insert(TermEntity termEntity) { mRepository.insert(termEntity); }

    public void update(TermEntity termEntity) { mRepository.update(termEntity); }

    public void delete(TermEntity termEntity) { mRepository.delete(termEntity); }
}
