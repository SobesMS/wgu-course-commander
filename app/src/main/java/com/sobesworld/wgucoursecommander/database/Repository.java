package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import com.sobesworld.wgucoursecommander.database.dao.AssessmentDAO;
import com.sobesworld.wgucoursecommander.database.dao.CourseDAO;
import com.sobesworld.wgucoursecommander.database.dao.TermDAO;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private final AssessmentDAO mAssessmentDAO;
    private List<TermEntity> mTermList;
    private List<CourseEntity> mCourseList;
    private List<AssessmentEntity> mAssessmentList;
    private int courseMaxAlertID;
    private int assessmentMaxAlertID;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getInstance(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();
    }

    // TermEntity query methods
    public List<TermEntity> getAllTerms() {
        databaseExecutor.execute(()-> mTermList = mTermDAO.getAllTerms());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mTermList;
    }

    public void insert(TermEntity term) {
        databaseExecutor.execute(()-> mTermDAO.insert(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(TermEntity term) {
        databaseExecutor.execute(()-> mTermDAO.update(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteTermByID(int i) {
        databaseExecutor.execute(()-> mTermDAO.deleteTermByID(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // CourseEntity query methods
    public List<CourseEntity> getAllCourses() {
        databaseExecutor.execute(()-> mCourseList = mCourseDAO.getAllCourses());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mCourseList;
    }

    public List<CourseEntity> getLinkedCourses(int i) {
        databaseExecutor.execute(()-> mCourseList = mCourseDAO.getLinkedCourses(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mCourseList;
    }

    public void insert(CourseEntity course) {
        databaseExecutor.execute(()-> mCourseDAO.insert(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(CourseEntity course) {
        databaseExecutor.execute(()-> mCourseDAO.update(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteCourseByID(int i) {
        databaseExecutor.execute(()-> mCourseDAO.deleteCourseByID(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteLinkedCourses(int i) {
        databaseExecutor.execute(()-> mCourseDAO.deleteLinkedCourses(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // AssessmentEntity query methods
    public List<AssessmentEntity> getAllAssessments() {
        databaseExecutor.execute(()-> mAssessmentList = mAssessmentDAO.getAllAssessments());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAssessmentList;
    }

    public List<AssessmentEntity> getLinkedAssessments(int i) {
        databaseExecutor.execute(()-> mAssessmentList = mAssessmentDAO.getLinkedAssessments(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAssessmentList;
    }

    public void insert(AssessmentEntity assessment) {
        databaseExecutor.execute(()-> mAssessmentDAO.insert(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(AssessmentEntity assessment) {
        databaseExecutor.execute(()-> mAssessmentDAO.update(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteAssessmentByID(int i) {
        databaseExecutor.execute(()-> mAssessmentDAO.deleteAssessmentByID(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteLinkedAssessments(int i) {
        databaseExecutor.execute(()-> mAssessmentDAO.deleteLinkedAssessments(i));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
