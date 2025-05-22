package model;

public enum Direction {
    HAUT(-1, 0),
    BAS(1, 0),
    GAUCHE(0, -1),
    DROITE(0, 1);

    private final int dRow;
    private final int dCol;

    Direction(int dRow, int dCol) {
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public int dRow() {
        return dRow;
    }

    public int dCol() {
        return dCol;
    }
}
