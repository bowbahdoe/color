package dev.mccue.color;

// Note that h is in [0..360] and s,v in [0..1]
/// The HSV color space.
///
/// @see <a href="http://en.wikipedia.org/wiki/HSL_and_HSV>http://en.wikipedia.org/wiki/HSL_and_HSV</a>
public record HSV(
        /// Hue
        double H,
        /// Saturation
        double S,
        /// Value
        double V
) implements Color {
    public HSV(
            double H,
            double S,
            double V
    ) {
        this.H = H % 360;
        this.S = S;
        this.V = V;
    }

    @Override
    public HSV HSV() {
        return this;
    }

    @Override
    public sRGB sRGB() {
        double H = this.H();
        double S = this.S();
        double V = this.V();

        var Hp = H / 60.0;
        var C = V * S;
        var X = C * (1.0 - Math.abs((Hp % 2.0)-1.0));

        var m = V - C;
        double r = 0;
        double g = 0;
        double b = 0;

        if (0.0 <= Hp && Hp < 1.0) {
            r = C;
            g = X;
        }
        else if (1.0 <= Hp && Hp < 2.0) {
            r = X;
            g = C;
        }
        else if (2.0 <= Hp && Hp < 3.0) {
            g = C;
            b = X;
        }
        else if (3.0 <= Hp && Hp < 4.0) {
            g = X;
            b = C;
        }
        else if (4.0 <= Hp && Hp < 5.0) {
            r = X;
            b = C;
        }
        else if (5.0 <= Hp && Hp < 6.0) {
            r = C;
            b = X;
        }

        return new sRGB(m + r, m + g, m + b);
    }
}
