package controller;

import javafx.scene.control.Alert;
import model.CellType;
import model.GameMap;

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
}
