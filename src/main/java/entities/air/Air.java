package entities.air;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Air extends Entity {
    protected static final double PERCENT = 100;

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

    /**
     * Gets the rounded air quality value
     * @return The rounded air quality
     */
    public double getAirQuality() {
        return Math.round(airQuality * PERCENT) / PERCENT;
    }

    /**
     * Gets the rounded oxygen level value.
     * @return The rounded oxygen level
     */
    public final double getOxygenLevel() {
        return Math.round(oxygenLevel * PERCENT) / PERCENT;
    }

    /**
     * Calculates the toxicity air quality
     */
    public void calculateToxicityAQ() {
        toxicityAQ = PERCENT * (1 - airQuality / getMaxScore());
        toxicityAQ = Math.round(toxicityAQ * PERCENT) / PERCENT;
        toxicityAQ = Math.max(0, Math.min(100, toxicityAQ));
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

    public final void addHumidity(final double humidity) {
        this.humidity += humidity;
        setAirQuality();
        calculateToxicityAQ();
    }

    public double getHumidity() {
        return Math.round(humidity * PERCENT) / PERCENT;
    }

    public boolean isToxic() {
        return toxicityAQ > 0.8 * getMaxScore();
    }
}
