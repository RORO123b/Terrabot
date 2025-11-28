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
    private static final double SPRING_PENALTY = 15;

    private double pollenLevel;
    private String season = "";

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
        airQuality = (oxygenLevel * OXYGEN_FACTOR) + (humidity * HUMIDITY_FACTOR)
                - (pollenLevel * POLLEN_FACTOR)
                - (season.equalsIgnoreCase("Spring") ? SPRING_PENALTY : 0);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
    }

    /**
     * Changes weather by setting the season.
     * @param season The season name
     */
    @Override
    public void changeWeather(final String season) {
        this.season = season;
        setAirQuality();
    }
}
