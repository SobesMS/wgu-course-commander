package com.sobesworld.wgucoursecommander.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {

    private final Repository mRepository;

    private final LiveData<List<CourseEntity>> mAllCourses;

    public CourseViewModel (Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllCourses = mRepository.getAllCourses();
    }

    public LiveData<List<CourseEntity>> getAllCourses() { return mAllCourses; }

    public void insert(CourseEntity courseEntity) { mRepository.insert(courseEntity); }

    public void update(CourseEntity courseEntity) { mRepository.update(courseEntity); }

    public void delete(CourseEntity courseEntity) { mRepository.delete(courseEntity); }
}
