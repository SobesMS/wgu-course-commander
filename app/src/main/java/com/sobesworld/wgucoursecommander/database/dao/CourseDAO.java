package com.sobesworld.wgucoursecommander.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CourseEntity courseEntity);

    @Update
    void update(CourseEntity courseEntity);

    @Delete
    void delete(CourseEntity courseEntity);

    @Query("SELECT * FROM course_table ORDER BY courseID ASC")
    List<CourseEntity> getAllCourses();

    @Query("SELECT * FROM course_table WHERE termID = :i ORDER BY courseID ASC")
    List<CourseEntity> getLinkedCourses(int i);

    @Query("DELETE FROM course_table WHERE termID = :i")
    void deleteLinkedCourses(int i);
}
