package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import com.sobesworld.wgucoursecommander.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.dao.TermDAO;
import com.sobesworld.wgucoursecommander.entities.Assessment;
import com.sobesworld.wgucoursecommander.entities.Course;
import com.sobesworld.wgucoursecommander.entities.Term;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final TermDAO mTermDao;
    private final CourseDAO mCourseDao;
    private final AssessmentDAO mAssessmentDao;
    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;

    private static final int NUMBER_OF_THREADS=4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        CourseCommDatabaseBuilder db = CourseCommDatabaseBuilder.getDatabase(application);
        mTermDao = db.termDAO();
        mCourseDao = db.courseDAO();
        mAssessmentDao = db.assessmentDAO();
    }

    public List<Term> getAllTerms() {
        databaseExecutor.execute(()-> mAllTerms = mTermDao.getAllTerms());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
    }

    public List<Course> getAllCourses() {
        databaseExecutor.execute(()-> mAllCourses = mCourseDao.getAllCourses());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
    }

    public List<Assessment> getAllAssessments() {
        databaseExecutor.execute(()-> mAllAssessments = mAssessmentDao.getAllAssessments());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
    }

    // Term Functions
    public void insert(Term term) {
        databaseExecutor.execute(()-> mTermDao.insert(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term) {
        databaseExecutor.execute(()-> mTermDao.update(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term) {
        databaseExecutor.execute(()-> mTermDao.delete(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Course Functions
    public void insert(Course course) {
        databaseExecutor.execute(()-> mCourseDao.insert(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        databaseExecutor.execute(()-> mCourseDao.update(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        databaseExecutor.execute(()-> mCourseDao.delete(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Assessment Functions
    public void insert(Assessment assessment) {
        databaseExecutor.execute(()-> mAssessmentDao.insert(assessment));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment) {
        databaseExecutor.execute(()-> mAssessmentDao.update(assessment));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment) {
        databaseExecutor.execute(()-> mAssessmentDao.delete(assessment));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
