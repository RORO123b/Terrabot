package Entities.Plants;

import Entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Plants extends Entity {
    private String type;
    private float maturity_oxygen_rate;
    private float oxygen_level;
    private float oxygen_from_plant;
    private float plant_possibility;

    public void setMaturity_oxygen_rate(String status) {
        if(status.equals("Young")) {
            this.maturity_oxygen_rate = 0.2f;
        } else if(status.equals("Mature")) {
            this.maturity_oxygen_rate = 0.7f;
        } else if(status.equals("Oxygen")) {
            this.maturity_oxygen_rate = 0.4f;
        }
    }

    public void setOxygen_level() {
        this.oxygen_level = this.oxygen_level + this.maturity_oxygen_rate + this.oxygen_from_plant;
    }
}
