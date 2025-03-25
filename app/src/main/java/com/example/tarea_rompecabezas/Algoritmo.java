package com.example.tarea_rompecabezas;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class Algoritmo {
    private PuzzleBoard initialBoard;

    public Algoritmo(PuzzleBoard board) {
        this.initialBoard = board;
    }

    private class Nodo {
        PuzzleBoard board;
        int g, h, f;
        Nodo padre;

        public Nodo(PuzzleBoard board, int g, Nodo padre) {
            this.board = board;
            this.g = g;
            this.h = board.manhattanDistance();
            this.f = g + h;
            this.padre = padre;
        }
    }

    public List<PuzzleBoard> solve() {
        PriorityQueue<Nodo> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        HashSet<PuzzleBoard> closedSet = new HashSet<>();

        Nodo start = new Nodo(initialBoard, 0, null);
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Nodo current = openSet.poll();

            if (current.board.isSolved()) {
                return reconstructPath(current);
            }

            closedSet.add(current.board);

            for (PuzzleBoard neighbor : current.board.getNeighbors()) {
                if (closedSet.contains(neighbor)) continue;

                Nodo neighborNode = new Nodo(neighbor, current.g + 1, current);
                openSet.add(neighborNode);
            }
        }

        return new ArrayList<>(); // No se encontró solución
    }

    private List<PuzzleBoard> reconstructPath(Nodo nodo) {
        List<PuzzleBoard> path = new ArrayList<>();
        while (nodo != null) {
            path.add(0, nodo.board);
            nodo = nodo.padre;
        }
        return path;
    }
}
