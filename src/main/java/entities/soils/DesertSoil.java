package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DesertSoil extends Soil {
    private static final double NITROGEN_FACTOR = 0.5;
    private static final double WATER_FACTOR = 0.3;
    private static final double SALINITY_FACTOR = 2;
    private static final double BASE_VALUE = 100;
    private static final double MULTIPLIER = 100;

    private double salinity;

    /**
     * Calculates the raw quality score for desert soil.
     * @return The raw quality score
     */
    @Override
    protected double calculateRawQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (waterRetention * WATER_FACTOR)
                - (salinity * SALINITY_FACTOR);
    }

    /**
     * Calculates the possibility to get stuck in desert soil
     * @return The possibility to get stuck
     */
    @Override
    public double possibilityToGetStuckInSoil() {
        return (BASE_VALUE - waterRetention + salinity) / BASE_VALUE * MULTIPLIER;
    }
}

