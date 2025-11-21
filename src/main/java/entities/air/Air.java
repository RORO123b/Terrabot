package entities.air;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Air extends Entity {
    protected static final double PERCENT = 100;
    protected static final double PERCENT_DECIMAL = 100.0;
    
    protected double humidity;
    protected double temperature;
    protected double oxygenLevel;
    protected double airQuality;
    private double toxicityAQ;
    private double finalResult;

    /**
     * Gets the maximum score for this air type.
     * @return The maximum score
     */
    public abstract int getMaxScore();
    
    /**
     * Sets the air quality based on specific air type factors.
     */
    public abstract void setAirQuality();

    /**
     * Calculates the toxicity air quality.
     */
    public void calculateToxicityAQ() {
        toxicityAQ = PERCENT * (1 - airQuality / getMaxScore());
        finalResult = Math.round(toxicityAQ * PERCENT_DECIMAL) / PERCENT_DECIMAL;
    }
}
