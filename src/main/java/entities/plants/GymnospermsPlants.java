package entities.plants;

public class GymnospermsPlants extends Plant {
    private static final double POSSIBILITY = 60;

    public GymnospermsPlants(final String name, final double mass) {
        type = "GymnospermsPlants";
        this.name = name;
        this.mass = mass;
        plantPossibility = POSSIBILITY;
        oxygenLevel = 0;
        setMaturityOxygenRate("Young");
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for gymnosperms plants.
     */
    @Override
    public void setOxygenLevel() {
        oxygenLevel = 0;
    }
}
