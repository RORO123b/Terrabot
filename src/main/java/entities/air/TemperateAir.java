package entities.air;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TemperateAir extends Air {
    private static final double HUMIDITY_FACTOR = 0.7;
    private static final double POLLEN_FACTOR = 0.1;
    private static final double OXYGEN_FACTOR = 2;
    private static final double MAX_SCORE_VALUE = 84;

    private double pollenLevel;

    /**
     * Gets the maximum score for temperate air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for temperate air.
     */
    public void setAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_FACTOR) + (humidity * HUMIDITY_FACTOR)
                - (pollenLevel * POLLEN_FACTOR);
    }
}
