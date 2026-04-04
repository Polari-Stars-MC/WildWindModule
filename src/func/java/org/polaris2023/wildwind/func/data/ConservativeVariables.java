package org.polaris2023.wildwind.func.data;

import static org.polaris2023.wildwind.func.data.euler.EulerEquationSolver.GAMMA;
import static org.polaris2023.wildwind.func.data.euler.EulerEquationSolver.R;

public class ConservativeVariables {
    public double density;      // ρ - 密度
    public double momentumX;    // ρu - x方向动量
    public double momentumY;    // ρv - y方向动量
    public double energy;       // ρE - 总能

    public ConservativeVariables(double density, double momentumX, double momentumY, double energy) {
        this.density = density;
        this.momentumX = momentumX;
        this.momentumY = momentumY;
        this.energy = energy;
    }

    public double pressure() {
        double u = velocityX();
        double v = velocityY();
        double kineticEnergy = 0.5 * density * (u*u + v*v);
        return (GAMMA - 1) * (energy - kineticEnergy);
    }

    public double velocityX() {
        return momentumX / density;
    }

    public double velocityY() {
        return momentumY / density;
    }

    public double speedOfSound() {
        return Math.sqrt(GAMMA * pressure() / density);
    }

    public double machNumber() {
        double velocity = Math.sqrt(velocityX()*velocityX() + velocityY()*velocityY());
        return velocity / speedOfSound();
    }

    public double temperature() {
        return pressure() / (R * density);
    }

    @Override
    public String toString() {
        return String.format("ρ=%.4f, ρu=%.4f, ρv=%.4f, ρE=%.4f, p=%.4f",
                density, momentumX, momentumY, energy, pressure());
    }
}
