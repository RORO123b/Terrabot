package map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Robot {
    private static final int DIRECTIONS = 4;

    private int x;
    private int y;
    private int energyPoints;
    private static boolean isCharging = false;
    private static int timestampReady = -1;

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
            int total = 0;
            int tempX = x + dx[k];
            int tempY = y + dy[k];
            if (tempX >= 0 && tempX < map.getWidth()
                    && tempY >= 0 && tempY < map.getHeight()) {
                double possibilityToGetStuckInSoil = 0;

                if (map.getCell(tempX, tempY).getSoil() != null) {
                    possibilityToGetStuckInSoil =
                            map.getCell(tempX, tempY).getSoil().possibilityToGetStuckInSoil();
                    total++;
                }

                double possibilityToGetDamagedByAir = 0;

                if (map.getCell(tempX, tempY).getAir() != null) {
                    possibilityToGetDamagedByAir =
                            map.getCell(tempX, tempY).getAir().getToxicityAQ();
                    total++;
                }

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
        if(minn == Integer.MAX_VALUE) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        x = bestX;
        y = bestY;
        energyPoints -= minn;

        return "The robot has successfully moved to position (" + x + ", " + y + ").";
    }

    public final void rechargeBattery(final int timeToCharge, final int currentTime) {
        isCharging = true;
        timestampReady = currentTime + timeToCharge;
        this.energyPoints += timeToCharge;
    }

    public final boolean getIsCharging() {
        return isCharging;
    }

    public static final void checkBatteryCharging(final int currentTime) {
        if (isCharging && timestampReady <= currentTime) {
            isCharging = false;
            timestampReady = -1;
        }
    }
}
