package com.example.tarea_rompecabezas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.yalantis.ucrop.UCrop;
import java.io.File;
import java.io.IOException;
import android.app.Activity;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

import android.os.Environment;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.Intent;
import android.app.Activity;
import android.widget.LinearLayout;

import androidx.core.content.FileProvider;


public class CaptureFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CROP_IMAGE = UCrop.REQUEST_CROP;
    private ImageView imageView;
    private Uri imageUri;
    private Button btnCapture;
    private Button btnOpenGalery;
    private Button btnHistory;
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

    private void startCrop(Uri sourceUri) {
        if (sourceUri == null) {
            showError("No se pudo obtener la imagen.");
            return;
        }

        Uri destinationUri = Uri.fromFile(new File(requireContext().getCacheDir(), "cropped.jpg"));
        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Imagen cuadrada
                .start(requireContext(), this);
    }


//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
//    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (photoUri != null) {
                    startCrop(photoUri);  // Usa el archivo en vez de la miniatura
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri croppedUri = UCrop.getOutput(data);
                if (croppedUri != null) {
                    try {
                        capturedImage = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), croppedUri);
                        imageView.setImageBitmap(capturedImage);
                        showSizeSelectionDialog();
                    } catch (IOException e) {
                        showError("Error al cargar la imagen recortada.");
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showError("Error en el recorte.");
        }
    }


    private Uri photoUri;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(requireContext(),
                        "com.example.tarea_rompecabezas.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private Uri saveImageToCache(Bitmap bitmap) {
//        File cachePath = new File(requireContext().getCacheDir(), "captured.jpg");
//        try {
//            java.io.FileOutputStream stream = new java.io.FileOutputStream(cachePath);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            stream.flush();
//            stream.close();
//            return Uri.fromFile(cachePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    private Bitmap cropToSquare(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = Math.min(width, height);
        int xOffset = (width - size) / 2;
        int yOffset = (height - size) / 2;

        return Bitmap.createBitmap(bitmap, xOffset, yOffset, size, size);
    }

    private void showSizeSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Selecciona el tamaño del rompecabezas")
                .setItems(new String[]{"3x3", "4x4", "5x5"}, (dialog, which) -> {
                    int size = (which == 0) ? 3 : (which == 1) ? 4 : 5;
                    sendImageToPuzzleFragment(size);
                })
                .setCancelable(false)
                .show();
    }

    private void sendImageToPuzzleFragment(int size) {
        if (capturedImage != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("capturedImage", capturedImage);
            bundle.putInt("puzzleSize", size);

            PuzzleFragment puzzleFragment = new PuzzleFragment();
            puzzleFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, puzzleFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            showError("No se ha capturado ninguna imagen.");
        }
    }

    private void showError(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}
