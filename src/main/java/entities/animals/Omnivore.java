package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Omnivore extends Animal {
    public Omnivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Omnivores";
    }
}
