package entities.air;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PolarAir extends Air {
    private static final double TEMP_FACTOR = 100;
    private static final double ICE_FACTOR = 0.05;
    private static final double OXYGEN_FACTOR = 2;
    private static final double MAX_SCORE_VALUE = 142;

    private double iceCrystalConcentration;

    /**
     * Gets the maximum score for polar air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for polar air.
     */
    public void setAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_FACTOR) + (TEMP_FACTOR - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_FACTOR);
    }
}
