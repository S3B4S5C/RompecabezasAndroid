package com.example.tarea_rompecabezas;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.lifecycle.LiveData; // Necesario para LiveData
import java.util.List;
@Dao
public interface PuzzleDao {
    @Insert
    void insert(PuzzleEntity puzzle);

    @Update
    void update(PuzzleEntity puzzle);

    @Delete
    void delete(PuzzleEntity puzzle);

    @Query("SELECT * FROM puzzles ORDER BY id DESC")
    LiveData<List<PuzzleEntity>> getAllPuzzles();
}