package com.example.tarea_rompecabezas;

import java.util.ArrayList;
import java.util.List;

public class Algoritmo {
    private PuzzleBoard initialBoard;

    public Algoritmo (PuzzleBoard board) {
        this.initialBoard = board;
    }

    public List<PuzzleBoard> solve() {
        List<PuzzleBoard> solutionSteps = new ArrayList<>();
        // Implementar A*:
        // 1. Crear nodos con estado del puzzle.
        // 2. Calcular el costo (g + h) para cada nodo.
        // 3. Expandir nodos hasta alcanzar el estado final.
        // 4. Retroceder para obtener la secuencia de pasos.
        return solutionSteps;
    }
}
