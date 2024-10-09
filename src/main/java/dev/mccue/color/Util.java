package dev.mccue.color;

final class Util {
    private Util() {}

    // Utility used by Hxx color-spaces for interpolating between two angles in [0,360].
    static double interp_angle(double a0, double a1, double t) {
        // Based on the answer here: http://stackoverflow.com/a/14498790/2366315
        // With potential proof that it works here: http://math.stackexchange.com/a/2144499
        var delta = (((a1 - a0 % 360.0) + 540) % 360.0) - 180.0;
        return (a0 + t * delta + 360.0) % 360.0;
    }

    static double sq(double v) {
        return v * v;
    }
}
