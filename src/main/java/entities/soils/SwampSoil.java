package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SwampSoil extends Soil {
    private static final double NITROGEN_FACTOR = 1.1;
    private static final double ORGANIC_FACTOR = 2.2;
    private static final double WATERLOGGING_FACTOR = 5;
    private static final double WATERLOGGING_MULTIPLIER = 10;

    private double waterLogging;

    /**
     * Calculates the raw quality score for swamp soil
     * @return The raw quality score
     */
    @Override
    protected double calculateRawQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                - (waterLogging * WATERLOGGING_FACTOR);
    }

    /**
     * Calculates the possibility to get stuck in swamp soil
     * @return The possibility to get stuck
     */
    @Override
    public double possibilityToGetStuckInSoil() {
        return waterLogging * WATERLOGGING_MULTIPLIER;
    }
}
