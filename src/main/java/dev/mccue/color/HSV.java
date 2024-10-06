package dev.mccue.color;

// From http://en.wikipedia.org/wiki/HSL_and_HSV
// Note that h is in [0..360] and s,v in [0..1]
public record HSV(
        /// Hue
        double H,
        /// Saturation
        double S,
        /// Value
        double V
) {
    public HSV(
            double H,
            double S,
            double V
    ) {
        this.H = Math.clamp(H, 0, 360);
        this.S = Math.clamp(S, 0, 1);
        this.V = Math.clamp(V, 0, 1);
    }
}
