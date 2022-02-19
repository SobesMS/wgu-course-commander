package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermRepository{

    private final TermDAO mTermDAO;
    private final LiveData<List<TermEntity>> mAllTerms;

    TermRepository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mTermDAO = db.termDAO();
        mAllTerms = mTermDAO.getAllTerms();
    }

    LiveData<List<TermEntity>> getAllTerms() { return mAllTerms; }

    void insert(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mTermDAO.insert(termEntity));
    }

    void update(TermEntity termEntity) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mTermDAO.update(termEntity));
    }

    void deleteTermByID(int i) {
        CourseCommDatabase.databaseWriteExecutor.execute(() -> mTermDAO.deleteTermByID(i));
    }
}
