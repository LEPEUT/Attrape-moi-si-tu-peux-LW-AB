package controller;

import javafx.scene.control.Alert;
import model.CellType;
import model.GameMap;
import java.util.Queue;
import java.util.LinkedList;



public class SetupController {
    private final GameMap gameMap;

    public SetupController(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public void setCell(int row, int col, CellType type) {
        gameMap.setCell(row, col, type);
    }

    public void resetMap() {
        gameMap.reset();
    }

    public GameMap getMap() {
        return gameMap;
    }

    public boolean isConfigurationValide() {
        boolean hasLoup = false, hasMouton = false, hasSortie = false;

        for (int i = 0; i < gameMap.getRows(); i++) {
            for (int j = 0; j < gameMap.getCols(); j++) {
                CellType t = gameMap.getCell(i, j);
                if (t == CellType.LOUP) hasLoup = true;
                if (t == CellType.MOUTON) hasMouton = true;
                if (t == CellType.SORTIE) hasSortie = true;
            }
        }

        return hasLoup && hasMouton && hasSortie;
    }

    public void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de configuration");
        alert.setContentText("Il faut une case de SORTIE, un MOUTON et un LOUP !");
        alert.showAndWait();
    }

    public static boolean estConnexe(GameMap map) {
        int rows = map.getRows();
        int cols = map.getCols();
        boolean[][] visited = new boolean[rows][cols];

        // Trouver la position du mouton
        int startRow = -1, startCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map.getCell(i, j) == CellType.MOUTON) {
                    startRow = i;
                    startCol = j;
                    break;
                }
            }
        }

        if (startRow == -1) return false;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int r = pos[0], c = pos[1];

            if (map.getCell(r, c) == CellType.SORTIE) return true;

            int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};
            for (int[] d : directions) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                        !visited[nr][nc] &&
                        map.getCell(nr, nc) != CellType.ROCHER) {

                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }

        return false; 
    }

}
