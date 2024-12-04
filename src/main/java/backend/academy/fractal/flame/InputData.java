package backend.academy.fractal.flame;

import backend.academy.fractal.flame.enums.GenerateMod;
import backend.academy.fractal.flame.enums.ImageMod;
import backend.academy.fractal.flame.enums.ScreenResolution;
import backend.academy.fractal.flame.enums.Transformations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import lombok.Getter;
import lombok.Setter;

public class InputData {
    @Getter
    private int height;
    @Getter
    private int width;
    @Getter
    private int iterationsCount;
    @Getter
    private int sampleCount;
    @Getter
    private List<Transformations> transformations;
    @Getter
    private GenerateMod generateMod;
    @Getter
    private ImageMod imageMod;

    @Setter
    private Scanner inInt = new Scanner(System.in);
    @Setter
    private Scanner in = new Scanner(System.in);

    public InputData() {
        initialization();
    }

    private void initialization() {
        //CHECKSTYLE:OFF
        System.out.println("Выберите способ задания размера изображения:");
        chooseSizeInputMethod();
        System.out.println("Размер изображения: " + height + "x" + width);
        System.out.println();

        System.out.println("Введите желаемое кол-во итераций для генерации пламени:");
        iterationsCount = selectIterationCount();
        System.out.println("Кол-во итераций: " + iterationsCount);
        System.out.println();

        System.out.println("Введите желаемое кол-во сэмплов для генерации пламени:");
        sampleCount = selectSampleCount();
        System.out.println("Кол-во итераций: " + sampleCount);
        System.out.println();

        System.out.println(
            "Укажите желаемые трансформации для генерации пламени (введите номера трансформаций через пробел):");
        transformations = selectTransformations();
        System.out.println("Выбранные трансформации:");

        for (Transformations transformation : transformations) {
            System.out.println(transformation.description());
        }
        System.out.println();

        System.out.println("Хотите просто сгенерировать рисунок или сравнить скорость реализации однопоточной " +
            "и многопоточной генерации пламени?");
        generateMod = selectGenerateMod();
        System.out.println("Выбран режим " + generateMod.toString());
        System.out.println();

        System.out.println("Хотите сгенерировать цветной или черно-белый рисунок?");
        imageMod = selectImageMod();
        System.out.println("Выбран режим " + imageMod.toString());
        System.out.println();

        System.out.println("Фрактальное пламя генерируется!");

        //CHECKSTYLE:ON
    }

