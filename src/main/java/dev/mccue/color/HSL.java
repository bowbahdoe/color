package dev.mccue.color;

public record HSL(double H, double S, double L) {
    public HSL(
            /// Hue
            double H,
            /// Saturation
            double S,
            /// Lightness
            double L
    ) {
        this.H = Math.clamp(H, 0, 360);
        this.S = Math.clamp(S, 0, 1);
        this.L = Math.clamp(L, 0, 1);
    }
}
