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
    private static final double RAINFALL_FACTOR = 0.3;

    private double co2Level;
    private double rainfall;

    /**
     * Gets the maximum score for tropical air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for tropical air.
     */
    public void setAirQuality() {
        airQuality = (oxygenLevel * OXYGEN_FACTOR) + (humidity * HUMIDITY_FACTOR)
                - (co2Level * CO2_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
        airQuality += (rainfall * RAINFALL_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
    }
    /**
     * Changes weather by setting rainfall.
     * @param rainfall The rainfall amount
     */
    @Override
    public void changeWeather(final double rainfall) {
        this.rainfall = rainfall;
        setAirQuality();
    }

    public final double getCo2Level() {
        return Math.round(co2Level * PERCENT) / PERCENT;
    }
}
