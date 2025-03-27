package com.example.tarea_rompecabezas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import java.util.ArrayList;

import java.util.List;

public class PuzzleHistoryFragment extends Fragment {
    private PuzzleHistoryAdapter adapter;
    private PuzzleDatabase db;
    private List<PuzzlePiece> puzzlePieces;
    private ListView gridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle_history, container, false);
        gridView = view.findViewById(R.id.gridHistory);

        db = Room.databaseBuilder(getContext(), PuzzleDatabase.class, "puzzle_db").build();
        db.puzzleDao().getAllPuzzles().observe(getViewLifecycleOwner(), puzzles -> {
            if (puzzles == null) {
                puzzles = new ArrayList<>();
            }

            adapter = new PuzzleHistoryAdapter(getContext(), puzzles, puzzle -> {
                new Thread(() -> db.puzzleDao().delete(puzzle)).start();
            });

            gridView.setAdapter(adapter);
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PuzzleEntity puzzle = findPuzzleByPosition(position);
                if (puzzle != null) {
                    if (puzzle.isCompleted) {
                        showCompletedImage(puzzle.imagePath);
                    } else {
                        loadPuzzle(puzzle);
                    }
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PuzzleEntity puzzle = findPuzzleByPosition(position);
                if (puzzle != null) {
                    new Thread(() -> db.puzzleDao().delete(puzzle)).start();
                }
                return true;
            }
        });

        return view;
    }

    private List<PuzzlePiece> convertToPuzzlePieces(List<PuzzleEntity> puzzles) {
        // Implementa la conversión de PuzzleEntity a PuzzlePiece según la estructura
        return null;
    }

    private PuzzleEntity findPuzzleByPosition(int position) {
        // Lógica para encontrar el PuzzleEntity correspondiente
        return null;
    }

    private void showCompletedImage(String imagePath) {
        // Abre una actividad para mostrar la imagen completa
    }

    private void loadPuzzle(PuzzleEntity puzzle) {
        // Carga el puzzle en el estado guardado
    }
}
