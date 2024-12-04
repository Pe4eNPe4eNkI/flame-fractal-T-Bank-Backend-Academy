package backend.academy.fractal.flame.enums;

import lombok.Getter;

@Getter
public enum ImageMod {
    COLOR(1, "Цветной"),
    BLACK_AND_WHITE(2, "Черно-белый");

    private final int number;
    private final String description;

    ImageMod(int number, String description) {
        this.number = number;
        this.description = description;
    }

    @Override
    public String toString() {
        return number + " " + description;
    }
}
