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

    public void deleteAllTerms() {
        CourseCommDatabase.databaseWriteExecutor.execute(termDAO::deleteAllTerms);
    }

    public LiveData<List<TermEntity>> getAllTerms() { return allTerms; }
}
