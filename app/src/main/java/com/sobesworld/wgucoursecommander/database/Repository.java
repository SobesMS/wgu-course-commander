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
    private List<TermEntity> mAllTerms;
    private List<CourseEntity> mAllCourses;
    private List<AssessmentEntity> mAllAssessments;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        CourseCommDatabase db = CourseCommDatabase.getDatabase(application);
        mTermDAO = db.termDAO();
        mCourseDAO = db.courseDAO();
        mAssessmentDAO = db.assessmentDAO();

        // uncomment method on next line to pre-populate an empty database
        //generateData();
    }

    // TermEntity query methods
    public List<TermEntity> getAllTerms() {
        databaseExecutor.execute(()-> mAllTerms = mTermDAO.getAllTerms());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllTerms;
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

    public void delete(TermEntity term) {
        databaseExecutor.execute(()-> mTermDAO.delete(term));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // CourseEntity query methods
    public List<CourseEntity> getAllCourses() {
        databaseExecutor.execute(()-> mAllCourses = mCourseDAO.getAllCourses());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllCourses;
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

    public void delete(CourseEntity course) {
        databaseExecutor.execute(()-> mCourseDAO.delete(course));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // AssessmentEntity query methods
    public List<AssessmentEntity> getAllAssessments() {
        databaseExecutor.execute(()-> mAllAssessments = mAssessmentDAO.getAllAssessments());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mAllAssessments;
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

    public void delete(AssessmentEntity assessment) {
        databaseExecutor.execute(()-> mAssessmentDAO.delete(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // generic data generator; use to pre-populate an empty database
    public void generateData() {
        TermEntity term1 = new TermEntity(1,"Term 1", "01/01/2022","06/30/2022");
        TermEntity term2 = new TermEntity(2,"Term 2", "01/01/2022","06/30/2022");
        TermEntity term3 = new TermEntity(3,"Term 3", "01/01/2022","06/30/2022");

        CourseEntity course1 = new CourseEntity(1,"C123", "01/01/2022",false,
                "06/30/2022", false,"in progress","John Doe",
                "216-555-5555", "john.doe@wgu.edu","These are course notes",1);
        CourseEntity course2 = new CourseEntity(2,"C456", "01/01/2022",false,
                "06/30/2022", false,"in progress","John Doe",
                "216-555-5555", "john.doe@wgu.edu","These are course notes",2);
        CourseEntity course3 = new CourseEntity(3,"C789", "01/01/2022",false,
                "06/30/2022", false,"in progress","John Doe",
                "216-555-5555", "john.doe@wgu.edu","These are course notes",3);

        AssessmentEntity assessment1 = new AssessmentEntity(1,"Bake a cake","Objective",
                "06/30/2022", true,1);
        AssessmentEntity assessment2 = new AssessmentEntity(2,"Build a model","Objective",
                "06/30/2022", true,2);
        AssessmentEntity assessment3 = new AssessmentEntity(3,"Braise short ribs","Objective",
                "06/30/2022", true,3);

        databaseExecutor.execute(()-> {
            mTermDAO.insert(term1);
            mTermDAO.insert(term2);
            mTermDAO.insert(term3);
            mCourseDAO.insert(course1);
            mCourseDAO.insert(course2);
            mCourseDAO.insert(course3);
            mAssessmentDAO.insert(assessment1);
            mAssessmentDAO.insert(assessment2);
            mAssessmentDAO.insert(assessment3);
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