    public void chooseSizeInputMethod() {
        //CHECKSTYLE:OFF
        System.out.println("1. Выбрать размер из предложенных вариантов");
        System.out.println("2. Ввести размеры вручную");

        int choice;
        while (true) {
            try {
                System.out.println("Введите номер выбранного варианта:");
                choice = inInt.nextInt();

                if (choice == 1) {
                    selectScreenResolution();
                    break;
                } else if (choice == 2) {
                    this.height = selectHeight();
                    this.width = selectWidth();
                    break;
                } else {
                    System.out.println("Введите корректный номер!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
    }

    public void selectScreenResolution() {
        //CHECKSTYLE:OFF
        System.out.println("Доступные размеры экранов:");
        ScreenResolution[] resolutions = ScreenResolution.values();
        for (int i = 0; i < resolutions.length; i++) {
            System.out.println((i + 1) + ". " + resolutions[i]);
        }

        int choice;
        while (true) {
            try {
                System.out.println("Введите номер выбранного размера:");
                choice = inInt.nextInt();

                if (choice >= 1 && choice <= resolutions.length) {
                    ScreenResolution resolution = resolutions[choice - 1];
                    this.width = resolution.width();
                    this.height = resolution.height();
                    break;
                } else {
                    System.out.println("Введите корректный номер!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
    }

    public int selectHeight() {
        int curHeight;
        //CHECKSTYLE:OFF
        while (true) {
            try {
                int minHeight = 100;
                int maxHeight = 10_000;
                System.out.println("Введите длину не меньше " + minHeight + " и не более " + maxHeight + ":");
                curHeight = inInt.nextInt();

                if (curHeight >= minHeight && curHeight <= maxHeight) {
                    break;
                } else {
                    System.out.println("Введите корректную высоту!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
        return curHeight;
    }

    public int selectWidth() {
        int curWidth;
        //CHECKSTYLE:OFF
        while (true) {
            try {
                int minWidth = 100;
                int maxWidth = 10_000;
                System.out.println("Введите ширину не меньше " + minWidth + " и не более " + maxWidth + ":");
                curWidth = inInt.nextInt();

                if (curWidth >= minWidth && curWidth <= maxWidth) {
                    break;
                } else {
                    System.out.println("Введите корректную ширину!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
        return curWidth;
    }

    public int selectIterationCount() {
        int curIterationCount;
        //CHECKSTYLE:OFF
        while (true) {
            try {
                int minIterationsCount = 10_000;
                int maxIterationsCount = 100_000_000;
                System.out.println(
                    "Введите количество итераций не меньше " + minIterationsCount + " и не более " +
                        maxIterationsCount +
                        ":");
                curIterationCount = inInt.nextInt();

                if (curIterationCount >= minIterationsCount && curIterationCount <= maxIterationsCount) {
                    break;
                } else {
                    System.out.println("Введите корректное кол-во итераций!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
        return curIterationCount;
    }

    public int selectSampleCount() {
        int curSampleCount;
        //CHECKSTYLE:OFF
        while (true) {
            try {
                int minSampleCount = 10_000;
                int maxSampleCount = 100_000_000;
                System.out.println(
                    "Введите количество сэмплов не меньше " + minSampleCount + " и не более " +
                        maxSampleCount +
                        ":");
                curSampleCount = inInt.nextInt();

                if (curSampleCount >= minSampleCount && curSampleCount <= maxSampleCount) {
                    break;
                } else {
                    System.out.println("Введите корректное кол-во сэмплов!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
        }
        //CHECKSTYLE:ON
        return curSampleCount;
    }

    public List<Transformations> selectTransformations() {
        for (int i = 1; i <= Transformations.values().length; i++) {
            //CHECKSTYLE:OFF
            System.out.println(i + " " + Transformations.fromId(i).description());
            //CHECKSTYLE:ON
        }

        String numberLine = in.nextLine();

        List<Integer> transformationIds =
            (numberLine.isEmpty() ? new ArrayList<>(List.of(1)) : Arrays.stream(numberLine.split(" "))
                .map(Integer::parseInt)
                .toList());

        List<Transformations> curTransformations = new ArrayList<>();
        for (int id : transformationIds) {
            Transformations transformation = Transformations.fromId(id);
            if (transformation != null) {
                curTransformations.add(transformation);
            }
        }
        return curTransformations;
    }

    public GenerateMod selectGenerateMod() {
        int curGenerateModNumber;
        for (GenerateMod curGenerateMod : GenerateMod.values()) {
            //CHECKSTYLE:OFF
            System.out.println(curGenerateMod.toString());
            //CHECKSTYLE:ON
        }
        while (true) {
            //CHECKSTYLE:OFF
            try {
                int maxGenerateModNumber = Objects.requireNonNull(Arrays.stream(GenerateMod.values())
                    .max(Comparator.comparingInt(GenerateMod::number))
                    .orElse(null)).number();

                System.out.println("Введите номер нужную реализации:");
                curGenerateModNumber = inInt.nextInt();

                if (curGenerateModNumber <= maxGenerateModNumber && curGenerateModNumber > 0) {
                    break;
                } else {
                    System.out.println("Введите корректное число!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
            //CHECKSTYLE:ON
        }
        return GenerateMod.values()[curGenerateModNumber - 1];
    }

    public ImageMod selectImageMod() {
        int curImageModNumber;
        for (ImageMod curImageMod : ImageMod.values()) {
            //CHECKSTYLE:OFF
            System.out.println(curImageMod.toString());
            //CHECKSTYLE:ON
        }
        while (true) {
            //CHECKSTYLE:OFF
            try {
                int maxImageModNumber = Objects.requireNonNull(Arrays.stream(ImageMod.values())
                    .max(Comparator.comparingInt(ImageMod::number))
                    .orElse(null)).number();

                System.out.println("Введите номер нужную реализации:");
                curImageModNumber = inInt.nextInt();

                if (curImageModNumber <= maxImageModNumber && curImageModNumber > 0) {
                    break;
                } else {
                    System.out.println("Введите корректное число!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите целое число!");
                inInt.next();
            }
            //CHECKSTYLE:ON
        }
        return ImageMod.values()[curImageModNumber - 1];
    }

}
