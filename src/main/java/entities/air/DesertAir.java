package entities.air;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DesertAir extends Air {
    private static final double DUST_FACTOR = 0.2;
    private static final double TEMP_FACTOR = 0.3;
    private static final double OXYGEN_FACTOR = 2;
    private static final double MAX_SCORE_VALUE = 65;
    
    private double dustParticles;

    /**
     * Gets the maximum score for desert air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for desert air.
     */
    public void setAirQuality() {
        this.airQuality = (oxygenLevel * OXYGEN_FACTOR) - (dustParticles * DUST_FACTOR)
                - (temperature * TEMP_FACTOR);
    }
}
