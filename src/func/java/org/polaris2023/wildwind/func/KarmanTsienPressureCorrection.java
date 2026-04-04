package org.polaris2023.wildwind.func;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author baka4n
 * 卡门-钱学森压力系数修正公式
 */
public class KarmanTsienPressureCorrection {

    public static final Logger LOGGER = LoggerFactory.getLogger(KarmanTsienPressureCorrection.class);

    /**
     * 计算卡门-钱学森修正后的压力系数
     *
     * @param cpIncompressible 不可压缩流压力系数
     * @param machFreestream 来流马赫数
     * @return 修正后的可压缩流压力系数
     */
    public static double calculateCorrectedCp(double cpIncompressible, double machFreestream) {
        if (machFreestream < 0) throw new IllegalArgumentException("The Mach number cannot be negative");
        if (machFreestream >= 1.0) {
            LOGGER.warn("Warning: When the Mach number is close to or exceeds 1.0, the accuracy of the formula decreases in the transonic region.");
        }
        double beta = Math.sqrt(1 - machFreestream * machFreestream);
        double denominator = beta + (machFreestream * machFreestream / (1 + beta)) * (cpIncompressible / 2);
        return cpIncompressible / denominator;
    }

    /**
     * 批量计算压力系数分布
     *
     * @param cpIncompressibleArray 不可压缩流压力系数数组
     * @param machFreestream 来流马赫数
     * @return 修正后的压力系数数组
     */
    public static double[] calculateCorrectedCpArray(double[] cpIncompressibleArray, double machFreestream) {
        if (cpIncompressibleArray == null) {
            throw new IllegalArgumentException("Array is null");
        }

        double[] correctedArray = new double[cpIncompressibleArray.length];
        for (int i = 0; i < cpIncompressibleArray.length; i++) {
            correctedArray[i] = calculateCorrectedCp(cpIncompressibleArray[i], machFreestream);
        }
        return correctedArray;
    }

    /**
     * 计算普朗特-格劳尔特修正
     *
     * @param cpIncompressible 不可压缩流压力系数
     * @param machFreestream 来流马赫数
     * @return 普朗特-格劳尔特修正后的压力系数
     */
    public static double calculatePrandtlGlauertCp(double cpIncompressible, double machFreestream) {
        double beta = Math.sqrt(1 - machFreestream * machFreestream);
        return cpIncompressible / beta;
    }

    /**
     * 计算戈瑟特修正
     *
     * @param cpIncompressible 不可压缩流压力系数
     * @param machFreestream 来流马赫数
     * @return 戈瑟特修正后的压力系数
     */
    public static double calculateGoethertCp(double cpIncompressible, double machFreestream) {
        double beta = Math.sqrt(1 - machFreestream * machFreestream);
        double cpLocal = cpIncompressible / beta;

        // 迭代计算更精确的值
        for (int i = 0; i < 5; i++) {
            cpLocal = cpIncompressible / (beta + (machFreestream * machFreestream / (2 * (1 + beta))) * cpLocal);
        }

        return cpLocal;
    }

    /**
     * 计算临界压力系数
     *
     * @param machFreestream 来流马赫数
     * @param gamma 比热比（空气通常为1.4）
     * @return 临界压力系数
     */
    public static double calculateCriticalPressureCoefficient(double machFreestream, double gamma) {
        // 计算临界马赫数对应的压力系数
        double criticalPressureRatio = Math.pow((2 / (gamma + 1)) *
                        (1 + (gamma - 1) / 2 * machFreestream * machFreestream),
                gamma / (gamma - 1));

        return (criticalPressureRatio - 1) / (0.5 * gamma * machFreestream * machFreestream);
    }
}
