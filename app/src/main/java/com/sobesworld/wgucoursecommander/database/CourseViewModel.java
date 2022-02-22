package com.sobesworld.wgucoursecommander.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseViewModel extends AndroidViewModel {
    private final CourseRepository courseRepository;
    private final LiveData<List<CourseEntity>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCourses();
    }

    public void insert(CourseEntity courseEntity) { courseRepository.insert(courseEntity); }

    public void update(CourseEntity courseEntity) { courseRepository.update(courseEntity); }

    public void delete(CourseEntity courseEntity) { courseRepository.delete(courseEntity); }

    public void deleteUsingCourseID(int i) { courseRepository.deleteUsingCourseID(i); }

    public LiveData<List<CourseEntity>> getAllCourses() { return allCourses; }

    public LiveData<List<CourseEntity>> getLinkedCourses(int i) { return courseRepository.getLinkedCourses(i); }
}
