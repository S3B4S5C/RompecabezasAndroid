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
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;

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
        }
        // Inicialización del PuzzleBoard (ej. rompecabezas de 3x3)
        puzzleBoard = new PuzzleBoard(capturedImage, 3, 3);

        // Se asigna un adaptador para mostrar las piezas en un GridView
        gridView.setAdapter(new PuzzleAdapter(getContext(), puzzleBoard.getPuzzlePieces()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Mover pieza si es válida y actualizar el estado
                if (puzzleBoard.movePiece(position)) {
                    ((PuzzleAdapter) gridView.getAdapter()).updatePieces(puzzleBoard.getPuzzlePieces());
                if (puzzleBoard.isSolved()){
                    showGameCompletedDialog();
                }
                }
            }
        });
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Algoritmo solver = new Algoritmo(puzzleBoard);
                List<PuzzleBoard> solutionSteps = solver.solve();
                // Aquí se podría implementar una animación para mostrar cada paso de la solución
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
    private void animateSolution(List<PuzzleBoard> steps) {
        // Implementación para animar la solución (por ejemplo, con un Handler o ValueAnimator)
    }
}
