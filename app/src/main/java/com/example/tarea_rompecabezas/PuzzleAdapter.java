package com.example.tarea_rompecabezas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.app.Activity; // Para usar Activity y obtener el ancho de la pantalla
import android.util.DisplayMetrics; // Para obtener el tamaño de la pantalla
import java.util.List;
import java.util.ArrayList;
public class PuzzleAdapter extends BaseAdapter {
    private Context context;
    private List<PuzzlePiece> puzzlePieces;
    private int cellSize;

    public PuzzleAdapter(Context context, List<PuzzlePiece> puzzlePieces) {
        this.context = context;
        this.puzzlePieces = (puzzlePieces != null) ? puzzlePieces : new ArrayList<>(); // Asegurar que nunca sea null

        // Obtener el tamaño de la pantalla para calcular el tamaño de las celdas
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;

        // Evitar una posible división por cero
        int puzzleSize = (int) Math.sqrt(this.puzzlePieces.size());
        cellSize = (puzzleSize > 0) ? screenWidth / puzzleSize : screenWidth / 4; // Por defecto, 4x4
    }


    @Override
    public int getCount() {
        return puzzlePieces.size();
    }

    @Override
    public Object getItem(int position) {
        return puzzlePieces.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PuzzlePiece piece = puzzlePieces.get(position);
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(cellSize-40, cellSize-40));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);

        } else {
            imageView = (ImageView) convertView;
        }

        if(piece != null) {
            imageView.setImageBitmap(piece.getImage());
        } else {
            imageView.setImageResource(android.R.color.white); // Espacio vacío
        }

        return imageView;
    }

    public void updatePieces(List<PuzzlePiece> newPieces) {
        this.puzzlePieces = newPieces;
        notifyDataSetChanged();
    }
}
