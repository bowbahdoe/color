package dev.mccue.color;

public record HCL(
        double H,
        double C,
        double L
) {
    static final double[] D65 = new double[] { 0.95047, 1.00000, 1.08883 };


    Lab Lab() {
        var H = 0.01745329251994329576 * this.H; // Deg2Rad
        double a = C * Math.cos(H);
        double b = C * Math.sin(H);
        double L = this.L;
        return new Lab(L, a, b);
    }
}
