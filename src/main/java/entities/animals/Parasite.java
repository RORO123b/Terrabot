package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Parasite extends Animal {
    public Parasite(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Parasites";
    }
}
