package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class GrasslandSoil extends Soil {
    private static final double NITROGEN_FACTOR = 1.3;
    private static final double ORGANIC_FACTOR = 1.5;
    private static final double ROOT_FACTOR = 0.8;
    
    private double rootDensity;

    /**
     * Calculates the quality score for grassland soil
     * @return The quality score
     */
    @Override
    public double calculateQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                + (rootDensity * ROOT_FACTOR);
    }
}
