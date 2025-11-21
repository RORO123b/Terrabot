package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Detritivore extends Animal {
    private static final int ANIMAL_POSIBILITY_TO_ATTACK = 90;

    public Detritivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Detritivores";
        animalPossibility = ANIMAL_POSIBILITY_TO_ATTACK;
    }
}
