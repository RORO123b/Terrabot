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
    
    private double leafLitter;

    /**
     * Calculates the quality score for forest soil
     * @return The quality score
     */
    @Override
    public double calculateQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                + (waterRetention * WATER_FACTOR) + (leafLitter * LITTER_FACTOR);
    }
}

