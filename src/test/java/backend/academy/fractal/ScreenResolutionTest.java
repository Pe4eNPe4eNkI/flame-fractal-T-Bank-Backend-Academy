package backend.academy.fractal;

import backend.academy.fractal.flame.enums.ScreenResolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ScreenResolutionTest {

    @Test
    void testFindByDescription_ValidDescription() {
        ScreenResolution resolution = ScreenResolution.findByDescription("Smartphone Full HD");

        Assertions.assertNotNull(resolution, "Решение экрана не должно быть пустым.");
        Assertions.assertEquals(1920, resolution.width(), "Ширина должна быть 1920.");
        Assertions.assertEquals(1080, resolution.height(), "Высота должна быть 1080.");
    }

    @Test
    void testFindByDescription_InvalidDescription() {
        ScreenResolution resolution = ScreenResolution.findByDescription("Invalid Resolution");

        Assertions.assertNull(resolution, "Решение экрана должно быть пустым.");
    }

    @Test
    void testToString() {
        ScreenResolution resolution = ScreenResolution.MONITOR_FHD;

        String expectedString = "Monitor Full HD (1920x1080)";
        Assertions.assertEquals(expectedString, resolution.toString(),
            "Метод toString должен возвращать корректный формат.");
    }

    @Test
    void testAllEnumValues() {
        for (ScreenResolution resolution : ScreenResolution.values()) {
            Assertions.assertNotNull(resolution.description(), "Описание не должно быть пустым.");
            Assertions.assertTrue(resolution.width() > 0, "Ширина должна быть положительным числом.");
            Assertions.assertTrue(resolution.height() > 0, "Высота должна быть положительным числом.");
        }
    }

    @Test
    void testFindByDescription_CaseInsensitive() {
        ScreenResolution resolution = ScreenResolution.findByDescription("monitor 4k");

        Assertions.assertNotNull(resolution, "Решение экрана не должно быть пустым.");
        Assertions.assertEquals(3840, resolution.width(), "Ширина должна быть 3840.");
        Assertions.assertEquals(2160, resolution.height(), "Высота должна быть 2160.");
    }
}
