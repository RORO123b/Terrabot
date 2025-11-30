package entities.plants;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Plant extends Entity {
    protected static final double YOUNG_RATE = 0.2;
    protected static final double MATURE_RATE = 0.7;
    protected static final double OLD_RATE = 0.4;
    protected static final double POSSIBILITY_DIVISOR = 100.0;

    protected double maturityOxygenRate;
    protected double oxygenLevel;
    protected double plantPossibility;
    protected boolean isScanned;
    protected double growthRate;
    protected double growthCapacity;

    /**
     * Sets the maturity oxygen rate based on plant status.
     * @param status The status of the plant (Young, Mature, or Old)
     */
    public void setMaturityOxygenRate(final String status) {
        if (status.equals("Young")) {
            maturityOxygenRate = YOUNG_RATE;
        } else if (status.equals("Mature")) {
            maturityOxygenRate = MATURE_RATE;
        } else if (status.equals("Old")) {
            maturityOxygenRate = OLD_RATE;
        }
    }

    /**
     * Sets the oxygen level for this plant type.
     */
    public void setOxygenLevel() { }

    /**
     * Calculates the oxygen level for the plant.
     */
    public void calculateOxygenLevel() {
        setOxygenLevel();
        oxygenLevel = oxygenLevel + maturityOxygenRate;
    }

    /**
     * Gets the oxygen level of the plant.
     * @return The oxygen level
     */
    public final double getOxygenLevel() {
        return oxygenLevel;
    }
    /**
     * Gets the possibility to get stuck in plants.
     * @return The possibility as a decimal value
     */
    public double getPossibilityToGetStuckInPlants() {
        return plantPossibility / POSSIBILITY_DIVISOR;
    }

    /**
     * Calculates the growth of the plant if it is growing.
     */
    public final void calculateGrowth() {
        if (isScanned) {
            growthCapacity += growthRate;
            if (growthCapacity > 1.0) {
                growthCapacity -= 1.0;
                nextMaturity();
            }
            calculateOxygenLevel();
        }
    }

    /**
     * Checks if the plant has exceeded its lifetime (gone past Old stage).
     * @return true if the plant should be destroyed, false otherwise
     */
    public boolean shouldBeDestroyed() {
        return maturityOxygenRate == OLD_RATE;
    }

    /**
     * Advances the plant to the next maturity stage.
     */
    public void nextMaturity() {
        if (maturityOxygenRate == YOUNG_RATE) {
            maturityOxygenRate = MATURE_RATE;
        } else if (maturityOxygenRate == MATURE_RATE) {
            maturityOxygenRate = OLD_RATE;
        }
    }
}
