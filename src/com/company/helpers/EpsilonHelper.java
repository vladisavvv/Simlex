package com.company.helpers;

public class EpsilonHelper {
    public static final double EPS = 1e-9;

    public static boolean isZero(double a) {
        return Math.abs(a) < EPS;
    }

    public static boolean isPositive(double a) {
        return a >= EPS;
    }

    public static boolean isNegative(double a) {
        return a <= -EPS;
    }
}
