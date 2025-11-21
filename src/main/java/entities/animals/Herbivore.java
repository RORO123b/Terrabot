package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Herbivore extends Animal {
    private static final int ANIMAL_POSIBILITY_TO_ATTACK = 85;

    public Herbivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Herbivores";
        animalPossibility = ANIMAL_POSIBILITY_TO_ATTACK;
    }
}
