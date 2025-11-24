package entities.plants;

public class Mosses extends Plant {
    private static final double POSSIBILITY = 40;
    private static final double OXYGEN = 0.8;

    public Mosses(final String name) {
        this.name = name;
        this.type = "Mosses";
        plantPossibility = POSSIBILITY;
        oxygenLevel = OXYGEN;
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for mosses.
     */
    @Override
    public void setOxygenLevel() {
        this.oxygenLevel = OXYGEN;
    }
}
