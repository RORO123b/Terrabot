package entities.plants;

public class GymnospermsPlants extends Plant {
    private static final double POSSIBILITY = 60;

    public GymnospermsPlants(final String name) {
        type = "GymnospermsPlants";
        this.name = name;
        plantPossibility = POSSIBILITY;
        oxygenLevel = 0;
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for gymnosperms plants.
     */
    @Override
    public void setOxygenLevel() {
        this.oxygenLevel = 0;
    }
}
