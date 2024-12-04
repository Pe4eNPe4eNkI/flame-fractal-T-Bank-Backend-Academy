package backend.academy.fractal;

import backend.academy.fractal.flame.enums.Transformations;
import backend.academy.fractal.flame.utils.Coefficient;
import backend.academy.fractal.flame.utils.Coefficients;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransformationTest {

    private static final Coefficients TEST_COEFFS = new Coefficients(
        new Coefficient(1, 1, 1, 1, 1, 1),
        255,
        255,
        255
    );

    @Test
    void testFromId() {
        assertEquals(Transformations.DISK, Transformations.fromId(1));
        assertEquals(Transformations.JULIA, Transformations.fromId(7));
        assertNull(Transformations.fromId(100)); // Несуществующий ID
    }

    @Test
    void testDescriptions() {
        assertEquals("Дисковая трансформация", Transformations.DISK.description());
        assertEquals("Синусоидальная трансформация", Transformations.SINUSOIDAL.description());
    }

    @Test
    void testApplyTransformations() {
        // Проверяем корректность формул для разных трансформаций
        double[] diskResult = Transformations.DISK.apply(1, 0, TEST_COEFFS);
        assertEquals(0, diskResult[0], 1e-5);
        assertEquals(0, diskResult[1], 1e-5);

        double[] sinusoidalResult = Transformations.SINUSOIDAL.apply(1, 1, TEST_COEFFS);
        assertEquals(Math.sin(1) * 2, sinusoidalResult[0], 1e-5);
        assertEquals(Math.sin(1) * 2, sinusoidalResult[1], 1e-5);

        double[] heartResult = Transformations.HEART.apply(1, 0, TEST_COEFFS);
        assertEquals(0, heartResult[0], 1e-5);
        assertEquals(-1, heartResult[1], 1e-5);
    }
}

