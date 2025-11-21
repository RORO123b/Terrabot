package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Omnivore extends Animal {
    private static final int ANIMAL_POSIBILITY_TO_ATTACK = 60;

    public Omnivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Omnivores";
        animalPossibility = ANIMAL_POSIBILITY_TO_ATTACK;
    }
}
