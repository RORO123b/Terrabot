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
    private static final double STORM_PENALTY = 30;

    private double dustParticles;
    private boolean desertStorm;

    public DesertAir(final String type, final String name, final double mass,
                     final double humidity, final double temperature,
                     final double oxygenLevel, final double dustParticles) {
        this.type = type;
        this.name = name;
        this.mass = mass;
        this.humidity = humidity;
        this.temperature = temperature;
        this.oxygenLevel = oxygenLevel;
        this.dustParticles = dustParticles;
        setAirQuality();
        calculateToxicityAQ();
    }

    public final int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for desert air.
     */
    public void setAirQuality() {
        airQuality = (oxygenLevel * OXYGEN_FACTOR) - (dustParticles * DUST_FACTOR)
                - (temperature * TEMP_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
        airQuality -= (desertStorm ? STORM_PENALTY : 0);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
    }

    /**
     * Changes weather by setting desert storm condition.
     * @param desertStorm Whether there is a desert storm
     */
    @Override
    public void changeWeather(final boolean storm) {
        this.desertStorm = storm;

        setAirQuality();
    }
}
