package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Parasite extends Animal {
    private static final int ANIMAL_POSIBILITY_TO_ATTACK = 60;

    public Parasite(final String name, final double mass) {
        super();
        this.name = name;
        this.mass = mass;
        this.type = "Parasites";
        animalPossibility = ANIMAL_POSIBILITY_TO_ATTACK;
    }
}
