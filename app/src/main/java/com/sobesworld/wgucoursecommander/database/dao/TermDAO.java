package com.sobesworld.wgucoursecommander.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TermEntity termEntity);

    @Update
    void update(TermEntity termEntity);

    @Delete
    void delete(TermEntity termEntity);

    @Query("DELETE FROM term_table WHERE termID = :i")
    void deleteUsingTermID(int i);

    @Query("SELECT * FROM term_table ORDER BY termID ASC")
    LiveData<List<TermEntity>> getAllTerms();

    @Query("SELECT * FROM term_table WHERE termUserID = :s ORDER BY termID ASC")
    LiveData<List<TermEntity>> getAllTermsByUserID(String s);

    @Query("DELETE FROM term_table WHERE termUserID = :s")
    void deleteAllTermsByUserID(String s);

    @Query("SELECT * FROM term_table WHERE termTitle LIKE :s " +
            "OR termStartDate LIKE :s " +
            "OR termEndDate LIKE :s " +
            "AND termUserID = :id " +
            "ORDER BY termID ASC")
    LiveData<List<TermEntity>> termSearch(String s, String id);
}
