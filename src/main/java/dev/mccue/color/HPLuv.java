package dev.mccue.color;

public record HPLuv(
        double H,
        double S,
        double L
) implements Color {
    @Override
    public HPLuv HPLuv() {
        return this;
    }

    @Override
    public LuvLCh LuvLCh() {
        // [-1..1] but the code expects it to be [-100..100]
        var l = L * 100.0;
        var s = S * 100.0;

        double c;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            c = 0.0;
        } else {
            max = Color.maxSafeChromaForL(l);
            c = max / 100.0 * s;
        }
        return new LuvLCh(l / 100.0, c / 100.0, H);
    }

    // HPLuv creates a new Color from values in the HPLuv color space.
    // Hue in [0..360], a Saturation [0..1], and a Luminance (lightness) in [0..1].
    //
    // The returned color values are clamped (using .Clamped), so this will never output
    // an invalid color.
    @Override
    public sRGB sRGB() {
        // HPLuv -> LuvLCh -> CIELUV -> CIEXYZ -> Linear RGB -> sRGB
        return this
                .LuvLCh()
                .Luv()
                .XYZ(ReferenceWhite.hSLuvD65)
                .LinearRGB()
                .sRGB()
                .clamped();
    }

    private static double sq(double v) {
        return v * v;
    }

    public double distance(HPLuv hpLuv) {
        var h1 = H;
        var h2 = hpLuv.H;

        var s1 = S;
        var s2 = hpLuv.S;

        var l1 = L;
        var l2 = hpLuv.L;

        return Math.sqrt(sq((h1-h2)/100.0) + sq(s1-s2) + sq(l1-l2));
    }
}
