package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.Course;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<Course>> mAllCourses;

    public CourseViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllCourses = mRepository.getAllCourses();
    }

    LiveData<List<Course>> getAllCourses() { return mAllCourses; }

    public void insert(Course course) { mRepository.insert(course); }

    public void update(Course course) { mRepository.update(course); }

    public void delete(Course course) { mRepository.delete(course); }
}
