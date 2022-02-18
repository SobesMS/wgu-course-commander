package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private final TermRepository mTermRepository;

    private final LiveData<List<TermEntity>> mAllTerms;

    public TermViewModel(Application application) {
        super(application);
        mTermRepository = new TermRepository(application);
        mAllTerms = mTermRepository.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() { return mAllTerms; }

    public void insert(TermEntity termEntity) { mTermRepository.insert(termEntity); }

    public void update(TermEntity termEntity) { mTermRepository.update(termEntity); }

    public void deleteTermByID(int i) { mTermRepository.deleteTermByID(i); }
}
