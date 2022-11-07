package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermRepository {
    private final TermDAO termDAO;
    private final LiveData<List<TermEntity>> allTerms;

    public TermRepository(Application application) {
        CourseCommDatabase database = CourseCommDatabase.getInstance(application);
        termDAO = database.termDAO();
        allTerms = termDAO.getAllTerms();
    }

    public void insert(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> termDAO.insert(termEntity));
    }

    public void update(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> termDAO.update(termEntity));
    }

    public void delete(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> termDAO.delete(termEntity));
    }

    public void deleteUsingTermID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> termDAO.deleteUsingTermID(i));
    }

    public LiveData<List<TermEntity>> getAllTerms() { return allTerms; }

    public LiveData<List<TermEntity>> getAllTermsByUserID(String s) { return termDAO.getAllTermsByUserID(s); }

    public void deleteAllTermsByUserID(String s) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> termDAO.deleteAllTermsByUserID(s));
    }

    public LiveData<List<TermEntity>> termSearch(String s, String id) { return termDAO.termSearch(s, id); }
}
