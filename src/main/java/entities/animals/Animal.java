package entities.animals;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Animal extends Entity {
    protected static final double DIVISOR = 10.0;
    protected static final int BASE = 100;
    public static final double INTAKE_RATE = 0.08;

    protected double animalPossibility;
    protected boolean isScanned = false;
    protected int nextUpdate;
    protected boolean isSick = false;
    protected String state = "hungry";
    protected double organicMatterToAdd = 0.0;

    /**
     * Gets the possibility to be attacked by an animal.
     * @return The possibility as a decimal value
     */
    public double getPossibilityToBeAttackedByAnimal() {
        return (BASE - animalPossibility) / DIVISOR;
    }

    /**
     * Checks if this animal is a carnivore or parasite.
     * @return true if carnivore or parasite, false otherwise
     */
    public boolean isCarnivoreOrParasite() {
        return type.equals("Carnivores") || type.equals("Parasites");
    }
}
