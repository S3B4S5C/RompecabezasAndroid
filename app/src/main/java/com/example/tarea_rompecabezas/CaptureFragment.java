package com.example.tarea_rompecabezas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class CaptureFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageView;
    private Button btnCapture;

    private Bitmap capturedImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture, container, false);
        imageView = view.findViewById(R.id.imageView);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(v -> dispatchTakePictureIntent());
        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap originalBitmap = (Bitmap) extras.get("data");


            if (originalBitmap != null) {
                // Recortar la imagen a un cuadrado
                capturedImage = cropToSquare(originalBitmap);
                imageView.setImageBitmap(capturedImage);

                // Preguntar tamaño del rompecabezas
                showSizeSelectionDialog();
            }
        }
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height); // Tamaño del lado del cuadrado
        int xOffset = (width - size) / 2;
        int yOffset = (height - size) / 2;

        return Bitmap.createBitmap(bitmap, xOffset, yOffset, size, size);
    }

    private void showSizeSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selecciona el tamaño del rompecabezas")
                .setItems(new String[]{"3x3", "4x4", "5x5"}, (dialog, which) -> {
                    int size;
                    switch (which) {
                        case 0: size = 3; break;
                        case 1: size = 4; break;
                        case 2: size = 5; break;
                        default: size = 3; break;
                    }
                    sendImageToPuzzleFragment(size);
                })
                .setCancelable(false)
                .show();
    }

    private void sendImageToPuzzleFragment(int size) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("capturedImage", capturedImage);
        bundle.putInt("puzzleSize", size);

        PuzzleFragment puzzleFragment = new PuzzleFragment();
        puzzleFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, puzzleFragment)
                .addToBackStack(null)
                .commit();
    }
}
