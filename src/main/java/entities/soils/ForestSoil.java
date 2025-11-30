package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ForestSoil extends Soil {
    private static final double NITROGEN_FACTOR = 1.2;
    private static final double WATER_FACTOR = 1.5;
    private static final double LITTER_FACTOR = 0.3;
    private static final double ORGANIC_FACTOR = 2;
    private static final double WATER_WEIGHT = 0.6;
    private static final double LITTER_WEIGHT = 0.4;
    private static final double BASE_DIVISOR = 80;
    private static final double MULTIPLIER = 100;

    private double leafLitter;

    public ForestSoil(final String type, final String name, final double mass,
                      final double nitrogen, final double waterRetention,
                      final double soilpH, final double organicMatter, final double leafLitter) {
        this.type = type;
        this.name = name;
        this.mass = mass;
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
        this.leafLitter = leafLitter;
        calculateQuality();
    }

    /**
     * Calculates the raw quality score for forest soil
     * @return The raw quality score
     */
    @Override
    protected double calculateRawQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                + (waterRetention * WATER_FACTOR) + (leafLitter * LITTER_FACTOR);
    }

    /**
     * Calculates the possibility to get stuck in forest soil
     * @return The possibility to get stuck
     */
    @Override
    public double possibilityToGetStuckInSoil() {
        return (waterRetention * WATER_WEIGHT + leafLitter * LITTER_WEIGHT)
                / BASE_DIVISOR * MULTIPLIER;
    }
}
