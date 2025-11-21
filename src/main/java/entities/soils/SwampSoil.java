package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SwampSoil extends Soil {
    private static final double NITROGEN_FACTOR = 1.1;
    private static final double ORGANIC_FACTOR = 2.2;
    private static final double WATERLOGGING_FACTOR = 5;
    
    private double waterLogging;

    /**
     * Calculates the quality score for swamp soil
     * @return The quality score
     */
    @Override
    public double calculateQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (organicMatter * ORGANIC_FACTOR)
                - (waterLogging * WATERLOGGING_FACTOR);
    }
}
