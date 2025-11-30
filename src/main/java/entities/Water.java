package entities;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.abs;

import fileio.WaterInput;

@Setter
@Getter
public final class Water extends Entity {

    private static final double MAX_PURITY = 100.0;
    private static final double IDEAL_PH = 7.5;
    private static final double MAX_PH_DIFF = 7.5;
    private static final double MAX_SALINITY = 350.0;
    private static final double MAX_TURBIDITY = 100.0;
    private static final double MAX_CONTAMINANT = 100.0;

    private static final double WEIGHT_PURITY = 0.3;
    private static final double WEIGHT_PH = 0.2;
    private static final double WEIGHT_SALINITY = 0.15;
    private static final double WEIGHT_TURBIDITY = 0.1;
    private static final double WEIGHT_CONTAMINANT = 0.15;
    private static final double WEIGHT_FROZEN = 0.2;

    private static final double QUALITY_SCALE = 100.0;

    private double salinity;
    private double pH;
    private double purity;
    private double turbidity;
    private double contaminantIndex;
    private boolean isFrozen;
    private double waterQuality;
    private boolean isScanned;
    private int nextUpdate;

    public Water(final WaterInput input) {
        this.type = input.getType();
        this.name = input.getName();
        this.mass = input.getMass();
        this.purity = input.getPurity();
        this.pH = input.getPH();
        this.salinity = input.getSalinity();
        this.turbidity = input.getTurbidity();
        this.contaminantIndex = input.getContaminantIndex();
        this.isFrozen = input.isFrozen();
        this.calculateWaterQuality();
    }
    /**
     * Calculates the normalized water quality based on formula
     */
    public void calculateWaterQuality() {
        purity = purity / MAX_PURITY;
        pH = 1 - abs(pH - IDEAL_PH) / MAX_PH_DIFF;
        salinity = 1 - (salinity / MAX_SALINITY);
        turbidity = 1 - (turbidity / MAX_TURBIDITY);
        contaminantIndex = 1 - (contaminantIndex / MAX_CONTAMINANT);

        int frozen = isFrozen ? 0 : 1;

        waterQuality =
                (WEIGHT_PURITY * purity
                        + WEIGHT_PH * pH
                        + WEIGHT_SALINITY * salinity
                        + WEIGHT_TURBIDITY * turbidity
                        + WEIGHT_CONTAMINANT * contaminantIndex
                        + WEIGHT_FROZEN * frozen)
                        * QUALITY_SCALE;
    }
}
