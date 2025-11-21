package entities.plants;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Plant extends Entity {
    protected static final double YOUNG_RATE = 0.2;
    protected static final double MATURE_RATE = 0.7;
    protected static final double OXYGEN_RATE = 0.4;
    protected static final double POSSIBILITY_DIVISOR = 100.0;

    protected String type;
    protected double maturityOxygenRate;
    protected double oxygenLevel;
    protected double oxygenFromPlant;
    protected double plantPossibility;

    /**
     * Sets the maturity oxygen rate based on plant status.
     * @param status The status of the plant (Young, Mature, or Oxygen)
     */
    public void setMaturityOxygenRate(final String status) {
        if (status.equals("Young")) {
            this.maturityOxygenRate = YOUNG_RATE;
        } else if (status.equals("Mature")) {
            this.maturityOxygenRate = MATURE_RATE;
        } else if (status.equals("Oxygen")) {
            this.maturityOxygenRate = OXYGEN_RATE;
        }
    }

    /**
     * Calculates the oxygen level for the plant.
     */
    public void calculateOxygenLevel() {
        this.oxygenLevel = this.oxygenLevel + this.maturityOxygenRate + this.oxygenFromPlant;
    }

    /**
     * Gets the possibility to get stuck in plants.
     * @return The possibility as a decimal value
     */
    public double getPossibilityToGetStuckInPlants() {
        return plantPossibility / POSSIBILITY_DIVISOR;
    }
}
