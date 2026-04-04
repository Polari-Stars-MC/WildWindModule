package org.polaris2023.wildwind.func;

import org.polaris2023.wildwind.func.data.FlowRegime;
import org.polaris2023.wildwind.func.data.KnudsenAnalysis;

/**
 * 克努森数计算工具
 * 克努森数是分子平均自由程与特征长度的比值，用于判断流动的连续性
 */
public class KnudsenNumberCalculator {
    /**
     * 克努森数定义：Kn = λ / L
     * 其中 λ 是分子平均自由程，L 是特征长度
     *
     * @param meanFreePath 分子平均自由程 (m)
     * @param characteristicLength 特征长度 (m)
     * @return 克努森数
     */
    public static double calculateKnudsenNumber(double meanFreePath, double characteristicLength) {
        if (characteristicLength <= 0) {
            throw new IllegalArgumentException("The characteristic length must be greater than 0");
        }
        if (meanFreePath < 0) {
            throw new IllegalArgumentException("The mean free path cannot be negative");
        }

        return meanFreePath / characteristicLength;
    }

    /**
     * 根据气体参数计算分子平均自由程
     * 公式：λ = kT / (√2 * π * d² * p)
     *
     * @param temperature 温度 (K)
     * @param pressure 压力 (Pa)
     * @param molecularDiameter 分子直径 (m)
     * @param boltzmannConstant 玻尔兹曼常数 (J/K)，默认值 1.380649e-23
     * @return 分子平均自由程 (m)
     */
    public static double calculateMeanFreePath(double temperature, double pressure,
                                               double molecularDiameter, double boltzmannConstant) {
        if (temperature <= 0) {
            throw new IllegalArgumentException("The temperature must be greater than 0");
        }
        if (pressure <= 0) {
            throw new IllegalArgumentException("Pressure must be greater than 0");
        }
        if (molecularDiameter <= 0) {
            throw new IllegalArgumentException("The molecular diameter must be greater than 0");
        }

        // λ = kT / (√2 * π * d² * p)
        double denominator = Math.sqrt(2) * Math.PI * molecularDiameter * molecularDiameter * pressure;
        return boltzmannConstant * temperature / denominator;
    }

    /**
     * 使用标准空气参数计算平均自由程
     * 空气分子直径约 3.7e-10 m
     *
     * @param temperature 温度 (K)
     * @param pressure 压力 (Pa)
     * @return 分子平均自由程 (m)
     */
    public static double calculateMeanFreePathAir(double temperature, double pressure) {
        double molecularDiameterAir = 3.7e-10;  // 空气分子直径 (m)
        double boltzmannConstant = 1.380649e-23; // 玻尔兹曼常数 (J/K)

        return calculateMeanFreePath(temperature, pressure, molecularDiameterAir, boltzmannConstant);
    }

    /**
     * 计算基于马赫数和雷诺数的克努森数
     * 公式：Kn = Ma / Re * √(πγ/2)
     *
     * @param machNumber 马赫数
     * @param reynoldsNumber 雷诺数
     * @param gamma 比热比（空气为1.4）
     * @return 克努森数
     */
    public static double calculateKnFromMachAndRe(double machNumber, double reynoldsNumber, double gamma) {
        if (reynoldsNumber <= 0) {
            throw new IllegalArgumentException("雷诺数必须大于0");
        }

        return machNumber / reynoldsNumber * Math.sqrt(Math.PI * gamma / 2);
    }

    /**
     * 计算基于高度的大气克努森数
     *
     * @param altitude 海拔高度 (m)
     * @return 克努森数（基于1m特征长度）
     */
    public static double calculateKnAtAltitude(double altitude) {
        // 简化的大气模型
        double pressure, temperature;

        if (altitude < 11000) {
            // 对流层
            temperature = 288.15 - 0.0065 * altitude;
            pressure = 101325 * Math.pow((288.15 / temperature), -5.25577);
        } else if (altitude < 20000) {
            // 对流层顶到平流层
            temperature = 216.65;
            pressure = 22632 * Math.exp(-(altitude - 11000) / 6341.62);
        } else {
            // 平流层以上
            temperature = 216.65 + 0.001 * (altitude - 20000);
            pressure = 5474.89 * Math.pow((216.65 / temperature), 34.163);
        }

        double meanFreePath = calculateMeanFreePathAir(temperature, pressure);
        double characteristicLength = 1.0; // 特征长度取1m

        return calculateKnudsenNumber(meanFreePath, characteristicLength);
    }

    /**
     * 计算并提供详细的克努森数分析
     */
    public static KnudsenAnalysis analyzeKnudsenNumber(double meanFreePath, double characteristicLength) {
        double kn = calculateKnudsenNumber(meanFreePath, characteristicLength);
        FlowRegime regime = FlowRegime.fromKnudsenNumber(kn);

        return new KnudsenAnalysis(kn, meanFreePath, characteristicLength, regime);
    }
}
