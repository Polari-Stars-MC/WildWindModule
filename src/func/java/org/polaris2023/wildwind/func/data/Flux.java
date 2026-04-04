package org.polaris2023.wildwind.func.data;

public class Flux {
    public double massFlux;      // ρu
    public double momentumFluxX; // ρu²+p
    public double momentumFluxY; // ρuv
    public double energyFlux;    // (ρE+p)u

    public Flux(ConservativeVariables u, boolean isX) {
        double _u = isX ? u.velocityX() : u.velocityY();
        double p = u.pressure();

        this.massFlux = u.momentumX;
        this.momentumFluxX = u.momentumX * _u + p;
        this.momentumFluxY = u.momentumY * _u;
        this.energyFlux = (u.energy + p) * _u;
    }
}
