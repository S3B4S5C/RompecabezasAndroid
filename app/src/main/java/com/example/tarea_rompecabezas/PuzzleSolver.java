package com.example.tarea_rompecabezas;

import java.util.*;

class PuzzleSolver {
    private PuzzleBoard puzzleBoard;
    private int rows, cols;

    public PuzzleSolver(PuzzleBoard puzzleBoard) {
        this.puzzleBoard = puzzleBoard;
        this.rows = puzzleBoard.getRows(); // Obtener filas dinámicamente
        this.cols = puzzleBoard.getCols(); // Obtener columnas dinámicamente
    }

    public List<Integer> solve() {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> visited = new HashSet<>();
        List<PuzzlePiece> initialState = puzzleBoard.getPuzzlePieces();

        Node startNode = new Node(initialState, null, -1, 0, heuristic(initialState));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (isGoal(current.state)) {
                return reconstructPath(current);
            }

            visited.add(serializeState(current.state));

            for (Node neighbor : getNeighbors(current)) {
                if (!visited.contains(serializeState(neighbor.state))) {
                    openSet.add(neighbor);
                }
            }
        }
        return null; // No hay solución
    }

    private List<Integer> reconstructPath(Node node) {
        List<Integer> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(node.movedPieceIndex);
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private boolean isGoal(List<PuzzlePiece> state) {
        for (int i = 0; i < state.size() - 1; i++) {
            PuzzlePiece piece = state.get(i);
            if (piece == null || piece.getOriginalRow() != i / cols || piece.getOriginalCol() != i % cols) {
                return false;
            }
        }
        return true;
    }


    private int heuristic(List<PuzzlePiece> state) {
        int h = 0;
        for (int i = 0; i < state.size(); i++) {
            PuzzlePiece piece = state.get(i);
            if (piece != null) {
                h += Math.abs(piece.getActualRow() - piece.getOriginalRow()) +
                        Math.abs(piece.getActualCol() - piece.getOriginalCol());
            }
        }
        return h;
    }


    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int emptyIndex = node.state.indexOf(null);
        int emptyRow = emptyIndex / cols, emptyCol = emptyIndex % cols;

        int[] dRows = {-1, 1, 0, 0};
        int[] dCols = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = emptyRow + dRows[i], newCol = emptyCol + dCols[i];
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                int newIndex = newRow * cols + newCol;
                List<PuzzlePiece> newState = new ArrayList<>(node.state);
                Collections.swap(newState, emptyIndex, newIndex);
                neighbors.add(new Node(newState, node, newIndex, node.g + 1, heuristic(newState)));
            }
        }
        return neighbors;
    }


    private String serializeState(List<PuzzlePiece> state) {
        StringBuilder sb = new StringBuilder();
        for (PuzzlePiece piece : state) {
            sb.append(piece == null ? "0" : (piece.getOriginalRow() * cols + piece.getOriginalCol() + 1)).append(",");
        }
        return sb.toString();
    }

    private static class Node {
        List<PuzzlePiece> state;
        Node parent;
        int movedPieceIndex;
        int g, f;

        Node(List<PuzzlePiece> state, Node parent, int movedPieceIndex, int g, int h) {
            this.state = state;
            this.parent = parent;
            this.movedPieceIndex = movedPieceIndex;
            this.g = g;
            this.f = g + h;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Nodo: f=").append(f).append(", g=").append(g).append("\n");
            for (int i = 0; i < state.size(); i++) {
                PuzzlePiece piece = state.get(i);
                sb.append(piece == null ? " [ ] " : String.format(" [%d,%d] ", piece.getOriginalRow(), piece.getOriginalCol()));
                if ((i + 1) % 3 == 0) sb.append("\n"); // Formato de 3x3
            }
            return sb.toString();
        }

    }
}

