package backend.academy.fractal;

import backend.academy.fractal.flame.enums.GenerateMod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GenerationModTest {

    @Test
    void testToString() {
        assertEquals("1 Многопоточная генерация", GenerateMod.MULTI_THREADED.toString());
        assertEquals("2 Однопоточная генерация", GenerateMod.SINGLE_THREADED.toString());
        assertEquals("3 Сравнение однопоточной и многопоточной реализации", GenerateMod.SPEED_TEST.toString());
    }

    @Test
    void testGetters() {
        assertEquals(1, GenerateMod.MULTI_THREADED.number());
        assertEquals("Многопоточная генерация", GenerateMod.MULTI_THREADED.description());

        assertEquals(2, GenerateMod.SINGLE_THREADED.number());
        assertEquals("Однопоточная генерация", GenerateMod.SINGLE_THREADED.description());
    }
}

