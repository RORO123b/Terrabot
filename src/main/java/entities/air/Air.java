package entities.air;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Air extends Entity {
    protected static final double PERCENT = 100;
    protected static final double MAX_QUALITY = 100;
    protected static final double TOXIC_THRESHOLD = 0.8;

    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double airQuality;
    private double toxicityAQ;

    /**
     * Gets the maximum score for this air type
     * @return The maximum score
     */
    public abstract int getMaxScore();

    /**
     * Sets the air quality based on specific air type factors
     */
    public abstract void setAirQuality();

    public final double getAirQuality() {
        return Math.round(airQuality * PERCENT) / PERCENT;
    }

    public final double getOxygenLevel() {
        return Math.round(oxygenLevel * PERCENT) / PERCENT;
    }

    /**
     * Calculates the toxicity air quality
     */
    public void calculateToxicityAQ() {
        double normalizedAirQuality = Math.round(airQuality * PERCENT) / PERCENT;
        normalizedAirQuality = Math.max(0, Math.min(MAX_QUALITY, normalizedAirQuality));
        toxicityAQ = PERCENT * (1 - normalizedAirQuality / getMaxScore());
        toxicityAQ = Math.round(toxicityAQ * PERCENT) / PERCENT;
        toxicityAQ = Math.max(0, Math.min(MAX_QUALITY, toxicityAQ));
    }

    /**
     * Changes weather with a numeric value
     * @param value The numeric weather parameter
     */
    public void changeWeather(final double value) { }

    /**
     * Changes weather with a season string
     * @param season The season name
     */
    public void changeWeather(final String season) { }

    /**
     * Changes weather with a boolean condition
     * @param condition The weather condition
     */
    public void changeWeather(final boolean condition) { }

    /**
     * Adds oxygen to the air and recalculates air quality.
     * @param oxygen The amount of oxygen to add
     */
    public final void addOxygen(final double oxygen) {
        oxygenLevel += oxygen;
        setAirQuality();
        calculateToxicityAQ();
    }

    /**
     * Adds humidity to the air.
     * @param humidityToAdd The amount of humidity to add
     */
    public void addHumidity(final double humidityToAdd) {
        humidity += humidityToAdd;
        setAirQuality();
        calculateToxicityAQ();
    }

    public final double getHumidity() {
        return Math.round(humidity * PERCENT) / PERCENT;
    }

    /**
     * Checks if the air is toxic.
     * @return true if toxic, false otherwise
     */
    public final boolean isToxic() {
        return toxicityAQ > TOXIC_THRESHOLD * getMaxScore();
    }
}
