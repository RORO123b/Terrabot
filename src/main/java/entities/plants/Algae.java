package entities.plants;

public class Algae extends Plant {
    private static final double POSSIBILITY = 20;
    private static final double OXYGEN = 0.5;

    public Algae(final String name, final double mass) {
        this.type = "Algae";
        this.name = name;
        this.mass = mass;
        plantPossibility = POSSIBILITY;
        oxygenLevel = OXYGEN;
        setMaturityOxygenRate("Young");
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
