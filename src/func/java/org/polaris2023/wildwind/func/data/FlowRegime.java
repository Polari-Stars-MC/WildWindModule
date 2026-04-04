package org.polaris2023.wildwind.func.data;

import java.util.Locale;

public enum FlowRegime {
    CONTINUUM( 0, 0.01),
    SLIP(0.001, 0.01),
    TRANSITION( 0.01, 0.1),
    MOLECULAR(0.1, 10),
    FREE_MOLECULAR( 10, Double.POSITIVE_INFINITY),
    ;

    private final String name;
    private final double lowerBound;
    private final double upperBound;

    FlowRegime(String name, double lowerBound, double upperBound) {
        this.name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    FlowRegime(double lowerBound, double upperBound) {
        this.name = name().toLowerCase(Locale.ROOT);
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public String getName() {
        return name;
    }

    public static FlowRegime fromKnudsenNumber(double kn) {
        if (kn < CONTINUUM.upperBound) return CONTINUUM;
        if (kn < SLIP.upperBound) return SLIP;
        if (kn < TRANSITION.upperBound) return TRANSITION;
        if (kn < MOLECULAR.upperBound) return MOLECULAR;
        return FREE_MOLECULAR;
    }

    /**
     * 根据克努森数判断流动区域
     *
     * @param kn 克努森数
     * @return 流动区域描述
     */
    public static String getFlowRegime(double kn) {
        if (kn < 0) {
            return "Invalid Knudsen number";
        } else if (kn <= 0.001) {
            return "Continuum Flow - NS equations can be used";
        } else if (kn <= 0.01) {
            return "Slip flow region - requires slip boundary conditions";
        } else if (kn <= 0.1) {
            return "Transitional flow region - requires DSMC or Burnett equations";
        } else if (kn <= 10) {
            return "Molecular Flow Region - Using Molecular Dynamics Simulation";
        } else {
            return "Free molecular flow region - intermolecular collisions can be ignored";
        }
    }
}
