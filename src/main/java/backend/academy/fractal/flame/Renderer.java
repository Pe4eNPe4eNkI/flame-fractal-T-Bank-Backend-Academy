package backend.academy.fractal.flame;

import backend.academy.fractal.flame.enums.ImageMod;
import backend.academy.fractal.flame.enums.Transformations;
import backend.academy.fractal.flame.utils.Coefficients;
import backend.academy.fractal.flame.utils.Pixel;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.SplittableRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.Getter;

public class Renderer {
    private final FlameFractal fractal;
    private final BufferedImage image;
    private Pixel[][] canvas;
    private final SplittableRandom random = new SplittableRandom(); // Faster random
    private static final int COLOR_COUNT = 255;
    private static final double RED_COEF = 0.299;
    private static final double GREEN_COEF = 0.587;
    private static final double BLUE_COEF = 0.114;

    @Getter
    private long durationMulti;
    @Getter
    private long durationSingle;

    public Renderer(FlameFractal fractal) {
        this.fractal = fractal;
        this.image = new BufferedImage(fractal.width(), fractal.height(), BufferedImage.TYPE_INT_RGB);
    }

    private Pixel[][] createCanvas() {
        Pixel[][] canvasLocal = new Pixel[fractal.height()][fractal.width()];
        for (int x = 0; x < fractal.height(); x++) {
            for (int y = 0; y < fractal.width(); y++) {
                canvasLocal[x][y] = new Pixel(0, 0, 0, 0, 0);
            }
        }
        return canvasLocal;
    }

    public void multiRender() {
        Coefficients[] coeffs = Coefficients.createCoeffs();
        canvas = createCanvas();
        long startTime = System.nanoTime();
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);
        int samplesPerThread = fractal.samples() / processors;

