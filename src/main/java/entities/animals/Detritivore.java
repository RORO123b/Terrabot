package entities.animals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Detritivore extends Animal {
    public Detritivore(final String name, final int mass) {
        super();
        this.name = name;
        this.mass = mass;
        type = "Detritivores";
    }
}
