package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TundraSoil extends Soil {
    private static final double NITROGEN_FACTOR = 0.7;
    private static final double ORGANIC_FACTOR = 0.5;
    private static final double PERMAFROST_FACTOR = 1.5;
    private static final double BASE_VALUE = 50;
    private static final double MULTIPLIER = 100;

    private double permafrostDepth;

    public TundraSoil(final String type, final String name, final double mass,
                      final double nitrogen, final double waterRetention,
                      final double soilpH, final double organicMatter,
                      final double permafrostDepth) {
        this.type = type;
        this.name = name;
        this.mass = mass;
        this.nitrogen = nitrogen;
        this.waterRetention = waterRetention;
        this.soilpH = soilpH;
        this.organicMatter = organicMatter;
        this.permafrostDepth = permafrostDepth;
        calculateQuality();
    }

    /**
     * Calculates the raw quality score for tundra soil
     * @return The raw quality score
     */
    @Override
    protected double calculateRawQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                - (permafrostDepth * PERMAFROST_FACTOR);
    }

    /**
     * Calculates the possibility to get stuck in tundra soil
     * @return The possibility to get stuck
     */
    @Override
    public double possibilityToGetStuckInSoil() {
        return (BASE_VALUE - permafrostDepth) / BASE_VALUE * MULTIPLIER;
    }
}
