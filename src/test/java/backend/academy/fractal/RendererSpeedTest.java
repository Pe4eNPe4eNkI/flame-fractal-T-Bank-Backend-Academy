package backend.academy.fractal;

import backend.academy.fractal.flame.FlameFractal;
import backend.academy.fractal.flame.InputData;
import backend.academy.fractal.flame.Renderer;
import backend.academy.fractal.flame.enums.GenerateMod;
import backend.academy.fractal.flame.enums.ImageMod;
import backend.academy.fractal.flame.enums.Transformations;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RendererSpeedTest {

    private Renderer renderer;

    @BeforeEach
    void setUp() {
        InputData inputData = mock(InputData.class);

        when(inputData.height()).thenReturn(1600);
        when(inputData.width()).thenReturn(2560);
        when(inputData.iterationsCount()).thenReturn(10_000);
        when(inputData.sampleCount()).thenReturn(10_000);
        when(inputData.transformations()).thenReturn(List.of(Transformations.BUBBLE, Transformations.JULIA));
        when(inputData.generateMod()).thenReturn(GenerateMod.SPEED_TEST);
        when(inputData.imageMod()).thenReturn(ImageMod.COLOR);

        FlameFractal fractal = new FlameFractal(inputData);
        renderer = new Renderer(fractal);
    }

    @Test
    void isMultithreadedFasterThanSingleThreaded() {
        renderer.multiRender();
        long durationMulti = renderer.durationMulti();

        renderer.singleRender();
        long durationSingle = renderer.durationSingle();

        System.out.println("Время выполнения многопоточной реализации: " + durationMulti + " мс");
        System.out.println("Время выполнения однопоточной реализации: " + durationSingle + " мс");

        assertTrue(durationMulti < durationSingle,
            "Многопоточная реализация должна быть быстрее однопоточной!");
    }
}
