package map;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import entities.Water;
import entities.animals.Animal;
import entities.plants.Plant;
import fileio.CommandInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Robot {
    private static final int DIRECTIONS = 4;
    private static final int SCAN_ENERGY_COST = 7;
    private static final int LEARN_ENERGY_COST = 2;
    private static final double WATER_GROWTH_RATE = 0.2;
    private static final double SOIL_GROWTH_RATE = 0.2;
    private static final double ORGANIC_MATTER = 0.3;
    private static final double WATER_RETENTION_INCREMENT = 0.2;
    private static final double HUMIDITY_INCREMENT = 0.2;
    private static final int IMPROVE_ENERGY_COST = 10;
    private static final double OXYGEN_INCREMENT = 0.3;

    private int x;
    private int y;
    private int energyPoints;
    private boolean isCharging;
    private int timestampReady;
    private LinkedHashMap<String, List<String>> knowledgeBase;
    private List<String> inventory;

    public Robot(final int energyPoints) {
        x = 0;
        y = 0;
        knowledgeBase = new LinkedHashMap<>();
        inventory = new ArrayList<>();
        timestampReady = -1;
        this.energyPoints = energyPoints;
    }

    /**
     * Moves the robot to the best adjacent cell based on environmental factors.
     * @param map The map containing the cells
     * @return A message indicating the result of the move
     */
    public final String moveRobot(final Map map) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        int minn = Integer.MAX_VALUE;
        int bestX = x;
        int bestY = y;

        for (int k = 0; k < DIRECTIONS; k++) {
            int total = 2;
            int tempX = x + dx[k];
            int tempY = y + dy[k];
            if (tempX >= 0 && tempX < map.getWidth()
                    && tempY >= 0 && tempY < map.getHeight()) {
                double possibilityToGetStuckInSoil = 0;

                possibilityToGetStuckInSoil =
                        map.getCell(tempX, tempY).getSoil().possibilityToGetStuckInSoil();

                        double possibilityToGetDamagedByAir = 0;

                possibilityToGetDamagedByAir =
                        map.getCell(tempX, tempY).getAir().getToxicityAQ();

                double possibilityToBeAttackedByAnimal = 0;

                if (map.getCell(tempX, tempY).getAnimal() != null) {
                    possibilityToBeAttackedByAnimal =
                            map.getCell(tempX, tempY).getAnimal()
                                    .getPossibilityToBeAttackedByAnimal();
                    total++;
                }

                double possibilityToGetStuckInPlants = 0;

                if (map.getCell(tempX, tempY).getPlant() != null) {
                    possibilityToGetStuckInPlants =
                            map.getCell(tempX, tempY).getPlant()
                                    .getPossibilityToGetStuckInPlants();
                    total++;
                }

                double sum = possibilityToGetStuckInSoil + possibilityToGetDamagedByAir
                        + possibilityToBeAttackedByAnimal + possibilityToGetStuckInPlants;
                double mean = Math.abs(sum / total);
                int result = (int) Math.round(mean);

                if (result < minn && energyPoints >= result) {
                    minn = result;
                    bestX = tempX;
                    bestY = tempY;
                }
            }
        }
        if (minn == Integer.MAX_VALUE) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        x = bestX;
        y = bestY;
        energyPoints -= minn;

        return "The robot has successfully moved to position (" + x + ", " + y + ").";
    }

    /**
     * Recharges the robot's battery.
     * @param timeToCharge The time needed to charge
     * @param currentTime The current timestamp
     */
    public final void rechargeBattery(final int timeToCharge, final int currentTime) {
        isCharging = true;
        timestampReady = currentTime + timeToCharge;
        energyPoints += timeToCharge;
    }

    public final boolean getIsCharging() {
        return isCharging;
    }

    /**
     * Checks if battery charging is complete.
     * @param currentTime The current timestamp
     */
    public final void checkBatteryCharging(final int currentTime) {
        if (isCharging && timestampReady <= currentTime) {
            isCharging = false;
            timestampReady = -1;
        }
    }

    /**
     * Scans an object at the robot's current position.
     * @param color The color attribute of the object
     * @param smell The smell attribute of the object
     * @param sound The sound attribute of the object
     * @param map The map containing the cells
     * @param command The command input with the timestamp
     * @return A message describing the scanned object
     */
    public final String scanObject(final String color, final String smell,
                                     final String sound, final Map map,
                                     final CommandInput command) {
        String result;

        if (energyPoints - SCAN_ENERGY_COST < 0) {
            return "ERROR: Not enough energy to perform action";
        }
        if (!color.equals("none") && !smell.equals("none") && sound.equals("none")) {
            result = "The scanned object is a plant.";
            if (map.getCell(x, y).getPlant() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
            energyPoints -= SCAN_ENERGY_COST;

            Plant scannedPlant = map.getCell(x, y).getPlant();

            inventory.add(scannedPlant.getName());
            scannedPlant.setScanned(true);
            double growthRate = 0;

            if (map.getCell(x, y).getWater() != null) {
                growthRate += WATER_GROWTH_RATE;
            }
            if (map.getCell(x, y).getSoil() != null) {
                growthRate += SOIL_GROWTH_RATE;
            }
            scannedPlant.setGrowthRate(growthRate);
            scannedPlant.calculateGrowth();
            if (scannedPlant.shouldBeDestroyed()
                    && scannedPlant.getGrowthCapacity() >= 1.0) {
                map.getCell(x, y).setPlant(null);
            }
            map.addScannedPlant(scannedPlant);
        } else if (!color.equals("none") && !smell.equals("none") && !sound.equals("none")) {
            result = "The scanned object is an animal.";
            if (map.getCell(x, y).getAnimal() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
            energyPoints -= SCAN_ENERGY_COST;
            Animal scannedAnimal = map.getCell(x, y).getAnimal();

            inventory.add(scannedAnimal.getName());
            scannedAnimal.setScanned(true);
            scannedAnimal.setNextUpdate(command.getTimestamp() + 2);
            map.addScannedAnimal(scannedAnimal);
        } else {
            result = "The scanned object is water.";
            if (map.getCell(x, y).getWater() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
            energyPoints -= SCAN_ENERGY_COST;
            Water scannedWater = map.getCell(x, y).getWater();

            inventory.add(scannedWater.getName());
            scannedWater.setScanned(true);
            scannedWater.setNextUpdate(command.getTimestamp() + 2);
            map.addScannedWater(scannedWater);
        }
        return result;
    }

    /**
     * Learns a fact about a subject.
     * @param subject The subject to learn about
     * @param component The component to analyze
     * @param map The map containing entities
     * @return A message describing the learned fact
     */
    public final String learnFact(final String subject, final String component) {
        if (energyPoints - LEARN_ENERGY_COST < 0) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        boolean isScanned = false;

        for (String item : inventory) {
            if (item.equals(component)) {
                isScanned = true;
                break;
            }
        }

        if (!isScanned) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }

        knowledgeBase.putIfAbsent(component, new ArrayList<>());
        knowledgeBase.get(component).add(subject);
        energyPoints -= LEARN_ENERGY_COST;
        return "The fact has been successfully saved in the database.";
    }

    /**
     * Improves the environment.
     * @param improvementType The type of improvement
     * @param type The entity type
     * @param name The entity name
     * @param map The map containing entities
     * @return A message describing the improvement
     */
    public final String improveEnvironment(final String improvementType,
                                            final String type, final String name,
                                            final Map map) {

        if (energyPoints - IMPROVE_ENERGY_COST < 0) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        if (!(inventory.contains(name))) {
            return "ERROR: Subject not yet saved. Cannot perform action";
        }
        if (knowledgeBase.get(name) == null) {
            return "ERROR: Fact not yet saved. Cannot perform action";
        }
        List<String> improvements = knowledgeBase.get(name);

        if (improvementType.equals("plantVegetation")) {
            for (String improvement : improvements) {
                if (improvement.contains("plant")) {
                    map.getCell(x, y).getAir().addOxygen(OXYGEN_INCREMENT);
                    energyPoints -= IMPROVE_ENERGY_COST;
                    inventory.remove(name);
                    return "The " + name + " was planted successfully.";
                }
            }
        }

        if (improvementType.equals("fertilizeSoil")) {
            for (String improvement : improvements) {
                if (improvement.contains("fertilize")) {
                    map.getCell(x, y).getSoil().setOrganicMatter(ORGANIC_MATTER);
                    map.getCell(x, y).getSoil().calculateQuality();
                    energyPoints -= IMPROVE_ENERGY_COST;
                    inventory.remove(name);
                    return "The soil was successfully fertilized using " + name;
                }
            }
        }

        if (improvementType.equals("increaseMoisture")) {
            for (String improvement : improvements) {
                if (improvement.contains("increaseMoisture")) {
                    map.getCell(x, y).getSoil().addWaterRetention(WATER_RETENTION_INCREMENT);
                    energyPoints -= IMPROVE_ENERGY_COST;
                    inventory.remove(name);
                    return "The moisture was successfully increased using " + name;
                }
            }
        }

        if (improvementType.equals("increaseHumidity")) {
            for (String improvement : improvements) {
                if (improvement.contains("humidity")) {
                    map.getCell(x, y).getAir().setHumidity(
                            map.getCell(x, y).getAir().getHumidity() + HUMIDITY_INCREMENT);
                    energyPoints -= IMPROVE_ENERGY_COST;
                    inventory.remove(name);
                    return "The humidity was successfully increased using " + name;
                }
            }
        }
        return "ERROR: Fact not yet saved. Cannot perform action";
    }
}
