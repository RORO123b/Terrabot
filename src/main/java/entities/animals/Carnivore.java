package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Carnivore extends Animal {
    public Carnivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Carnivores";
    }
}
