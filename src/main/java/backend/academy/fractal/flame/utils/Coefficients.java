package backend.academy.fractal.flame.utils;

import java.util.SplittableRandom;

public record Coefficients(Coefficient coefficient,
                           int red,
                           int green,
                           int blue) {

    public static Coefficients[] createCoeffs() {
        final int affinTransformationCount = 4;
        final double firstCoeff = -1.5;
        final double secondCoeff = 3;
        final int bound = 256;

        Coefficients[] coeffs = new Coefficients[affinTransformationCount];
        SplittableRandom random = new SplittableRandom(); // Faster random

        for (int i = 0; i < affinTransformationCount; i++) {
            boolean valid = false;
            Coefficients coeff = null;

            while (!valid) {
                double a = firstCoeff + random.nextDouble() * secondCoeff;
                double b = firstCoeff + random.nextDouble() * secondCoeff;
                double c = firstCoeff + random.nextDouble() * secondCoeff;
                double d = firstCoeff + random.nextDouble() * secondCoeff;
                double e = firstCoeff + random.nextDouble() * secondCoeff;
                double f = firstCoeff + random.nextDouble() * secondCoeff;
                int rColor = random.nextInt(bound);
                int gColor = random.nextInt(bound);
                int bColor = random.nextInt(bound);

                if (isValid(a, b, d, e)) {
                    Coefficient coefficient = new Coefficient(a, b, c, d, e, f);
                    coeff = new Coefficients(coefficient, rColor, gColor, bColor);
                    valid = true;
                }
            }
            coeffs[i] = coeff;
        }
        return coeffs;
    }

    private static boolean isValid(double a, double b, double d, double e) {
        return (a * a + d * d < 1)
            && (b * b + e * e < 1)
            && (a * a + d * d + b * b + e * e < 1 + (a * e - b * d) * (a * e - b * d));
    }
}
