package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private final TermRepository termRepository;
    private final LiveData<List<TermEntity>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        termRepository = new TermRepository(application);
        allTerms = termRepository.getAllTerms();
    }


    public void insert(TermEntity termEntity) { termRepository.insert(termEntity); }

    public void update(TermEntity termEntity) { termRepository.update(termEntity); }

    public void delete(TermEntity termEntity) { termRepository.delete(termEntity); }

    public void deleteUsingTermID(int i) { termRepository.deleteUsingTermID(i); }

    public LiveData<List<TermEntity>> getAllTerms() { return allTerms; }

    public LiveData<List<TermEntity>> getAllTermsByUserID(String s) { return termRepository.getAllTermsByUserID(s); }

    public void deleteAllTermsByUserID(String s) { termRepository.deleteAllTermsByUserID(s); }

    public LiveData<List<TermEntity>> termSearch(String s, String id) { return termRepository.termSearch(s, id); }
}
