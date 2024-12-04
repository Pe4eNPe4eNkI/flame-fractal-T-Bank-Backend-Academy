package backend.academy.fractal.flame.enums;

import lombok.Getter;

@Getter
public enum ScreenResolution {
    // Смартфоны
    SMARTPHONE_FHD("Smartphone Full HD", 1920, 1080),
    SMARTPHONE_QHD("Smartphone Quad HD", 2560, 1440),
    SMARTPHONE_4K("Smartphone 4K", 3840, 2160),

    // Мониторы
    MONITOR_HD("Monitor HD", 1366, 768),
    MONITOR_FHD("Monitor Full HD", 1920, 1080),
    MONITOR_QHD("Monitor Quad HD", 2560, 1440),
    MONITOR_QHD_16_10("Monitor Quad HD (16:10)", 2560, 1600),
    MONITOR_MAC("Monitor MacBook", 3456, 2234),
    MONITOR_4K("Monitor 4K", 3840, 2160),
    MONITOR_5K("Monitor 5K", 5120, 2880),
    MONITOR_8K("Monitor 8K", 7680, 4320);

    private final String description;
    private final int width;
    private final int height;

    ScreenResolution(String description, int width, int height) {
        this.description = description;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return description + " (" + width + "x" + height + ")";
    }

    public static ScreenResolution findByDescription(String description) {
        for (ScreenResolution resolution : values()) {
            if (resolution.description.equalsIgnoreCase(description)) {
                return resolution;
            }
        }
        return null;
    }
}

