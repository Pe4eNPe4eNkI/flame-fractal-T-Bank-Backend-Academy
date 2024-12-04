package backend.academy;

import backend.academy.fractal.flame.FlameFractal;
import backend.academy.fractal.flame.InputData;
import backend.academy.fractal.flame.Renderer;
import backend.academy.fractal.flame.enums.GenerateMod;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        InputData inputData = new InputData();
        FlameFractal fractal = new FlameFractal(inputData);
        Renderer renderer = new Renderer(fractal);
        if (inputData.generateMod() == GenerateMod.MULTI_THREADED) {
            renderer.multiRender();
        } else if (inputData.generateMod() == GenerateMod.SINGLE_THREADED) {
            renderer.singleRender();
        } else {
            // speedtest
            renderer.multiRender();
            renderer.singleRender();
        }
    }
}

