package com.example.tarea_rompecabezas;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class PuzzleAdapter extends BaseAdapter {
    private Context context;
    private List<PuzzlePiece> puzzlePieces;

    public PuzzleAdapter(Context context, List<PuzzlePiece> pieces) {
        this.context = context;
        this.puzzlePieces = pieces;
    }

    public void updatePieces(List<PuzzlePiece> pieces) {
        this.puzzlePieces = pieces;
        notifyDataSetChanged();
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
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        PuzzlePiece piece = puzzlePieces.get(position);
        if(piece != null) {
            imageView.setImageBitmap(piece.getImage());
        } else {
            imageView.setImageResource(android.R.color.darker_gray); // Espacio vacío
        }
        return imageView;
    }
}
