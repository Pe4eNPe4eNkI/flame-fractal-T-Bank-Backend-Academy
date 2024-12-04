package backend.academy.fractal.flame;

import backend.academy.fractal.flame.enums.ImageMod;
import backend.academy.fractal.flame.utils.Pixel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class CreateImage {

    private static final int COLOR_COUNT = 255;
    private static final int RED_SHIFT = 16;
    private static final int GREEN_SHIFT = 8;

    private CreateImage() {
    }

    static void imageFiltration(BufferedImage image, FlameFractal fractal, Pixel[][] canvas) {
        int maxFrequency = getFrequencyMax(fractal, canvas);
        double logMaxFrequency = Math.log10(maxFrequency);

        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int y = 0; y < fractal.height(); y++) {
                final int row = y;
                executor.submit(() -> processRow(row, fractal, canvas, logMaxFrequency, image));
            }
            executor.shutdown();
            //CHECKSTYLE:OFF
            try {
                if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                    System.err.println("Filter did not complete in time!");
                }
            } catch (InterruptedException e) {
                System.err.println("Filter interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //CHECKSTYLE:ON

    }

    static void applyGammaCorrection(FlameFractal fractal, Pixel[][] canvas, BufferedImage image) {
        // Сохраняем максимум частот
        int maxFrequency = getFrequencyMax(fractal, canvas);
        double logMaxFrequency = Math.log10(maxFrequency);

        // Параллелизация обработки строк
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int y = 0; y < fractal.height(); y++) {
            final int row = y;
            executor.submit(() -> processRow(row, fractal, canvas, logMaxFrequency, image));
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.HOURS)) {
                System.err.println("Gamma correction did not complete in time!");
            }
        } catch (InterruptedException e) {
            System.err.println("Gamma correction interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private static void processRow(
        int y,
        FlameFractal fractal,
        Pixel[][] canvas,
        double logMaxFrequency,
        BufferedImage image
    ) {
        final int brightnessGray = 10;
        final double gamma = 2.2;
        int invertedY = fractal.height() - 1 - y;

        for (int x = 0; x < fractal.width(); x++) {
            Pixel pixel = canvas[y][x];

            double frequencyAvg = pixel.counter();
            double alpha = frequencyAvg > 0 ? Math.log10(frequencyAvg) / logMaxFrequency : 0.0;
            double factor = Math.pow(alpha, 1.0 / gamma);

            // Применяем гамма-коррекцию
            if (fractal.imageMod() == ImageMod.BLACK_AND_WHITE) {
                // Черно-белый режим
                int grayValue = (int) Math.min(COLOR_COUNT, COLOR_COUNT * factor);
                grayValue = Math.max(grayValue, brightnessGray); // Минимальная яркость
                int rgb = (grayValue << RED_SHIFT) | (grayValue << GREEN_SHIFT) | grayValue;
                image.setRGB(x, invertedY, rgb);
            } else {
                // Цветной режим
                int red = (int) Math.min(COLOR_COUNT, pixel.r() * factor);
                int green = (int) Math.min(COLOR_COUNT, pixel.g() * factor);
                int blue = (int) Math.min(COLOR_COUNT, pixel.b() * factor);

                int rgb = (red << RED_SHIFT) | (green << GREEN_SHIFT) | blue;
                image.setRGB(x, invertedY, rgb);
            }
        }
    }

    public static void flipImageVertically(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < width; x++) {
                int topPixel = image.getRGB(x, y);
                int bottomPixel = image.getRGB(x, height - y - 1);

                // Swap pixels
                image.setRGB(x, y, bottomPixel);
                image.setRGB(x, height - y - 1, topPixel);
            }
        }
    }

    public static void saveImage(BufferedImage image) {
        String baseFileName = "fractals/fractal";
        String extension = ".png";
        int fileNumber = 1; // Initial file number

        File outputFile = new File(baseFileName + fileNumber + extension);

        while (outputFile.exists()) {
            fileNumber++;
            outputFile = new File(baseFileName + fileNumber + extension);
        }

        try {
            ImageIO.write(image, "PNG", outputFile);
            //CHECKSTYLE:OFF
            System.out.println("Фрактал сохранён как " + outputFile.getName());
            //CHECKSTYLE:OFF

        } catch (IOException e) {
            //CHECKSTYLE:OFF
            e.printStackTrace();
            //CHECKSTYLE:OFF
        }
    }

    public static int getFrequencyMax(FlameFractal fractal, Pixel[][] canvas) {
        int max = 0;
        for (int y = 0; y < fractal.height(); y++) {
            for (int x = 0; x < fractal.width(); x++) {
                max = Math.max(max, canvas[y][x].counter());
            }
        }
        return max;
    }
}
