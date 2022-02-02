package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.Term;

import java.util.List;

public class TermViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<Term>> mAllTerms;

    public TermViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllTerms = mRepository.getAllTerms();
    }

    LiveData<List<Term>> getAllTerms() { return mAllTerms; }

    public void insert(Term term) { mRepository.insert(term); }

    public void update(Term term) { mRepository.update(term); }

    public void delete(Term term) { mRepository.delete(term); }
}
