package entities.plants;

public class Algae extends Plant {
    private static final double POSSIBILITY = 20;
    private static final double OXYGEN = 0.8;

    public Algae(final String name) {
        type = "Algae";
        this.name = name;
        plantPossibility = POSSIBILITY;
        oxygenLevel = OXYGEN;
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for algae.
     */
    @Override
    public void setOxygenLevel() {
        this.oxygenLevel = OXYGEN;
    }
}
