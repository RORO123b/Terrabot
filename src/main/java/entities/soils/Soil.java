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
        double normalizeScore = Math.max(0, Math.min(MAX_SCORE, calculateQuality()));
        finalResult = Math.round(normalizeScore * PERCENT) / PERCENT;
    }
}
