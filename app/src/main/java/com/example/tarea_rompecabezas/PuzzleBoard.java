package com.example.tarea_rompecabezas;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PuzzleBoard {
    private Bitmap originalImage;
    private int rows, cols;
    private List<PuzzlePiece> puzzlePieces;

    public PuzzleBoard(Bitmap image, int rows, int cols) {
        this.originalImage = image;
        this.rows = rows;
        this.cols = cols;
        createPuzzlePieces();
    }

    private void createPuzzlePieces() {
        puzzlePieces = new ArrayList<>();
        int pieceWidth = originalImage.getWidth() / cols;
        int pieceHeight = originalImage.getHeight() / rows;

        // Se crean las piezas a partir de la imagen
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Bitmap pieceImage = Bitmap.createBitmap(originalImage, col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(pieceImage, row, col);
                puzzlePieces.add(piece);
            }
        }
        // Se elimina una pieza para crear el espacio vacío
        puzzlePieces.remove(puzzlePieces.size() - 1);
        Collections.shuffle(puzzlePieces); // Opcional: mezcla para iniciar el juego
        puzzlePieces.add(null); // Representa el espacio en blanco
    }

    public List<PuzzlePiece> getPuzzlePieces() {
        return puzzlePieces;
    }

    public boolean movePiece(int position) {
        // Lógica para determinar si la pieza en "position" es movible (adyacente al espacio vacío).
        // Si es válida, se realiza el intercambio y se retorna true.
        // (La implementación detallada dependerá de cómo se gestione la posición y se almacene el estado)
        return false; // A implementar
    }
}
