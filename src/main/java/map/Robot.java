package map;
import entities.plants.Plant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Robot {
    private static final int DIRECTIONS = 4;
    private static final int SCAN_ENERGY_COST = 7;

    private int x;
    private int y;
    private int energyPoints;
    private boolean isCharging = false;
    private int timestampReady = -1;

    public Robot(final int energyPoints) {
        x = 0;
        y = 0;
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
        this.energyPoints += timeToCharge;
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

    public String scanObject(String color, String smell, String sound, Map map) {
        String result;
        if (energyPoints - SCAN_ENERGY_COST < 0) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        energyPoints -= SCAN_ENERGY_COST;
        if (!color.equals("none") && !smell.equals("none") && sound.equals("none")) {
            result = "The scanned object is a plant.";
            if (map.getCell(x, y).getPlant() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
            Plant scannedPlant = map.getCell(x, y).getPlant();
            scannedPlant.setGrowing(true);
            double growthRate = 0;
            if (map.getCell(x, y).getWater() != null) {
                growthRate += 0.2;
            }
            if (map.getCell(x, y).getSoil() != null) {
                growthRate += 0.2;
            }
            scannedPlant.setGrowthRate(growthRate);
            scannedPlant.calculateGrowth();
            // Destroy plant if it has exceeded Old stage
            if (scannedPlant.shouldBeDestroyed()
                    && scannedPlant.getGrowthCapacity() >= 1.0) {
                map.getCell(x, y).setPlant(null);
            }
        } else if (!color.equals("none") && !smell.equals("none") && !sound.equals("none")) {
            result = "The scanned object is an animal.";
            if (map.getCell(x, y).getAnimal() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
        } else {
            result = "The scanned object is water.";
            if (map.getCell(x, y).getWater() == null) {
                return "ERROR: Object not found. Cannot perform action";
            }
        }
        return result;
    }
}
