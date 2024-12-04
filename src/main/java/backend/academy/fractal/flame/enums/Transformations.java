package backend.academy.fractal.flame.enums;

import backend.academy.fractal.flame.utils.Coefficients;
import lombok.Getter;

@Getter
public enum Transformations {
    DISK("Дисковая трансформация", 1) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = (theta / Math.PI) * Math.sin(Math.PI * r);
            double newY = (theta / Math.PI) * Math.cos(Math.PI * r);
            return new double[] {newX, newY};
        }
    },

    SINUSOIDAL("Синусоидальная трансформация", 2) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double newX = Math.sin(x);
            double newY = Math.sin(y);
            final double scale = 2;
            newX *= scale;
            newY *= scale;
            return new double[] {newX, newY};
        }
    },

    HEART("Трансформация сердце", 3) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            double newX = r * Math.sin(theta * r);
            double newY = -r * Math.cos(theta * r);
            return new double[] {newX, newY};
        }
    },

    SPHERICAL("Сферическая трансформация", 4) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double denom = x * x + y * y;
            final double num = 1e-5;
            denom = denom == 0 ? num : denom;
            double newX = x / denom;
            double newY = y / denom;
            return new double[] {newX, newY};
        }
    },

    WAVE("Волновая трансформация", 5) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            final double num = 1e-2;
            double newX =
                x + coeff.coefficient().b() * Math.sin(y / (coeff.coefficient().c() * coeff.coefficient().c() + num));
            double newY =
                y + coeff.coefficient().d() * Math.sin(x / (coeff.coefficient().e() * coeff.coefficient().e() + num));
            return new double[] {newX, newY};
        }
    },

    BUBBLE("Трансформация пузырьком", 6) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double rSquared = x * x + y * y;
            final double num = 4.0;
            double scale = num / (rSquared + num);
            double newX = scale * x;
            double newY = scale * y;
            return new double[] {newX, newY};
        }
    },

    JULIA("Трансформация Джулия", 7) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double r = Math.sqrt(x * x + y * y);
            final double num = 2.0;
            double theta = Math.atan2(y, x) / num + (Math.random() < (1 / num) ? 0 : Math.PI);
            double newX = r * Math.cos(theta);
            double newY = r * Math.sin(theta);
            return new double[] {newX, newY};
        }
    },

    FISH_EYE("Трансформация рыбий глаз", 8) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double r = Math.sqrt(x * x + y * y);
            final double num = 2.0;
            double scale = num / (r + 1);
            double newX = scale * x;
            double newY = scale * y;
            return new double[] {newX, newY};
        }
    },

    CYLINDER("Цилиндрическая трансформация", 9) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double newX = Math.sin(x);
            return new double[] {newX, y};
        }
    },

    MOBIUS("Трансформация Мёбиуса", 10) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double denom = coeff.coefficient().a() * x + coeff.coefficient().b() * y + coeff.coefficient().c();
            final double num = 1e-6;
            denom = denom == 0 ? num : denom;

            double newX = (coeff.coefficient().d() * x + coeff.coefficient().e() * y + coeff.coefficient().f()) / denom;
            double newY = (coeff.coefficient().a() * y - coeff.coefficient().b() * x) / denom;
            return new double[] {newX, newY};
        }
    },
    SPIRAL("Спиральная трансформация", 11) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double r = Math.sqrt(x * x + y * y);
            double theta = Math.atan2(y, x);
            final double num = 0.1;

            // Рисуем спираль с увеличением радиуса
            double nx = r * Math.cos(theta + num * r);
            double ny = r * Math.sin(theta + num * r);

            return new double[] {nx, ny};
        }
    },
    GAUSSIAN("Трансформация Гаусса", 12) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double a = -x * x - y * y;
            double newX = Math.exp(a) * x;  // Гауссово искажение
            double newY = Math.exp(a) * y;  // Гауссово искажение
            return new double[] {newX, newY};
        }
    },
    SINE_WAVE("Синусоидальные волны", 13) {
        @Override
        public double[] apply(double x, double y, Coefficients coeff) {
            double newX = Math.sin(x) * Math.cos(y);  // Синусоидальное искривление
            double newY = Math.cos(x) * Math.sin(y);  // Синусоидальное искривление
            return new double[] {newX, newY};
        }
    };

    private final String description;
    private final int id;

    Transformations(String description, int id) {
        this.description = description;
        this.id = id;
    }

    public static Transformations fromId(int id) {
        for (Transformations t : Transformations.values()) {
            if (t.id == id) {
                return t;
            }
        }
        return null;
    }

    public abstract double[] apply(double x, double y, Coefficients coeff);
}
