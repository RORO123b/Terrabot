package entities.animals;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Animal extends Entity {
    protected static final double DIVISOR = 10.0;
    protected static final int BASE = 100;

    protected String type;
    protected double animalPossibility;

    /**
     * Gets the possibility to be attacked by an animal.
     * @return The possibility as a decimal value
     */
    public double getPossibilityToBeAttackedByAnimal() {
        return (BASE - animalPossibility) / DIVISOR;
    }
}
