package dev.mccue.color;

import static dev.mccue.color.Util.sq;

public record HSLuv(
        double H,
        double S,
        double L
) implements Color {
    @Override
    public HSLuv HSLuv() {
        return this;
    }

    @Override
    public LuvLCh LuvLCh() {
        var l = 100 * L;
        var s = 100 * S;

        double c;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            c = 0.0;
        } else {
            max = Color.maxChromaForLH(l, H);
            c = max / 100.0 * s;
        }

        // c is [-100..100], but for LCh it's supposed to be almost [-1..1]
        return new LuvLCh(Math.clamp(l / 100.0, 0, 1), c / 100.0, H);
    }

    @Override
    public sRGB sRGB() {
        return this.LuvLCh().Luv().XYZ(ReferenceWhite.hSLuvD65).LinearRGB().sRGB().clamped();
    }

    public double distance(HSLuv c2) {
        var h1 = this.H;
        var s1 = this.S;
        var l1 = this.L;

        var h2 = c2.H;
        var s2 = c2.S;
        var l2 = c2.L;

        return Math.sqrt(sq((h1-h2)/100.0) + sq(s1-s2) + sq(l1-l2));
    }
}
