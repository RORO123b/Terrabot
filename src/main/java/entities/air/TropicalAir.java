package entities.air;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TropicalAir extends Air {
    private static final double HUMIDITY_FACTOR = 0.5;
    private static final double CO2_FACTOR = 0.01;
    private static final double OXYGEN_FACTOR = 2;
    private static final double MAX_SCORE_VALUE = 82;
    
    private double co2Level;
    private final double maxScore = MAX_SCORE_VALUE;

    /**
     * Gets the maximum score for tropical air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) maxScore;
    }

    /**
     * Sets the air quality for tropical air.
     */
    public void setAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_FACTOR) + (humidity * HUMIDITY_FACTOR)
                - (co2Level * CO2_FACTOR);
    }
}
