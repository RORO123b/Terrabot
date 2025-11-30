package entities.air;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MountainAir extends Air {
    private static final double ALTITUDE_DIVISOR = 1000.0;
    private static final double ALTITUDE_FACTOR = 0.5;
    private static final double HUMIDITY_FACTOR = 0.6;
    private static final double OXYGEN_FACTOR = 2;
    private static final double MAX_SCORE_VALUE = 78;
    private static final double HIKER_FACTOR = 0.1;

    private double altitude;
    private double numberOfHikers;
    /**
     * Gets the maximum score for mountain air.
     * @return The maximum score
     */
    public int getMaxScore() {
        return (int) MAX_SCORE_VALUE;
    }

    /**
     * Sets the air quality for mountain air.
     */
    public void setAirQuality() {
        double oxygenFactor = oxygenLevel - ((altitude / ALTITUDE_DIVISOR)
                * ALTITUDE_FACTOR);
        airQuality = (oxygenFactor * OXYGEN_FACTOR) + (humidity * HUMIDITY_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
        airQuality -= (numberOfHikers * HIKER_FACTOR);
        airQuality = Math.max(0, Math.min(MAX_QUALITY, airQuality));
    }

    /**
     * Changes weather by setting number of hikers.
     * @param numberOfHikers The number of hikers
     */
    @Override
    public void changeWeather(final double numberOfHikers) {
        this.numberOfHikers = numberOfHikers;
        setAirQuality();
    }
}
