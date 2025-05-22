package model;

public class GameMap {
    private final int rows;
    private final int cols;
    private CellType[][] map;

    public GameMap(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        map = new CellType[rows][cols];
        initializeMap();
    }

    private void initializeMap() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                map[i][j] = CellType.HERBE;

        // Bordures en rocher
        for (int i = 0; i < rows; i++) {
            map[i][0] = CellType.ROCHER;
            map[i][cols - 1] = CellType.ROCHER;
        }
        for (int j = 0; j < cols; j++) {
            map[0][j] = CellType.ROCHER;
            map[rows - 1][j] = CellType.ROCHER;
        }
    }

    public CellType getCell(int row, int col) {
        return map[row][col];
    }

    public void setCell(int row, int col, CellType type) {
        map[row][col] = type;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public void reset() {
        initializeMap();
    }
}