        for (int i = 0; i < processors; i++) {
            SplittableRandom threadRandom = random.split(); // Thread-local random
            executor.submit(() -> {
                for (int j = 0; j < samplesPerThread; j++) {
                    renderSample(coeffs, threadRandom);
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Ждём завершения всех потоков
        }

        long endTime = System.nanoTime();
        durationMulti = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
        //CHECKSTYLE:OFF
        System.out.println("Многопоточная реализация завершена за " + durationMulti + " мс");
        //CHECKSTYLE:ON

        CreateImage.imageFiltration(image, fractal, canvas);
        CreateImage.applyGammaCorrection(fractal, canvas, image);
        CreateImage.flipImageVertically(image);
        CreateImage.saveImage(image);
    }

    public void singleRender() {
        Coefficients[] coeffs = Coefficients.createCoeffs();
        canvas = createCanvas();
        SplittableRandom threadRandom = random.split(); // Thread-local random
        long startTime = System.nanoTime();

        // Однопоточная обработка всех сэмплов
        for (int i = 0; i < fractal.samples(); i++) {
            renderSample(coeffs, threadRandom);
        }

        long endTime = System.nanoTime();
        durationSingle = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
        //CHECKSTYLE:OFF
        System.out.println("Однопоточная реализация завершена за " + durationSingle + " мс");
        //CHECKSTYLE:ON

        CreateImage.imageFiltration(image, fractal, canvas);
        CreateImage.applyGammaCorrection(fractal, canvas, image);
        CreateImage.flipImageVertically(image);
        CreateImage.saveImage(image);
    }

    private void renderSample(Coefficients[] coeffs, SplittableRandom random) {
        double xMin = fractal.border().xMin();
        double xMax = fractal.border().xMax();
        double yMin = fractal.border().yMin();
        double yMax = fractal.border().yMax();

        // Начальная точка ближе к центру
        double newX = random.nextDouble() * (xMax - xMin) + xMin;
        double newY = random.nextDouble() * (yMax - yMin) + yMin;
        final int startIteration = -20;

        for (int step = startIteration; step < fractal.iterations(); step++) {
            int coeffIndex = random.nextInt(coeffs.length);
            Coefficients coeff = coeffs[coeffIndex];

            // Аффинные преобразования
            double[] affin = affinTransformation(newX, newY, coeff);
            double affinX = affin[0];
            double affinY = affin[1];

            // Применяем трансформацию
            List<Transformations> transformations = fractal.transformations();
            Transformations transformation = transformations.get(random.nextInt((transformations.size())));
            double[] transformed = transformation.apply(affinX, affinY, coeff);
            double curX = transformed[0];
            double curY = transformed[1];

            // Проверка попадания в область экрана
            if (step > 0 && curX >= xMin && curX <= xMax && curY >= yMin && curY <= yMax) {
                // Нормализуем координаты
                double normalizedX = (curX - xMin) / (xMax - xMin);
                double normalizedY = (curY - yMin) / (yMax - yMin);

                int pixelX = (int) Math.floor((normalizedX * fractal.width()));
                int pixelY = (int) Math.floor((normalizedY * fractal.height()));

                // Проверка границ пикселя
                if (pixelX >= 0 && pixelX < fractal.width() && pixelY >= 0 && pixelY < fractal.height()) {
                    Pixel pixel = canvas[pixelY][pixelX];

                    if (pixel.counter() == 0) {
                        // Устанавливаем стартовый цвет
                        startColor(coeffs, pixelX, pixelY, coeffIndex);
                    } else {
                        updateColor(coeff, pixelX, pixelY, pixel);
                    }
                }
            }
            newX = curX * 2;
            newY = curY * 2;
        }
    }

    private void startColor(Coefficients[] coeffs, int pixelX, int pixelY, int coeffIndex) {
        if (fractal.imageMod() == ImageMod.COLOR) {
            canvas[pixelY][pixelX] = new Pixel(
                coeffs[coeffIndex].red(),
                coeffs[coeffIndex].green(),
                coeffs[coeffIndex].blue(),
                1,
                Math.log10(1)
            );
        } else {
            // Реализация черно-белого режима
            int grayscale = (int) Math.min(COLOR_COUNT,
                RED_COEF * coeffs[coeffIndex].red()
                    + GREEN_COEF * coeffs[coeffIndex].green()
                    + BLUE_COEF * coeffs[coeffIndex].blue());
            canvas[pixelY][pixelX] = new Pixel(
                grayscale,
                grayscale,
                grayscale,
                1,
                Math.log10(1)
            );
        }
    }

    private void updateColor(Coefficients coeff, int pixelX, int pixelY, Pixel pixel) {
        int newHitCount = pixel.counter() + 1;
        if (fractal.imageMod() == ImageMod.COLOR) {
            // Обновляем цвет в режиме цвета
            canvas[pixelY][pixelX] = new Pixel(
                (pixel.r() * pixel.counter() + coeff.red()) / (pixel.counter() + 1),
                (pixel.g() * pixel.counter() + coeff.green()) / (pixel.counter() + 1),
                (pixel.b() * pixel.counter() + coeff.blue()) / (pixel.counter() + 1),
                newHitCount,
                Math.log10(newHitCount)
            );
        } else {
            // Обновляем цвет в черно-белом режиме
            int grayValue = Math.min(COLOR_COUNT,
                (pixel.r() * pixel.counter() + coeff.red()) / (pixel.counter() + 1));
            canvas[pixelY][pixelX] = new Pixel(
                grayValue,
                grayValue,
                grayValue,
                newHitCount,
                Math.log10(newHitCount)
            );
        }
    }

    private double[] affinTransformation(double x, double y, Coefficients coeff) {
        double newX = coeff.coefficient().a() * x + coeff.coefficient().b() * y + coeff.coefficient().c();
        double newY = coeff.coefficient().e() * x + coeff.coefficient().d() * y + coeff.coefficient().f();
        return new double[] {newX, newY};
    }

}
