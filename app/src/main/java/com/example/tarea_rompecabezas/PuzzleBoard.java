package com.example.tarea_rompecabezas;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.content.Context;

public class PuzzleBoard {
    private Bitmap originalImage;
    private int rows, cols;
    private List<PuzzlePiece> puzzlePieces;
    private Context context;
    public PuzzleBoard(Bitmap image, int rows, int cols, Context context) {
        this.originalImage = image;
        this.rows = rows;
        this.cols = cols;
        this.context = context;
        createPuzzlePieces();
    }
    public int getRows() {
        return this.rows;
    }
    public int getCols() {
        return this.cols;
    }
    private void createPuzzlePieces() {
        puzzlePieces = new ArrayList<>();

        // Obtener el ancho de la pantalla
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Escalar la imagen para que ocupe todo el ancho de la pantalla
        int targetHeight = (int) ((double) screenWidth / originalImage.getWidth() * originalImage.getHeight());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalImage, screenWidth, targetHeight, true);

        int pieceWidth = screenWidth / cols;
        int pieceHeight = targetHeight / rows;
        int count = 1;

        // Se crean las piezas a partir de la imagen escalada
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Bitmap pieceImage = Bitmap.createBitmap(scaledBitmap, col * pieceWidth, row * pieceHeight, pieceWidth, pieceHeight);
                pieceImage = addNumberToBitmap(pieceImage, count);
                PuzzlePiece piece = new PuzzlePiece(pieceImage, row, col, count++);
                puzzlePieces.add(piece);
            }
        }

        // Se elimina una pieza para crear el espacio vacío
        puzzlePieces.remove(puzzlePieces.size() - 1);
        Collections.shuffle(puzzlePieces);
        while (!isSolvable()) {
            Collections.shuffle(puzzlePieces);
        }
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
    private Bitmap addNumberToBitmap(Bitmap bitmap, int number) {
        int pieceSize = bitmap.getWidth();
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);

        Paint paintBorder = new Paint();
        paintBorder.setTextSize(pieceSize / 6);
        paintBorder.setColor(Color.BLACK);  // Borde negro
        paintBorder.setTypeface(Typeface.DEFAULT_BOLD);
        paintBorder.setAntiAlias(true);
        paintBorder.setTextAlign(Paint.Align.RIGHT);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(pieceSize / 30); // Grosor del borde

        Paint paintText = new Paint(paintBorder);
        paintText.setColor(Color.WHITE);  // Texto blanco
        paintText.setStyle(Paint.Style.FILL);

        int margin = pieceSize / 15;
        int x = bitmap.getWidth() - margin;
        int y = margin + (int) (-paintBorder.ascent());

        // Dibujar borde negro
        canvas.drawText(String.valueOf(number), x, y, paintBorder);
        // Dibujar texto blanco encima
        canvas.drawText(String.valueOf(number), x, y, paintText);

        return mutableBitmap;
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

    public boolean isSolvable() {
        List<Integer> flattened = new ArrayList<>();

        // Aplanar el puzzle en una lista sin incluir el espacio vacío
        for (PuzzlePiece piece : puzzlePieces) {
            if (piece != null) {
                flattened.add(piece.getOriginalRow() * cols + piece.getOriginalCol() + 1);
            }
        }

        int inversions = 0;
        for (int i = 0; i < flattened.size(); i++) {
            for (int j = i + 1; j < flattened.size(); j++) {
                if (flattened.get(i) > flattened.get(j)) {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
    }
}
