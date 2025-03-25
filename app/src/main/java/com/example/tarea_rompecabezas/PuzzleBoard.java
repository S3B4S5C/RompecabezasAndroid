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
        int emptyIndex = puzzlePieces.indexOf(null); // Encuentra el espacio vacío

        // Si la pieza seleccionada es adyacente al espacio vacío, intercambiamos
        if (isAdjacent(position, emptyIndex)) {
            Collections.swap(puzzlePieces, position, emptyIndex);

            PuzzlePiece movedPiece = puzzlePieces.get(emptyIndex);
            if (movedPiece != null) {
                int newRow = emptyIndex / cols;
                int newCol = emptyIndex % cols;
                movedPiece.setActualPosition(newRow, newCol);
            }

            return true; // Movimiento válido
        }
        return false; // No se puede mover
    }

    private boolean isAdjacent(int pos1, int pos2) {
        int row1 = pos1 / cols, col1 = pos1 % cols;
        int row2 = pos2 / cols, col2 = pos2 % cols;

        return (Math.abs(row1 - row2) == 1 && col1 == col2) ||  // Movimiento vertical
                (Math.abs(col1 - col2) == 1 && row1 == row2);   // Movimiento horizontal
    }

    public boolean isSolved() {
        for (int i = 0; i < puzzlePieces.size() - 1; i++) { // Ignorar el espacio vacío
            PuzzlePiece piece = puzzlePieces.get(i);
            if (piece == null || piece.getActualRow() != piece.getOriginalRow() || piece.getActualCol() != piece.getOriginalCol()) {
                return false;
            }
        }
        return true;
    }
}
