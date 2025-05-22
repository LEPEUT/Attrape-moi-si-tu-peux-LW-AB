package controller;

import model.CellType;
import model.Direction;
import model.GameMap;

public class GameController {
    private final GameMap map;
    private int moutonRow, moutonCol;
    private int loupRow, loupCol;
    private int margueritesMangees = 0;
    private int cactusTouches = 0;
    private int ortiesTraversees = 0;


    private boolean bonusMarguerite = false;
    private boolean malusCactus = false;

    public GameController(GameMap map) {
        this.map = map;

        for (int i = 0; i < map.getRows(); i++) {
            for (int j = 0; j < map.getCols(); j++) {
                CellType cell = map.getCell(i, j);
                if (cell == CellType.MOUTON) {
                    moutonRow = i;
                    moutonCol = j;
                } else if (cell == CellType.LOUP) {
                    loupRow = i;
                    loupCol = j;
                }
            }
        }
    }

    public GameMap getMap() {
        return map;
    }

    public boolean deplacerMouton(Direction dir) {
        int newRow = moutonRow + dir.dRow();
        int newCol = moutonCol + dir.dCol();

        if (isValidMove(newRow, newCol)) {
            CellType destination = map.getCell(newRow, newCol);

            if (destination == CellType.MARGUERITE) {
                bonusMarguerite = true;
                margueritesMangees++;
            } else if (destination == CellType.CACTUS) {
                malusCactus = true;
                cactusTouches++;
            } else if (destination == CellType.ORTIE) {
                ortiesTraversees++;
            }

            map.setCell(moutonRow, moutonCol, CellType.HERBE);
            moutonRow = newRow;
            moutonCol = newCol;

            if (map.getCell(moutonRow, moutonCol) != CellType.SORTIE) {
                map.setCell(moutonRow, moutonCol, CellType.MOUTON);
            }

            return true;
        }

        return false;
    }

    public boolean deplacerLoup(Direction dir) {
        int newRow = loupRow + dir.dRow();
        int newCol = loupCol + dir.dCol();

        if (isValidMove(newRow, newCol)) {
            map.setCell(loupRow, loupCol, CellType.HERBE);
            loupRow = newRow;
            loupCol = newCol;
            map.setCell(loupRow, loupCol, CellType.LOUP);
            return true;
        }

        return false;
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < map.getRows() &&
                col >= 0 && col < map.getCols() &&
                map.getCell(row, col) != CellType.ROCHER;
    }

    public boolean isMoutonAttrape() {
        return moutonRow == loupRow && moutonCol == loupCol;
    }

    public boolean isVictoire() {
        return map.getCell(moutonRow, moutonCol) == CellType.SORTIE;
    }

    public boolean aBonusMarguerite() {
        return bonusMarguerite;
    }

    public void retirerBonusMarguerite() {
        bonusMarguerite = false;
    }

    public boolean aMalusCactus() {
        return malusCactus;
    }

    public void retirerMalusCactus() {
        malusCactus = false;
    }
    public int getMargueritesMangees() {
        return margueritesMangees;
    }

    public int getCactusTouches() {
        return cactusTouches;
    }

    public int getOrtiesTraversees() {
        return ortiesTraversees;
    }

}
