package com.example.tarea_rompecabezas;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.app.AlertDialog;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import java.util.List;

public class PuzzleFragment extends Fragment {
    private Bitmap capturedImage;
    private PuzzleBoard puzzleBoard;
    private GridView gridView;
    private Button btnSolve;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle, container, false);
        gridView = view.findViewById(R.id.gridView);
        btnSolve = view.findViewById(R.id.btnSolve);
        if(getArguments() != null) {
            capturedImage = getArguments().getParcelable("capturedImage");
            int puzzleSize = getArguments().getInt("puzzleSize", 3); // Valor por defecto 3x3
            puzzleBoard = new PuzzleBoard(capturedImage, puzzleSize, puzzleSize);
            gridView.setNumColumns(puzzleSize);
        }

        // Se asigna un adaptador para mostrar las piezas en un GridView
        gridView.setAdapter(new PuzzleAdapter(getContext(), puzzleBoard.getPuzzlePieces()));

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            if (puzzleBoard.movePiece(position)) {
                ((PuzzleAdapter) gridView.getAdapter()).updatePieces(puzzleBoard.getPuzzlePieces());
                if (puzzleBoard.isSolved()){
                    showGameCompletedDialog();
                }
            }
        });
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PuzzleSolver solver = new PuzzleSolver(puzzleBoard);
                List<Integer> solutionSteps = solver.solve();
                // Aquí se podría implementar una animación para mostrar cada paso de la solución
                Log.d("amigomio", solutionSteps.toString());
                animateSolution(solutionSteps);
            }
        });
        return view;
    }


    private void showGameCompletedDialog(){
        new AlertDialog.Builder(getContext()).setTitle("Felicidades")
                .setMessage("Has Completado el rompecabezas")
                .setPositiveButton("OK", (dialog, which) -> getActivity().finish())
                .setCancelable(false).show();
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
