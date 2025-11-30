package entities.plants;

public class FloweringPlants extends Plant {
    private static final double POSSIBILITY = 90;
    private static final double OXYGEN = 6.0;

    public FloweringPlants(final String name, final double mass) {
        this.type = "FloweringPlants";
        this.name = name;
        this.mass = mass;
        plantPossibility = POSSIBILITY;
        oxygenLevel = OXYGEN;
        setMaturityOxygenRate("Young");
        calculateOxygenLevel();
    }

    /**
     * Sets the oxygen level for flowering plants.
     */
    @Override
    public void setOxygenLevel() {
        oxygenLevel = OXYGEN;
    }
}
