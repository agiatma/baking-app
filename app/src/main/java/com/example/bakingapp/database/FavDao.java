package com.example.bakingapp.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavDao {
    @Query("SELECT * FROM recipe")
    List<FavEntry> loadAllTask();

    @Query("SELECT * FROM recipe")
    LiveData<List<FavEntry>> loadAllTaskLive();

    @Query("SELECT * FROM recipe WHERE id = :id")
    FavEntry loadFavById(Integer id);

    @Insert
    void insertFav(FavEntry favEntry);

    @Delete
    void deleteFav(FavEntry favEntry);



}
