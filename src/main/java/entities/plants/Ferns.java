package entities.plants;

public class Ferns extends Plant {
    private static final double POSSIBILITY = 30;

    public Ferns(final String name, final double mass) {
        this.name = name;
        type = "Ferns";
        this.mass = mass;
        plantPossibility = POSSIBILITY;
        oxygenLevel = 0;
        setMaturityOxygenRate("Young");
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for ferns.
     */
    @Override
    public void setOxygenLevel() {
        oxygenLevel = 0;
    }
}
