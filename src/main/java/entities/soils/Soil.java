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

    /**
     * Calculates the raw quality score for the soil (to be overridden by subclasses)
     * @return The raw quality score
     */
    protected abstract double calculateRawQuality();

    /**
     * Calculates and returns the normalized soil quality
     * @return The normalized quality score
     */
    public double calculateQuality() {
        double normalizeScore = Math.clamp(calculateRawQuality(), 0, MAX_SCORE);
        return Math.round(normalizeScore * PERCENT) / PERCENT;
    }

    /**
     * Calculates the possibility to get stuck in this soil type.
     * @return The possibility to get stuck
     */
    public abstract double possibilityToGetStuckInSoil();

    /**
     * Adds water retention to the soil.
     * @param water The amount of water retention to add
     */
    public final void addWaterRetention(final double water) {
        waterRetention += water;
        calculateQuality();
    }

    /**
     * Gets the water retention value.
     * @return The water retention rounded to 2 decimals
     */
    public final double getWaterRetention() {
        return Math.round(waterRetention * PERCENT) / PERCENT;
    }
}
