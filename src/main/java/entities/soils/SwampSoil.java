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

    public SwampSoil(final String type, final String name, final double mass,
                     final double nitrogen, final double waterRetention,
                     final double soilpH, final double organicMatter, final double waterLogging) {
        this.type = type;
        this.name = name;
        this.mass = mass;
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
        this.waterLogging = waterLogging;
        calculateQuality();
    }

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
