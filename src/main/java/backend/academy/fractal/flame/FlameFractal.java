package backend.academy.fractal.flame;

import backend.academy.fractal.flame.enums.GenerateMod;
import backend.academy.fractal.flame.enums.ImageMod;
import backend.academy.fractal.flame.enums.Transformations;
import backend.academy.fractal.flame.utils.Border;
import java.util.List;
import lombok.Getter;

@Getter
public class FlameFractal {
    private final int width;
    private final int height;
    private final int samples;
    private final int iterations;
    private final List<Transformations> transformations;
    private final GenerateMod generateMod;
    private final ImageMod imageMod;
    private final Border border;

    public FlameFractal(InputData inputData) {
        this.width = inputData.width();
        this.height = inputData.height();
        this.samples = inputData.sampleCount();
        this.iterations = inputData.iterationsCount();
        this.transformations = inputData.transformations();
        this.generateMod = inputData.generateMod();
        this.imageMod = inputData.imageMod();
        double aspectRatio = (double) width / height;
        this.border = new Border(-aspectRatio, aspectRatio, -1, 1);
    }

}

