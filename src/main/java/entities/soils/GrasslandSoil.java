package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GrasslandSoil extends Soil {
    private static final double NITROGEN_FACTOR = 1.3;
    private static final double ORGANIC_FACTOR = 1.5;
    private static final double ROOT_FACTOR = 0.8;
    private static final double BASE_VALUE = 50;
    private static final double WATER_WEIGHT = 0.5;
    private static final double BASE_DIVISOR = 75;
    private static final double MULTIPLIER = 100;

    private double rootDensity;

    /**
     * Calculates the raw quality score for grassland soil
     * @return The raw quality score
     */
    @Override
    protected double calculateRawQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                + (rootDensity * ROOT_FACTOR);
    }

    /**
     * Calculates the possibility to get stuck in grassland soil
     * @return The possibility to get stuck
     */
    @Override
    public double possibilityToGetStuckInSoil() {
        return ((BASE_VALUE - rootDensity) + waterRetention * WATER_WEIGHT)
                / BASE_DIVISOR * MULTIPLIER;
    }
}
