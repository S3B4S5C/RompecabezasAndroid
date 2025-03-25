package com.example.tarea_rompecabezas;

import android.graphics.Bitmap;

public class PuzzlePiece {
    private Bitmap image;
    private int originalRow, originalCol;

    public PuzzlePiece(Bitmap image, int originalRow, int originalCol) {
        this.image = image;
        this.originalRow = originalRow;
        this.originalCol = originalCol;
    }

    public Bitmap getImage() {
        return image;
    }
}
