package ru.vsu.cs.p_p_v;

public final class FastMath {

    public static double pow(double a, int b) {
        double c = 1;

        while (b-- > 0) {
            c *= a;
        }

        return c;
    }

}
