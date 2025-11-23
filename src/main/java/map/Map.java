package map;

import java.util.ArrayList;
import java.util.List;

import entities.air.Air;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public final class Map {
    private static final int WEATHER_DURATION = 3;

    private Cell[][] cells;
    private final int height;
    private final int width;

    public Map(final int height, final int width) {
        this.height = height;
        this.width = width;
        cells = new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell(j, i);
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

    /**
     * Gets all cells that have the specified air type.
     * @param airType The type of air to search for
     * @return List of cells with the specified air type
     */
    public List<Cell> getCellsByAirType(final String airType) {
        List<Cell> cellsOfType = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j].getAir() != null
                        && cells[i][j].getAir().getType().equals(airType)) {
                    cellsOfType.add(cells[i][j]);
                }
            }
        }
        return cellsOfType;
    }

    /**
     * Changes the weather type for all cells in the map.
     *
     * @param type the new weather type
     * @param value the numeric value for weather change
     * @param season the season string for weather change
     * @param timestamp the timestamp for weather change
     */

    public void changeWeather(final String type, final double value, final int timestamp) {
        List<Cell> cellsOfType = getCellsByAirType(type);

        for (Cell cell : cellsOfType) {
            Air air = cell.getAir();
            air.changeWeather(value);
            cell.setTimestampWeatherFinished(timestamp + WEATHER_DURATION);
        }
    }

    /**
     * Changes the weather for cells with specified air type using a season.
     * @param type The air type to change
     * @param season The season value
     * @param timestamp The current timestamp
     */
    public void changeWeather(final String type, final String season, final int timestamp) {
        List<Cell> cellsOfType = getCellsByAirType(type);

        for (Cell cell : cellsOfType) {
            Air air = cell.getAir();
            air.changeWeather(season);
            cell.setTimestampWeatherFinished(timestamp + WEATHER_DURATION);
        }
    }

    /**
     * Changes the weather for cells with specified air type using a boolean state.
     * @param type The air type to change
     * @param state The boolean state value
     * @param timestamp The current timestamp
     */
    public void changeWeather(final String type, final boolean state, final int timestamp) {
        List<Cell> cellsOfType = getCellsByAirType(type);

        for (Cell cell : cellsOfType) {
            Air air = cell.getAir();
            air.changeWeather(state);
            cell.setTimestampWeatherFinished(timestamp + WEATHER_DURATION);
        }
    }

    /**
     * Checks if weather effects have finished for any cells.
     * @param currentTimestamp The current timestamp
     */
    public void checkWeatherFinished(final int currentTimestamp) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = cells[i][j];
                if (cell.getTimestampWeatherFinished() != -1
                        && cell.getTimestampWeatherFinished() <= currentTimestamp) {
                    cell.setTimestampWeatherFinished(-1);
                    cell.getAir().changeWeather(false);
                    cell.getAir().changeWeather("");
                    cell.getAir().changeWeather(0.0);
                }
            }
        }
    }

    public void updateEntities(Robot robot) {
        if (robot.getEnergyPoints() <= 0) {
            return;
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = cells[i][j];
                if (cell.getPlant() != null && cell.getPlant().isGrowing()) {
                    cell.getPlant().calculateGrowth();
                    cell.getAir().addOxygen(cell.getPlant().getOxygenLevel());
                    if (cell.getPlant().shouldBeDestroyed()
                            && cell.getPlant().getGrowthCapacity() >= 1.0) {
                        cell.setPlant(null);
                    }
                }
            }
        }
    }
}