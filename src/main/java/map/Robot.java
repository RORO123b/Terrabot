package map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Robot {
    private static final int DIRECTIONS = 4;
    private static final int DIVISOR = 4;
    private static final int MIN_ENERGY = 2;

    private int i;
    private int j;
    private int energyPoints;

    public Robot(final int energyPoints) {
        i = 0;
        j = 0;
        this.energyPoints = energyPoints;
    }

    /**
     * Moves the robot to the best adjacent cell based on environmental factors.
     * @param map The map containing the cells
     * @return A message indicating the result of the move
     */
    public final String moveRobot(final Map map) {
        if (energyPoints < MIN_ENERGY) {
            return "ERROR: Not enough battery left. Cannot perform action";
        }
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        int maxx = -1;

        for (int k = 0; k < DIRECTIONS; k++) {
            int tempI = i + dx[k];
            int tempJ = j + dy[k];
            if (tempI >= 0 && tempI < map.getHeight()
                    && tempJ >= 0 && tempJ < map.getWidth()) {
                double possibilityToGetStuckInSoil = 0;
                if (map.getCell(tempJ, tempI).getSoil() != null) {
                    possibilityToGetStuckInSoil =
                            map.getCell(tempJ, tempI).getSoil().possibilityToGetStuckInSoil();
                }

                double possibilityToGetDamagedByAir = 0;
                if (map.getCell(tempJ, tempI).getAir() != null) {
                    possibilityToGetDamagedByAir =
                            map.getCell(tempJ, tempI).getAir().getToxicityAQ();
                }

                double possibilityToBeAttackedByAnimal = 0;
                if (map.getCell(tempJ, tempI).getAnimal() != null) {
                    possibilityToBeAttackedByAnimal =
                            map.getCell(tempJ, tempI).getAnimal()
                                    .getPossibilityToBeAttackedByAnimal();
                }

                double possibilityToGetStuckInPlants = 0;
                if (map.getCell(tempJ, tempI).getPlant() != null) {
                    possibilityToGetStuckInPlants =
                            map.getCell(tempJ, tempI).getPlant()
                                    .getPossibilityToGetStuckInPlants();
                }

                double sum = possibilityToGetStuckInSoil + possibilityToGetDamagedByAir
                        + possibilityToBeAttackedByAnimal + possibilityToGetStuckInPlants;
                double mean = Math.abs(sum / DIVISOR);
                int result = (int) Math.round(mean);
                if (result > maxx) {
                    maxx = result;
                    i = tempI;
                    j = tempJ;
                }
            }
        }
        energyPoints -= MIN_ENERGY;

        return "The robot has successfully moved to position (" + i + ", " + j + ").";
    }
}
