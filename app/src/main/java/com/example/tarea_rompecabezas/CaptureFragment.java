package com.example.tarea_rompecabezas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture, container, false);
        imageView = view.findViewById(R.id.imageView);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
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
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            // Envío de la imagen al PuzzleFragment
            Bundle bundle = new Bundle();
            bundle.putParcelable("capturedImage", imageBitmap);
            PuzzleFragment puzzleFragment = new PuzzleFragment();
            puzzleFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, puzzleFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
