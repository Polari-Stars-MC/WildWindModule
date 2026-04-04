package org.polaris2023.wildwind.func;

public class ShockRelations {
    // 正激波关系式
    public static double[] normalShockRelations(double M1, double gamma) {
        double M2 = Math.sqrt((1 + (gamma-1)/2 * M1*M1) / (gamma*M1*M1 - (gamma-1)/2));
        double p2_p1 = 1 + (2*gamma/(gamma+1)) * (M1*M1 - 1);
        double rho2_rho1 = ((gamma+1)*M1*M1) / (2 + (gamma-1)*M1*M1);
        double T2_T1 = p2_p1 / rho2_rho1;

        return new double[]{M2, p2_p1, rho2_rho1, T2_T1};
    }

    // 斜激波关系式
    public static double[] obliqueShockRelations(double M1, double theta, double gamma) {
        // 求解激波角β
        double tanTheta = Math.tan(Math.toRadians(theta));
        double M1sq = M1 * M1;

        // 迭代求解
        double beta = Math.toRadians(30);
        for (int i = 0; i < 100; i++) {
            double tanBeta = Math.tan(beta);
            double numerator = 2 * (M1sq * tanBeta * tanBeta - 1) / tanBeta;
            double denominator = M1sq * (gamma + Math.cos(2*beta)) + 2;
            double newTanTheta = numerator / denominator;

            if (Math.abs(Math.tan(beta) - newTanTheta) < 1e-6) {
                break;
            }
            beta = Math.atan(newTanTheta);
        }

        double Mn1 = M1 * Math.sin(beta);
        double[] normalResults = normalShockRelations(Mn1, gamma);
        double Mn2 = normalResults[0];
        double M2 = Mn2 / Math.sin(beta - Math.toRadians(theta));

        return new double[]{M2, Math.toDegrees(beta), normalResults[1], normalResults[2], normalResults[3]};
    }
}
