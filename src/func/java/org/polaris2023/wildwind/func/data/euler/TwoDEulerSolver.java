package org.polaris2023.wildwind.func.data.euler;

import org.polaris2023.wildwind.func.data.ConservativeVariables;
import org.polaris2023.wildwind.func.data.Flux;
import org.polaris2023.wildwind.func.data.flux.FluxX;
import org.polaris2023.wildwind.func.data.flux.FluxY;

import static org.polaris2023.wildwind.func.data.euler.EulerEquationSolver.GAMMA;

public class TwoDEulerSolver {
    private int nx, ny;
    private double dx, dy;
    private double dt;
    private double cfl;
    private ConservativeVariables[][] u;
    private ConservativeVariables[][] u_new;

    public TwoDEulerSolver(int nx, int ny, double lengthX, double lengthY, double cfl) {
        this.nx = nx;
        this.ny = ny;
        this.dx = lengthX / (nx - 1);
        this.dy = lengthY / (ny - 1);
        this.cfl = cfl;
        this.u = new ConservativeVariables[nx][ny];
        this.u_new = new ConservativeVariables[nx][ny];
    }

    // 初始化均匀流场
    public void initializeUniformFlow(double rho, double _u, double v, double p) {
        double E = p / ((GAMMA - 1) * rho) + 0.5 * (_u*_u + v*v);
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                u[i][j] = new ConservativeVariables(rho, rho*_u, rho*v, rho*E);
            }
        }
    }

    // 初始化圆柱绕流
    public void initializeCylinderFlow(double rhoInf, double uInf, double pInf, double cylinderX, double cylinderY, double radius) {
        double E_inf = pInf / ((GAMMA - 1) * rhoInf) + 0.5 * uInf * uInf;

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                double x = i * dx;
                double y = j * dy;

                // 计算到圆柱中心的距离
                double dist = Math.sqrt((x - cylinderX)*(x - cylinderX) + (y - cylinderY)*(y - cylinderY));

                if (dist < radius) {
                    // 圆柱内部（固体边界）
                    u[i][j] = new ConservativeVariables(rhoInf, 0, 0, rhoInf * E_inf);
                } else {
                    // 外部流场
                    u[i][j] = new ConservativeVariables(rhoInf, rhoInf*uInf, 0, rhoInf*E_inf);
                }
            }
        }
    }

    // 计算时间步长
    private double computeTimeStep() {
        double maxSpeed = 0;
        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                double _u = u[i][j].velocityX();
                double v = u[i][j].velocityY();
                double c = u[i][j].speedOfSound();
                double speed = Math.abs(_u) + Math.abs(v) + c;
                maxSpeed = Math.max(maxSpeed, speed);
            }
        }
        double dt_x = cfl * dx / maxSpeed;
        double dt_y = cfl * dy / maxSpeed;
        return Math.min(dt_x, dt_y);
    }

    // 简单的时间推进（使用Lax-Friedrichs方法）
    public void solve(double finalTime) {
        double time = 0;
        int iter = 0;

        System.out.println("\n二维欧拉方程求解");
        System.out.println("=================");
        System.out.printf("网格: %d x %d, CFL: %.2f\n", nx, ny, cfl);

        while (time < finalTime) {
            dt = computeTimeStep();
            if (time + dt > finalTime) {
                dt = finalTime - time;
            }

            // 计算新时间步的值
            for (int i = 1; i < nx-1; i++) {
                for (int j = 1; j < ny-1; j++) {
                    // x方向通量
                    Flux F_im1 = new FluxX(u[i-1][j]);
                    Flux F_ip1 = new FluxX(u[i+1][j]);

                    // y方向通量
                    Flux G_jm1 = new FluxY(u[i][j-1]);
                    Flux G_jp1 = new FluxY(u[i][j+1]);

                    // Lax-Friedrichs格式
                    double rho_new = 0.25 * (u[i-1][j].density + u[i+1][j].density +
                            u[i][j-1].density + u[i][j+1].density) -
                            dt/(2*dx) * (F_ip1.massFlux - F_im1.massFlux) -
                            dt/(2*dy) * (G_jp1.massFlux - G_jm1.massFlux);

                    double momX_new = 0.25 * (u[i-1][j].momentumX + u[i+1][j].momentumX +
                            u[i][j-1].momentumX + u[i][j+1].momentumX) -
                            dt/(2*dx) * (F_ip1.momentumFluxX - F_im1.momentumFluxX) -
                            dt/(2*dy) * (G_jp1.momentumFluxX - G_jm1.momentumFluxX);

                    double momY_new = 0.25 * (u[i-1][j].momentumY + u[i+1][j].momentumY +
                            u[i][j-1].momentumY + u[i][j+1].momentumY) -
                            dt/(2*dx) * (F_ip1.momentumFluxY - F_im1.momentumFluxY) -
                            dt/(2*dy) * (G_jp1.momentumFluxY - G_jm1.momentumFluxY);

                    double energy_new = 0.25 * (u[i-1][j].energy + u[i+1][j].energy +
                            u[i][j-1].energy + u[i][j+1].energy) -
                            dt/(2*dx) * (F_ip1.energyFlux - F_im1.energyFlux) -
                            dt/(2*dy) * (G_jp1.energyFlux - G_jm1.energyFlux);

                    u_new[i][j] = new ConservativeVariables(rho_new, momX_new, momY_new, energy_new);
                }
            }

            // 更新流场
            for (int i = 1; i < nx-1; i++) {
                System.arraycopy(u_new[i], 1, u[i], 1, ny-2);
            }

            // 应用边界条件
            applyBoundaryConditions();

            time += dt;
            iter++;

            if (iter % 100 == 0) {
                System.out.printf("时间: %.4f s, 迭代步数: %d\n", time, iter);
            }
        }

        System.out.printf("\n计算完成！最终时间: %.4f s, 总迭代步数: %d\n", time, iter);
    }

    // 边界条件
    private void applyBoundaryConditions() {
        // 简单边界：外推
        for (int j = 0; j < ny; j++) {
            u[0][j] = u[1][j];           // 左边界
            u[nx-1][j] = u[nx-2][j];     // 右边界
        }
        for (int i = 0; i < nx; i++) {
            u[i][0] = u[i][1];           // 下边界
            u[i][ny-1] = u[i][ny-2];     // 上边界
        }
    }

    // 计算流场统计信息
    public void printStatistics() {
        double avgDensity = 0, avgPressure = 0, avgMach = 0;
        int count = 0;

        for (int i = 0; i < nx; i++) {
            for (int j = 0; j < ny; j++) {
                avgDensity += u[i][j].density;
                avgPressure += u[i][j].pressure();
                avgMach += u[i][j].machNumber();
                count++;
            }
        }

        avgDensity /= count;
        avgPressure /= count;
        avgMach /= count;

        System.out.println("\n流场统计:");
        System.out.printf("平均密度: %.4f kg/m³\n", avgDensity);
        System.out.printf("平均压力: %.4f Pa\n", avgPressure);
        System.out.printf("平均马赫数: %.4f\n", avgMach);
    }
}
