package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final CourseRepository mCourseRepository;
    private final LiveData<List<CourseEntity>> mAllCourses;
    private final LiveData<List<CourseEntity>> mLinkedCourses;

    public CourseViewModel(Application application) {
        super(application);
        mCourseRepository = new CourseRepository(application);
        mAllCourses = mCourseRepository.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

    public LiveData<List<CourseEntity>> getLinkedCourses(int i) {

        return mLinkedCourses;
    }

    public void insert(CourseEntity courseEntity) { mCourseRepository.insert(courseEntity); }

    public void update(CourseEntity courseEntity) { mCourseRepository.update(courseEntity); }

    public void deleteTermByID(int i) { mCourseRepository.deleteTermByID(i); }
}
