package Entities.Plants;

import Entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Plants extends Entity {
    private String type;
    private String maturity_oxygen_rate;
    private double oxygen_level;
}
