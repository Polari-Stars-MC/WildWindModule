package org.polaris2023.wildwind.func.data;

public class KnudsenAnalysis {
    private final double knudsenNumber;
    private final double meanFreePath;
    private final double characteristicLength;
    private final FlowRegime regime;

    public KnudsenAnalysis(double knudsenNumber, double meanFreePath, double characteristicLength, FlowRegime regime) {
        this.knudsenNumber = knudsenNumber;
        this.meanFreePath = meanFreePath;
        this.characteristicLength = characteristicLength;
        this.regime = regime;
    }

    public double getCharacteristicLength() {
        return characteristicLength;
    }

    public double getKnudsenNumber() {
        return knudsenNumber;
    }

    public double getMeanFreePath() {
        return meanFreePath;
    }

    public FlowRegime getRegime() {
        return regime;
    }

    private String getRecommendedMethod() {
        switch (regime) {
            case CONTINUUM: return "Navier-Stokes equations + no-slip boundary condition";
            case SLIP: return "Navier-Stokes equation + slip boundary condition";
            case TRANSITION: return "DSMC method or Burnett equations";
            case MOLECULAR: return "Molecular dynamics simulation or DSMC";
            case FREE_MOLECULAR: return "Free molecular flow theory";
            default: return "Unknown";
        }
    }

    @Override
    public String toString() {
        return """
               Knudsen number analysis results:
                Knudsen number (Kn) = %.6e,
                mean free path (λ) = %.6e m,
                Characteristic length (L) = %.6e m,
                Flow area = %s (%.6e - %.6e),
                Recommended Method: = %s
               """
                .formatted(
                        knudsenNumber,
                        meanFreePath,
                        characteristicLength,
                        regime.getName(),
                        regime.getLowerBound(),
                        regime.getUpperBound(),
                        getRecommendedMethod()
                );
    }
}
