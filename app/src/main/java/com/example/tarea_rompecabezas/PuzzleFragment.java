package com.example.tarea_rompecabezas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.app.AlertDialog;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.os.Handler;

import java.io.ByteArrayOutputStream;
import java.util.List;
import androidx.room.Room;

public class PuzzleFragment extends Fragment {
    private Bitmap capturedImage;
    private PuzzleBoard puzzleBoard;
    private GridView gridView;
    private Button btnSolve;
    private long startTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle, container, false);
        gridView = view.findViewById(R.id.gridView);
        btnSolve = view.findViewById(R.id.btnSolve);

        if(getArguments() != null) {
            capturedImage = getArguments().getParcelable("capturedImage");
            int puzzleSize = getArguments().getInt("puzzleSize", 3); // Valor por defecto 3x3
            puzzleBoard = new PuzzleBoard(capturedImage, puzzleSize, puzzleSize, requireContext());
            gridView.setNumColumns(puzzleSize);
            gridView.setVerticalSpacing(10);
            gridView.setHorizontalSpacing(10);
            startTime = System.currentTimeMillis();
        }

        // Se asigna un adaptador para mostrar las piezas en un GridView
        gridView.setAdapter(new PuzzleAdapter(getContext(), puzzleBoard.getPuzzlePieces()));

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            if (puzzleBoard.movePiece(position)) {
                ((PuzzleAdapter) gridView.getAdapter()).updatePieces(puzzleBoard.getPuzzlePieces());

                if (puzzleBoard.isSolved()){
                    Log.d("amigomio", "Tiempo");
                    capturedImage = resizeBitmap(capturedImage, 300, 300);
                    saveOnDatabase(bitmapToBytes(capturedImage), puzzleBoard.getCols(), System.currentTimeMillis() - startTime, true);
                    showGameCompletedDialog();
                }
            }
        });
        btnSolve.setOnClickListener(v -> new SolvePuzzleTask().execute());
        return view;
    }
    private void saveOnDatabase(byte[] imageBytes, int size, long solveTime, boolean isCompleted) {
        PuzzleEntity puzzle = new PuzzleEntity();
        puzzle.imagePath = "";
        puzzle.imageBytes = imageBytes;
        puzzle.size = size;
        puzzle.isCompleted = isCompleted;
        puzzle.solveTime = solveTime;

        Log.d("amigomio", "Hola " + imageBytes);
        new Thread(() -> {
            PuzzleDatabase db = Room.databaseBuilder(requireContext(),
                    PuzzleDatabase.class, "puzzle_db").allowMainThreadQueries().build();
            db.puzzleDao().insert(puzzle);
        }).start();
        Log.d("amigomio", "Tiempo de resolución: " + solveTime + "ms");
    }

    public Bitmap resizeBitmap(Bitmap original, int maxWidth, int maxHeight) {
        int width = original.getWidth();
        int height = original.getHeight();

        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;

        if (ratioMax > ratioBitmap) {
            finalWidth = (int) (maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) (maxWidth / ratioBitmap);
        }

        return Bitmap.createScaledBitmap(original, finalWidth, finalHeight, true);
    }
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private class SolvePuzzleTask extends AsyncTask<Void, Void, List<Integer>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            // Mostrar el ProgressDialog antes de iniciar el algoritmo
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Resolviendo el rompecabezas...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected List<Integer> doInBackground(Void... voids) {
            // Ejecutar el algoritmo de resolución en segundo plano
            PuzzleSolver solver = new PuzzleSolver(puzzleBoard);
            return solver.solve();
        }

        @Override
        protected void onPostExecute(List<Integer> solutionSteps) {
            // Ocultar el ProgressDialog y ejecutar la animación
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            Log.d("amigomio", solutionSteps.toString());
            animateSolution(solutionSteps);
        }
    }


    private void showGameCompletedDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Felicidades")
                .setMessage("Has completado el rompecabezas. ¿Quieres volver al menú principal?")
                .setPositiveButton("Sí", (dialog, which) -> returnToMainMenu())
                .setNegativeButton("No", (dialog, which) -> getActivity().finish())
                .setCancelable(false)
                .show();
    }

    private void returnToMainMenu() {
        Intent intent = new Intent(getActivity(), MainActivity.class); // Reemplaza con el nombre de tu actividad de menú
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish(); // Cierra la actividad actual para evitar que el usuario regrese con "atrás"
    }
    public void animateSolution(List<Integer> steps) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < steps.size()) {
                    if (puzzleBoard.movePiece(steps.get(index))) {
                        ((PuzzleAdapter) gridView.getAdapter()).updatePieces(puzzleBoard.getPuzzlePieces());
                        if (puzzleBoard.isSolved()){
                            capturedImage = resizeBitmap(capturedImage, 300, 300);
                            saveOnDatabase(bitmapToBytes(capturedImage), puzzleBoard.getCols(), System.currentTimeMillis() - startTime, false);
                            showGameCompletedDialog();
                        }
                    }
                    index++;
                    handler.postDelayed(this, 500); // Retraso de 500ms entre movimientos
                }
            }
        }, 500);
    }
}
