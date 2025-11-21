package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Herbivore extends Animal {
    public Herbivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Herbivores";
    }
}
