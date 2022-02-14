package com.sobesworld.wgucoursecommander.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TermEntity termEntity);

    @Update
    void update(TermEntity termEntity);

    @Delete
    void delete(TermEntity termEntity);

    @Query("SELECT * FROM term_table ORDER BY termID ASC")
    List<TermEntity> getAllTerms();

    @Query("UPDATE term_table SET termNotes = :n WHERE termID = :i")
    void updateTermNotes(String n, int i);
}
