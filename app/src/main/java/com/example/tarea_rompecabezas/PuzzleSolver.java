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
        Map<String, Integer> visited = new HashMap<>();
        List<PuzzlePiece> initialState = puzzleBoard.getPuzzlePieces();

        Node startNode = new Node(initialState, null, -1, 0, heuristic(initialState));
        openSet.add(startNode);
        visited.put(serializeState(initialState), startNode.g);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll(); // Saca el mejor nodo

            if (isGoal(current.state)) {
                return reconstructPath(current);
            }

            String currentStateKey = serializeState(current.state);

            for (Node neighbor : getNeighbors(current)) {
                String neighborKey = serializeState(neighbor.state);
                int newCost = neighbor.g;

                // Solo añadir si es un mejor camino
                if (!visited.containsKey(neighborKey) || newCost < visited.get(neighborKey)) {
                    visited.put(neighborKey, newCost);
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
        int linearConflict = 0;

        for (int i = 0; i < state.size(); i++) {
            PuzzlePiece piece = state.get(i);
            if (piece != null) {
                int goalRow = piece.getOriginalRow();
                int goalCol = piece.getOriginalCol();
                int currentRow = i / cols;
                int currentCol = i % cols;

                // Distancia Manhattan
                h += Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);

                // Linear Conflict
                if (currentRow == goalRow) { // Verificar conflicto en la misma fila
                    for (int j = i + 1; j < (currentRow + 1) * cols; j++) {
                        PuzzlePiece other = state.get(j);
                        if (other != null && other.getOriginalRow() == goalRow && other.getOriginalCol() < goalCol) {
                            linearConflict += 2;
                        }
                    }
                }
                if (currentCol == goalCol) { // Verificar conflicto en la misma columna
                    for (int j = i + cols; j < state.size(); j += cols) {
                        PuzzlePiece other = state.get(j);
                        if (other != null && other.getOriginalCol() == goalCol && other.getOriginalRow() < goalRow) {
                            linearConflict += 2;
                        }
                    }
                }
            }
        }
        return h + linearConflict;
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

        // Ordenar vecinos por f (priorizar los mejores caminos)
        neighbors.sort(Comparator.comparingInt(n -> n.f));

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

