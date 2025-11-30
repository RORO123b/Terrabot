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
    private static final double WIND_FACTOR = 0.2;

    private double iceCrystalConcentration;
    private double windSpeed;

    public PolarAir(final String type, final String name, final double mass,
                    final double humidity, final double temperature,
                    final double oxygenLevel, final double iceCrystalConcentration) {
        this.type = type;
        this.name = name;
        this.mass = mass;
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.iceCrystalConcentration = iceCrystalConcentration;
        setAirQuality();
        calculateToxicityAQ();
    }

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
        airQuality = (oxygenLevel * OXYGEN_FACTOR) + (TEMP_FACTOR - Math.abs(temperature))
                - (iceCrystalConcentration * ICE_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
        airQuality -= (windSpeed * WIND_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
    }

    /**
     * Changes weather by setting wind speed.
     * @param speed The wind speed value
     */

    @Override
    public void changeWeather(final double speed) {
        this.windSpeed = speed;
        setAirQuality();
    }
}
