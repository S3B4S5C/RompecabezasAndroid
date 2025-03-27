package com.example.tarea_rompecabezas;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PuzzleEntity.class}, version = 1)
public abstract class PuzzleDatabase extends RoomDatabase {
    public abstract PuzzleDao puzzleDao();
}

