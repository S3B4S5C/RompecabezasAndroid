package com.example.tarea_rompecabezas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class PuzzleHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<PuzzleEntity> puzzles;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(PuzzleEntity puzzle);
    }

    public PuzzleHistoryAdapter(Context context, List<PuzzleEntity> puzzles, OnDeleteClickListener listener) {
        this.context = context;
        this.puzzles = puzzles;
        this.deleteClickListener = listener;
    }

    @Override
    public int getCount() {
        return puzzles.size();
    }

    @Override
    public Object getItem(int position) {
        return puzzles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_puzzle, parent, false);
        }

        ImageView previewImage = convertView.findViewById(R.id.previewImage);
        TextView puzzleSize = convertView.findViewById(R.id.puzzleSize);
        TextView solveTime = convertView.findViewById(R.id.solveTime);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        PuzzleEntity puzzle = puzzles.get(position);
        Log.d("amigomio", "size:" + puzzle.size);
        Log.d("amigomio", "position:" + position);
        Log.d("amigomio", "puzzles:" + puzzle.solveTime);
        puzzleSize.setText("Tamaño: " + puzzle.size + "x" + puzzle.size);
        solveTime.setText("Tiempo: " + puzzle.solveTime);

        // Cargar imagen desde la ruta guardada
        //File imgFile = new File(puzzle.imagePath);
        //if (imgFile.exists()) {
        //    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        //    previewImage.setImageBitmap(bitmap);
        //}
        previewImage.setImageBitmap(bytesToBitmap(puzzle.imageBytes));
        btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(puzzle);
            }
        });

        return convertView;
    }


    // Convertir byte[] a Bitmap
    public static Bitmap bytesToBitmap(byte[] imageBytes) {
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

}

