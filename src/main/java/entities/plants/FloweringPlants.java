package entities.plants;

public class FloweringPlants extends Plant {
    private static final double POSSIBILITY = 90;
    private static final double OXYGEN = 6.0;

    public FloweringPlants(final String name) {
        type = "FloweringPlants";
        this.name = name;
        plantPossibility = POSSIBILITY;
        oxygenLevel = OXYGEN;
        calculateOxygenLevel();
    }
}
