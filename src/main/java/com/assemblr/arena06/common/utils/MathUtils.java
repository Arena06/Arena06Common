package com.assemblr.arena06.common.utils;


public final class MathUtils {
    
    private MathUtils() {}
    
    public static int clamp(int x, int min, int max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
    
    public static double clamp(double x, double min, double max) {
        if (x < min) return min;
        if (x > max) return max;
        return x;
    }
    
    /*
    @return: the CCW angle formed by these cordinates and the ray along the positive x axis and it assumes the y axis is inverted.
    */
    public static double getAngle(double x, double y) {
        if (x > 0) {
            return Math.atan(-y / x);
        }
        return 0;
    }
}
