package entities.soils;

import entities.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public abstract class Soil extends Entity {
    protected static final double MAX_SCORE = 100;
    protected static final double PERCENT = 100.0;

    protected double nitrogen;
    protected double waterRetention;
    protected double soilpH;
    protected double organicMatter;
    private double finalResult;

    /**
     * Calculates the quality score for the soil
     * @return The quality score
     */
    public abstract double calculateQuality();

    /**
     * Calculates and normalizes the final result
     */
    public void calculateFinalResult() {
        double normalizeScore = Math.clamp(calculateQuality(), 0, MAX_SCORE);
        finalResult = Math.round(normalizeScore * PERCENT) / PERCENT;
    }

    /**
     * Calculates the possibility to get stuck in this soil type.
     * @return The possibility to get stuck
     */
    public abstract double possibilityToGetStuckInSoil();
}
