package map;

import entities.air.Air;
import entities.animals.Animal;
import entities.plants.Plant;
import entities.soils.Soil;
import entities.Water;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Cell {

    private static final double QUALITY_LOW = 40.0;
    private static final double QUALITY_MEDIUM = 70.0;

    private int i;
    private int j;
    private Air air;
    private Animal animal;
    private Plant plant;
    private Soil soil;
    private Water water;
    private String airQuality;
    private String soilQuality;

    /**
     * Counts how many non-null objects exist in this cell.
     *
     * @return number of objects placed in the cell
     */
    public int totalObjects() {
        int total = 0;
        if (animal != null) {
            total++;
        }
        if (plant != null) {
            total++;
        }
        if (water != null) {
            total++;
        }
        return total;
    }

    /**
     * Calculates and updates the air quality category
     * based on the air quality numeric value.
     */
    public void calculateAirQuality() {
        double value = air.getAirQuality();
        if (value < QUALITY_LOW) {
            airQuality = "poor";
        } else if (value < QUALITY_MEDIUM) {
            airQuality = "moderate";
        } else {
            airQuality = "good";
        }
    }

    /**
     * Calculates and updates the soil quality category
     * based on the soil quality numeric value.
     */
    public void calculateSoilQuality() {
        double value = soil.calculateQuality();
        if (value < QUALITY_LOW) {
            soilQuality = "poor";
        } else if (value < QUALITY_MEDIUM) {
            soilQuality = "moderate";
        } else {
            soilQuality = "good";
        }
    }

    /**
     * Sets the air object and updates the air quality.
     *
     * @param air the air entity assigned to this cell
     */
    public void setAir(final Air air) {
        this.air = air;
        calculateAirQuality();
    }

    /**
     * Sets the soil object and updates the soil quality.
     *
     * @param soil the soil entity assigned to this cell
     */
    public void setSoil(final Soil soil) {
        this.soil = soil;
        calculateSoilQuality();
    }
}
