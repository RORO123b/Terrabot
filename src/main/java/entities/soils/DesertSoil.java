package entities.soils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DesertSoil extends Soil {
    private static final double NITROGEN_FACTOR = 0.5;
    private static final double WATER_FACTOR = 0.3;
    private static final double SALINITY_FACTOR = 2;
    
    private double salinity;

    /**
     * Calculates the quality score for desert soil.
     * @return The quality score
     */
    @Override
    public double calculateQuality() {
        return (nitrogen * NITROGEN_FACTOR) + (waterRetention * WATER_FACTOR)
                - (salinity * SALINITY_FACTOR);
    }
}

