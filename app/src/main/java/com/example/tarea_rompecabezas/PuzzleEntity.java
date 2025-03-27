package com.example.tarea_rompecabezas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "puzzles")
public class PuzzleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String imagePath; // Ruta de la imagen
    public byte[] imageBytes; // Imagen en bytes
    public int size; // Tamaño del puzzle (ej. 3x3, 4x4)
    public boolean isCompleted; // Indica si se terminó
    public long solveTime; // Tiempo de resolución en ms
}