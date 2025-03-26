package com.example.tarea_rompecabezas;

import android.graphics.Bitmap;

public class PuzzlePiece {
    private Bitmap image;
    private int originalRow, originalCol, actualRow, actualCol, number;

    public PuzzlePiece(Bitmap image, int originalRow, int originalCol, int number) {
        this.image = image;
        this.originalRow = originalRow;
        this.originalCol = originalCol;
        this.actualRow = originalRow;
        this.actualCol = originalCol;
        this.number = number;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setActualPosition(int row, int col) {
        this.actualRow = row;
        this.actualCol = col;
    }

    public int getActualRow(){
        return actualRow;
    }

    public int getActualCol(){
        return actualCol;
    }

    public int getOriginalCol(){
        return originalCol;
    }

    public int getOriginalRow(){
        return originalRow;
    }

    public int getNumber() { return number; }
}
