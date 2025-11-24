package entities.plants;

public class Ferns extends Plant {
    private static final double POSSIBILITY = 30;

    public Ferns(final String name) {
        this.name = name;
        type = "Ferns";
        plantPossibility = POSSIBILITY;
        oxygenLevel = 0;
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for ferns.
     */
    @Override
    public void setOxygenLevel() {
        this.oxygenLevel = 0;
    }
}
