package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TundraSoil extends Soil {
    private static final double NITROGEN_FACTOR = 0.7;
    private static final double ORGANIC_FACTOR = 0.5;
    private static final double PERMAFROST_FACTOR = 1.5;
    
    private double permafrostDepth;

    /**
     * Calculates the quality score for tundra soil
     * @return The quality score
     */
    @Override
    public double calculateQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                - (permafrostDepth * PERMAFROST_FACTOR);
    }
}
