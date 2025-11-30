package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Carnivore extends Animal {
    private static final int ANIMAL_POSIBILITY_TO_ATTACK = 30;

    public Carnivore(final String name, final double mass) {
        super();
        this.name = name;
        this.mass = mass;
        this.type = "Carnivores";
        animalPossibility = ANIMAL_POSIBILITY_TO_ATTACK;
    }
}
