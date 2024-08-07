package ru.rodionkrainov.fastdevrkcustomuilib.utils;

public final class MathPlus {

    // ------------- Numbers ------------
    public static float roundTo(float _number, int _count) {
        float ten_power = (float) Math.pow(10, (float) _count);
        return (Math.round(_number * ten_power) / ten_power);
    }

    public static double roundTo(double _number, long _count) {
        double ten_power = Math.pow(10, (double) _count);
        return (Math.round(_number * ten_power) / ten_power);
    }

    // --------- For threads --------
    public static long calculateMillisToSleep(int _numAttempts) {
        return Math.round( (1000 / (_numAttempts + Math.exp(-_numAttempts))) );
    }
}
