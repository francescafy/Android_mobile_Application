package com.example.dimgion.parkinson;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DemographicsDao {

    @Query("SELECT * FROM demographics")
    List<Demographics> getAll();

    @Insert
    void insertAll(Demographics... demographics);

    @Delete
    void delete(Demographics user);

    @Query("DELETE FROM demographics")
    public void nukeTable();

}