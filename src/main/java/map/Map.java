package map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Map {

    private Cell[][] cells;
    private final int height;
    private final int width;

    public Map(final int height, final int width) {
        this.height = height;
        this.width = width;
        cells = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
                cells[i][j].setI(i);
                cells[i][j].setJ(j);
            }
        }
    }

    /**
     * Returns the cell at the given (x, y) coordinates
     *
     * @param x the column index (0-based)
     * @param y the row index (0-based)
     * @return the cell at the given coordinates, or null if out of bounds
     */
    public Cell getCell(final int x, final int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return cells[x][y];
        }
        return null;
    }
}
