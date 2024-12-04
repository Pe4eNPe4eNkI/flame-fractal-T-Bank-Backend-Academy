package backend.academy.fractal;

import backend.academy.fractal.flame.enums.ImageMod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageModTest {

    @Test
    void testToString() {
        assertEquals("1 Цветной", ImageMod.COLOR.toString());
        assertEquals("2 Черно-белый", ImageMod.BLACK_AND_WHITE.toString());
    }

    @Test
    void testGetters() {
        assertEquals(1, ImageMod.COLOR.number());
        assertEquals("Цветной", ImageMod.COLOR.description());

        assertEquals(2, ImageMod.BLACK_AND_WHITE.number());
        assertEquals("Черно-белый", ImageMod.BLACK_AND_WHITE.description());
    }
}
