package map;

import java.util.ArrayList;
import java.util.List;

import entities.Water;
import entities.air.Air;
import entities.animals.Animal;
import entities.plants.Plant;
import fileio.CommandInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public final class Map {
    private static final int WEATHER_DURATION = 3;
    private static final int DIRECTIONS = 4;
    private static final double ORGANIC_MATTER_CARNIVORE_BOTH = 0.8;
    private static final double ORGANIC_MATTER_CARNIVORE_PLANT = 0.5;
    private static final double ORGANIC_MATTER_CARNIVORE_WATER = 0.5;
    private static final double ORGANIC_MATTER_HERBIVORE_BOTH = 0.8;
    private static final double ORGANIC_MATTER_HERBIVORE_PLANT = 0.5;
    private static final double ORGANIC_MATTER_HERBIVORE_WATER = 0.5;
    private static final double ORGANIC_MATTER_PREDATOR = 0.5;
    private static final double HUMIDITY_INCREMENT = 0.1;
    private static final double WATER_RETENTION_INCREMENT = 0.1;
    private static final double PLANT_GROWTH_RATE = 0.2;

    private Cell[][] cells;
    private final int height;
    private final int width;
    private List<Plant> scannedPlants = new ArrayList<>();
    private List<Water> scannedWaters = new ArrayList<>();
    private List<Animal> scannedAnimals = new ArrayList<>();

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
            return cells[y][x];
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

    public boolean hasTypeOfWeather(final String type) {
        List<Cell> cellsOfType = getCellsByAirType(type);
        return !cellsOfType.isEmpty();
    }

    /**
     * Adds a scanned plant to the list of scanned plants.
     * @param plant The plant to add
     */
    public void addScannedPlant(final Plant plant) {
        scannedPlants.add(plant);
    }

    /**
     * Adds a scanned water to the list of scanned waters.
     * @param water The water to add
     */
    public void addScannedWater(final Water water) {
        scannedWaters.add(water);
    }

    /**
     * Adds a scanned animal to the list of scanned animals.
     * @param animal The animal to add
     */
    public void addScannedAnimal(final Animal animal) {
        scannedAnimals.add(animal);
    }

    /**
     * Prints detailed debug information for all cells in the map.
     * Shows position, air, soil, water, plant, and animal attributes.
     */
    public void printDebugMap() {
        System.out.println("\n========== DEBUG MAP ==========");
        System.out.println("Map Size: " + width + " x " + height);
        System.out.println("Scanned Plants: " + scannedPlants.size());
        System.out.println("Scanned Waters: " + scannedWaters.size());
        System.out.println("Scanned Animals: " + scannedAnimals.size());
        System.out.println("===============================\n");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Cell cell = cells[i][j];
                System.out.println(">>> CELL [" + j + ", " + i + "] <<<");
                System.out.println("  Total Objects: " + cell.totalObjects());
                System.out.println("  Air Quality: " + cell.getAirQuality());
                System.out.println("  Soil Quality: " + cell.getSoilQuality());

                if (cell.getAir() != null) {
                    Air air = cell.getAir();
                    System.out.println("  AIR:");
                    System.out.println("    Type: " + air.getType());
                    System.out.println("    Air Quality (numeric): " + air.getAirQuality());
                    System.out.println("    Humidity: " + air.getHumidity());
                    System.out.println("    Temperature: " + air.getTemperature());
                    System.out.println("    Oxygen Level: " + air.getOxygenLevel());
                } else {
                    System.out.println("  AIR: null");
                }

                if (cell.getSoil() != null) {
                    var soil = cell.getSoil();
                    System.out.println("  SOIL:");
                    System.out.println("    Type: " + soil.getType());
                    System.out.println("    Quality (numeric): " + soil.calculateQuality());
                    System.out.println("    Organic Matter: " + soil.getOrganicMatter());
                    System.out.println("    Water Retention: " + soil.getWaterRetention());
                    System.out.println("    Nitrogen: " + soil.getNitrogen());
                    System.out.println("    Soil pH: " + soil.getSoilpH());
                } else {
                    System.out.println("  SOIL: null");
                }

                if (cell.getWater() != null) {
                    Water water = cell.getWater();
                    System.out.println("  WATER:");
                    System.out.println("    Type: " + water.getType());
                    System.out.println("    Water Quality: " + water.getWaterQuality());
                    System.out.println("    Mass: " + water.getMass());
                    System.out.println("    Scanned: " + water.isScanned());
                    System.out.println("    In Scanned List: " + scannedWaters.contains(water));
                } else {
                    System.out.println("  WATER: null");
                }

                if (cell.getPlant() != null) {
                    Plant plant = cell.getPlant();
                    System.out.println("  PLANT:");
                    System.out.println("    Type: " + plant.getType());
                    System.out.println("    Mass: " + plant.getMass());
                    System.out.println("    Growing: " + plant.isScanned());
                    System.out.println("    Growth Rate: " + plant.getGrowthRate());
                    System.out.println("    Growth Capacity: " + plant.getGrowthCapacity());
                    System.out.println("    Oxygen Level: " + plant.getOxygenLevel());
                    System.out.println("    In Scanned List: " + scannedPlants.contains(plant));
                } else {
                    System.out.println("  PLANT: null");
                }

                if (cell.getAnimal() != null) {
                    Animal animal = cell.getAnimal();
                    System.out.println("  ANIMAL:");
                    System.out.println("    Type: " + animal.getType());
                    System.out.println("    Mass: " + animal.getMass());
                    System.out.println("    State: " + animal.getState());
                    System.out.println("    Scanned: " + animal.isScanned());
                    System.out.println("    Sick: " + animal.isSick());
                    System.out.println("    Next Update: " + animal.getNextUpdate());
                    System.out.println("    Carnivore/Parasite: " + animal.isCarnivoreOrParasite());
                    System.out.println("    Organic Matter to Add: "
                            + animal.getOrganicMatterToAdd());
                    System.out.println("    In Scanned List: " + scannedAnimals.contains(animal));
                } else {
                    System.out.println("  ANIMAL: null");
                }

                System.out.println("  Weather Timestamp Finished: "
                        + cell.getTimestampWeatherFinished());
                System.out.println();
            }
        }
        System.out.println("========== END DEBUG MAP ==========\n");
    }

    /**
     * Moves a scanned animal to the best adjacent cell based on food availability.
     * @param animal The animal to move
     * @param timestamp The timestamp for the move
     * @param currentX The current x coordinate of the animal
     * @param currentY The current y coordinate of the animal
     */
    public void moveAnimal(final Animal animal, final int timestamp,
                          final int currentX, final int currentY) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        Cell bestCell = null;
        double bestWaterQuality = Double.MIN_VALUE;
        boolean predator = animal.isCarnivoreOrParasite();

        for (int k = 0; k < DIRECTIONS; k++) {
            int newX = currentX + dx[k];
            int newY = currentY + dy[k];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                Cell cell = cells[newY][newX];

                if (predator || cell.getAnimal() == null) {
                    if (cell.getPlant() != null && cell.getWater() != null) {
                        if (cell.getPlant().isScanned()
                                && cell.getWater().isScanned()) {
                            if (bestWaterQuality < cell.getWater().getWaterQuality()) {
                                bestWaterQuality = cell.getWater().getWaterQuality();
                                bestCell = cell;
                            }
                        }
                    }
                }
            }
        }

        if (bestCell != null) {
            cells[currentY][currentX].setAnimal(null);
            bestCell.setAnimal(animal);
            animal.setNextUpdate(timestamp + 2);
            return;
        }
        bestWaterQuality = Double.MIN_VALUE;
        for (int k = 0; k < DIRECTIONS; k++) {
            int newX = currentX + dx[k];
            int newY = currentY + dy[k];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                Cell cell = cells[newY][newX];

                if (predator || cell.getAnimal() == null) {
                    if (cell.getPlant() != null && cell.getPlant().isScanned()) {
                        bestCell = cell;
                        break;
                    }
                }
            }
        }

        if (bestCell != null) {
            cells[currentY][currentX].setAnimal(null);
            bestCell.setAnimal(animal);
            animal.setNextUpdate(timestamp + 2);
            return;
        }
        bestWaterQuality = Double.MIN_VALUE;
        for (int k = 0; k < DIRECTIONS; k++) {
            int newX = currentX + dx[k];
            int newY = currentY + dy[k];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                Cell cell = cells[newY][newX];

                if (predator || cell.getAnimal() == null) {
                    if (cell.getWater() != null && cell.getWater().isScanned()) {
                        if (cell.getWater().getWaterQuality() > bestWaterQuality) {
                            bestCell = cell;
                            bestWaterQuality = cell.getWater().getWaterQuality();
                        }
                    }
                }
            }
        }

        if (bestCell != null) {
            cells[currentY][currentX].setAnimal(null);
            bestCell.setAnimal(animal);
            animal.setNextUpdate(timestamp + 2);
            return;
        }
        bestWaterQuality = Double.MIN_VALUE;
        for (int k = 0; k < DIRECTIONS; k++) {
            int newX = currentX + dx[k];
            int newY = currentY + dy[k];

            if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                Cell cell = cells[newY][newX];

                if (predator || cell.getAnimal() == null) {
                    bestCell = cell;
                    break;
                }
            }
        }

        if (bestCell != null) {
            cells[currentY][currentX].setAnimal(null);
            bestCell.setAnimal(animal);
        }

        animal.setNextUpdate(timestamp + 2);
    }

    /**
     * Feeds an animal based on available food sources.
     * @param animal The animal to feed
     * @param currentX The x coordinate of the animal
     * @param currentY The y coordinate of the animal
     */
    public void feedAnimal(final Animal animal, final int currentX,
                          final int currentY) {
        Cell cell = cells[currentY][currentX];
        animal.setOrganicMatterToAdd(0.0);

        if (animal.isCarnivoreOrParasite()) {
            if (cell.getAnimal() != null && cell.getAnimal() != animal) {
                Animal prey = cell.getAnimal();
                animal.setMass(animal.getMass() + prey.getMass());
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_PREDATOR);
                animal.setState("well-fed");
                cell.setAnimal(animal);
                return;
            }

            if (cell.getPlant() != null && cell.getPlant().isScanned()
                    && cell.getWater() != null && cell.getWater().isScanned()) {
                double waterToDrink = Math.min(animal.getMass() * Animal.INTAKE_RATE,
                        cell.getWater().getMass());
                cell.getWater().setMass(cell.getWater().getMass() - waterToDrink);
                animal.setMass(animal.getMass() + waterToDrink + cell.getPlant().getMass());
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_CARNIVORE_BOTH);
                animal.setState("well-fed");
                cell.setPlant(null);
                return;
            }

            if (cell.getPlant() != null && cell.getPlant().isScanned()
                    && (cell.getWater() == null || !cell.getWater().isScanned())) {
                animal.setMass(animal.getMass() + cell.getPlant().getMass());
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_CARNIVORE_PLANT);
                animal.setState("well-fed");
                cell.setPlant(null);
                return;
            }

            if (cell.getWater() != null && cell.getWater().isScanned()
                    && (cell.getPlant() == null || !cell.getPlant().isScanned())) {
                double waterToDrink = Math.min(animal.getMass() * Animal.INTAKE_RATE,
                        cell.getWater().getMass());
                cell.getWater().setMass(cell.getWater().getMass() - waterToDrink);
                animal.setMass(animal.getMass() + waterToDrink);
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_CARNIVORE_WATER);
                animal.setState("well-fed");
                return;
            }

            animal.setState("hungry");
        } else {
            if (cell.getPlant() != null && cell.getPlant().isScanned()
                    && cell.getWater() != null && cell.getWater().isScanned()) {
                double waterToDrink = Math.min(animal.getMass() * Animal.INTAKE_RATE,
                        cell.getWater().getMass());
                cell.getWater().setMass(cell.getWater().getMass() - waterToDrink);
                animal.setMass(animal.getMass() + waterToDrink + cell.getPlant().getMass());
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_HERBIVORE_BOTH);
                animal.setState("well-fed");
                cell.setPlant(null);
                return;
            }

            if (cell.getPlant() != null && cell.getPlant().isScanned()
                    && (cell.getWater() == null || !cell.getWater().isScanned())) {
                animal.setMass(animal.getMass() + cell.getPlant().getMass());
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_HERBIVORE_PLANT);
                animal.setState("well-fed");
                cell.setPlant(null);
                return;
            }

            if (cell.getWater() != null && cell.getWater().isScanned()
                    && (cell.getPlant() == null || !cell.getPlant().isScanned())) {
                double waterToDrink = Math.min(animal.getMass() * Animal.INTAKE_RATE,
                        cell.getWater().getMass());
                cell.getWater().setMass(cell.getWater().getMass() - waterToDrink);
                animal.setMass(animal.getMass() + waterToDrink);
                animal.setOrganicMatterToAdd(ORGANIC_MATTER_HERBIVORE_WATER);
                animal.setState("well-fed");
                return;
            }

            animal.setState("hungry");
        }
    }

    /**
     * Updates all growing plants in the map.
     * @param robot The robot to check energy
     * @param command The command with current timestamp
     * @param lastTimestamp The last processed timestamp
     */
    public void updateEntities(final Robot robot, final CommandInput command,
                              final int lastTimestamp) {
        int currentTimestamp = command.getTimestamp();
        for (int t = lastTimestamp + 1; t <= currentTimestamp; t++) {
            for (Plant plant : scannedPlants) {
                if (plant != null && plant.isScanned()) {
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            if (cells[i][j].getPlant() == plant) {
                                double growthRate = 0;
                                if (cells[i][j].getWater() != null
                                        && cells[i][j].getWater().isScanned()) {
                                    growthRate += PLANT_GROWTH_RATE;
                                }
                                if (cells[i][j].getSoil() != null) {
                                    growthRate += PLANT_GROWTH_RATE;
                                }
                                plant.setGrowthRate(growthRate);

                                plant.calculateGrowth();
                                cells[i][j].getAir().addOxygen(plant.getOxygenLevel());
                                if (plant.shouldBeDestroyed()
                                        && plant.getGrowthCapacity() >= 1.0) {
                                    cells[i][j].setPlant(null);
                                }
                                break;
                            }
                        }
                    }
                }
            }

            for (Water water : scannedWaters) {
                if (water.getNextUpdate() <= t) {
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            if (cells[i][j].getWater() == water) {
                                cells[i][j].getAir().addHumidity(HUMIDITY_INCREMENT);
                                cells[i][j].getSoil()
                                        .addWaterRetention(WATER_RETENTION_INCREMENT);
                                water.setNextUpdate(t + 2);
                                break;
                            }
                        }
                    }
                }
            }

            for (Animal animal : scannedAnimals) {
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if (cells[i][j].getAnimal() == animal) {
                            if (cells[i][j].getAir().isToxic()) {
                                animal.setSick(true);
                                animal.setOrganicMatterToAdd(0);
                            }

                            if (animal.getOrganicMatterToAdd() > 0
                                    && cells[i][j].getSoil() != null) {
                                cells[i][j].getSoil().setOrganicMatter(
                                        cells[i][j].getSoil().getOrganicMatter()
                                        + animal.getOrganicMatterToAdd());
                                cells[i][j].getSoil().calculateQuality();
                                animal.setOrganicMatterToAdd(0);
                            }

                            if (animal.getNextUpdate() <= t) {
                                moveAnimal(animal, t, j, i);
                                for (int ci = 0; ci < height; ci++) {
                                    for (int cj = 0; cj < width; cj++) {
                                        if (cells[ci][cj].getAnimal() == animal) {
                                            feedAnimal(animal, cj, ci);
                                            break;
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}
