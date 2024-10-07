package dev.mccue.color;

public record HCL(
        double H,
        double C,
        double L
) {
    public Lab Lab() {
        var H = 0.01745329251994329576 * this.H; // Deg2Rad
        double a = C * Math.cos(H);
        double b = C * Math.sin(H);
        double L = this.L;
        return new Lab(L, a, b);
    }
}
