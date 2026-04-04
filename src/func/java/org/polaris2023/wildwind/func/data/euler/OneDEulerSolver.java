package org.polaris2023.wildwind.func.data.euler;

import org.polaris2023.wildwind.func.data.ConservativeVariables;
import org.polaris2023.wildwind.func.data.Flux;
import org.polaris2023.wildwind.func.data.flux.FluxX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.polaris2023.wildwind.func.data.euler.EulerEquationSolver.GAMMA;

public class OneDEulerSolver {

    public static final Logger LOGGER = LoggerFactory.getLogger(OneDEulerSolver.class);

    private int nx;                 // 网格点数
    private double dx;              // 空间步长
    private double dt;              // 时间步长
    private double cfl;             // CFL数
    private ConservativeVariables[] u;  // 守恒变量
    private ConservativeVariables[] u_new;

    public OneDEulerSolver(int nx, double length, double cfl) {
        this.nx = nx;
        this.dx = length / (nx - 1);
        this.cfl = cfl;
        this.u = new ConservativeVariables[nx];
        this.u_new = new ConservativeVariables[nx];
    }

    // 初始化流场（激波管问题 - Sod问题）
    public void initializeSodShockTube() {
        for (int i = 0; i < nx; i++) {
            double x = i * dx;
            if (x < 0.5) {
                // 左状态（高压区）
                double rho = 1.0;
                double _u = 0.0;
                double p = 1.0;
                double E = p / ((GAMMA - 1) * rho) + 0.5 * _u * _u;
                u[i] = new ConservativeVariables(rho, rho * _u, 0, rho * E);
            } else {
                // 右状态（低压区）
                double rho = 0.125;
                double _u = 0.0;
                double p = 0.1;
                double E = p / ((GAMMA - 1) * rho) + 0.5 * _u * _u;
                u[i] = new ConservativeVariables(rho, rho * _u, 0, rho * E);
            }
        }
    }

    // 计算时间步长
    private double computeTimeStep() {
        double maxSpeed = 0;
        for (int i = 0; i < nx; i++) {
            double speed = Math.abs(u[i].velocityX()) + u[i].speedOfSound();
            maxSpeed = Math.max(maxSpeed, speed);
        }
        return cfl * dx / maxSpeed;
    }

    // 边界条件（反射边界）
    private void applyBoundaryConditions() {
        // 左边界
        u[0] = u[1];
        // 右边界
        u[nx-1] = u[nx-2];
    }

    // MacCormack方法求解一维欧拉方程
    public void solve(double finalTime) {
        double time = 0;
        int iter = 0;

        LOGGER.info("One-dimensional Euler equation solution (Sod shock tube problem)");
        LOGGER.info("=================================");
        LOGGER.info("Number of grid points: {}, CFL Number: {}", nx, "%.2f".formatted(cfl));

        while (time < finalTime) {
            dt = computeTimeStep();
            if (time + dt > finalTime) {
                dt = finalTime - time;
            }

            // 预测步（前向差分）
            ConservativeVariables[] U_pred = new ConservativeVariables[nx];
            for (int i = 1; i < nx-1; i++) {
                Flux F_i = new FluxX(u[i]);
                Flux F_im1 = new FluxX(u[i-1]);

                double dU_dt_rho = -(F_i.massFlux - F_im1.massFlux) / dx;
                double dU_dt_mom = -(F_i.momentumFluxX - F_im1.momentumFluxX) / dx;
                double dU_dt_energy = -(F_i.energyFlux - F_im1.energyFlux) / dx;

                double rho_pred = u[i].density + dt * dU_dt_rho;
                double mom_pred = u[i].momentumX + dt * dU_dt_mom;
                double energy_pred = u[i].energy + dt * dU_dt_energy;

                U_pred[i] = new ConservativeVariables(rho_pred, mom_pred, 0, energy_pred);
            }

            // 校正步（后向差分）
            for (int i = 1; i < nx-1; i++) {
                Flux F_pred_i = new FluxX(U_pred[i]);
                Flux F_pred_ip1 = new FluxX(U_pred[i+1]);

                double dU_dt_rho = -(F_pred_ip1.massFlux - F_pred_i.massFlux) / dx;
                double dU_dt_mom = -(F_pred_ip1.momentumFluxX - F_pred_i.momentumFluxX) / dx;
                double dU_dt_energy = -(F_pred_ip1.energyFlux - F_pred_i.energyFlux) / dx;

                double rho_new = 0.5 * (u[i].density + U_pred[i].density + dt * dU_dt_rho);
                double mom_new = 0.5 * (u[i].momentumX + U_pred[i].momentumX + dt * dU_dt_mom);
                double energy_new = 0.5 * (u[i].energy + U_pred[i].energy + dt * dU_dt_energy);

                u_new[i] = new ConservativeVariables(rho_new, mom_new, 0, energy_new);
            }

            // 更新变量
            System.arraycopy(u_new, 0, u, 0, nx);
            applyBoundaryConditions();

            time += dt;
            iter++;

            if (iter % 100 == 0) {
                LOGGER.info("Time: {} s, Number of iterations: {}\n", "%.4f".formatted(time), iter);
            }
        }

        LOGGER.info("\nCalculation complete!Final time: {} s, All Number of iterations: {}", "%.4f".formatted(time), iter);
    }

    // 输出结果
    public void printResults() {
        LOGGER.info("Final flow field parameters:");
        LOGGER.info("Position\tDensity\tVelocity\tPressure\tMach Numbe");
        for (int i = 0; i < nx; i += nx/10) {
            double x = i * dx;
            LOGGER.info("{}\t{}\t{}\t{}\t{}", "%.3f".formatted(x), "%.4f".formatted(u[i].density), "%.4f".formatted(u[i].velocityX()),
                    "%.4f".formatted(u[i].pressure()), "%.4f".formatted(u[i].machNumber()));

        }
    }
}
