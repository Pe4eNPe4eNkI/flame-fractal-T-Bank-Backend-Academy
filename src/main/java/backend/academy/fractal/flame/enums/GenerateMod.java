package backend.academy.fractal.flame.enums;

import lombok.Getter;

@Getter
public enum GenerateMod implements Comparable<GenerateMod> {
    MULTI_THREADED(1, "Многопоточная генерация"),
    SINGLE_THREADED(2, "Однопоточная генерация"),
    SPEED_TEST(3, "Сравнение однопоточной и многопоточной реализации");

    private final int number;
    private final String description;

    GenerateMod(int number, String description) {
        this.number = number;
        this.description = description;
    }

    @Override
    public String toString() {
        return number + " " + description;
    }

}
