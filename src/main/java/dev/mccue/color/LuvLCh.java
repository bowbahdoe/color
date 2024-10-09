package dev.mccue.color;

import static dev.mccue.color.Util.interp_angle;

public record LuvLCh(double L, double C, double h) implements Color {
    @Override
    public LuvLCh LuvLCh() {
        return this;
    }

    // Generates a color by using data given in LuvLCh space using D65 as reference white.
    // h values are in [0..360], C and L values are in [0..1]
    // WARNING: many combinations of `l`, `c`, and `h` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    @Override
    public sRGB sRGB() {
        return sRGB(ReferenceWhite.D65);
    }

    // Generates a color by using data given in LuvLCh space, taking
    // into account a given reference white. (i.e. the monitor's white)
    // h values are in [0..360], C and L values are in [0..1]
    public sRGB sRGB(ReferenceWhite referenceWhite) {
        return this.Luv(referenceWhite).sRGB();
    }

    public HSLuv HSLuv() {
        // [-1..1] but the code expects it to be [-100..100]
        var c = C * 100.0;
        var l = L * 100.0;

        double s;
        double max;
        if (l > 99.9999999 || l < 0.00000001) {
            s = 0.0;
        } else {
            max = Color.maxChromaForLH(l, h);
            s = c / max * 100.0;
        }
        return new HSLuv(
                h, Math.clamp(s / 100.0, 0, 1), Math.clamp(l / 100.0, 0, 1)
        );
    }

    @Override
    public Luv Luv() {
        double L;
        double u;
        double v;

        var H = 0.01745329251994329576 * h; // Deg2Rad
        u = this.C * Math.cos(H);
        v = this.C * Math.sin(H);
        L = this.L;
        return new Luv(L, u, v);
    }

    public Luv Luv(ReferenceWhite referenceWhite) {
        return Luv().Luv(referenceWhite);
    }

    public HPLuv HPLuv() {
        var c = C;
        var l = L;
        // [-1..1] but the code expects it to be [-100..100]
        c *= 100.0;
        l *= 100.0;

        double s;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            s = 0.0;
        } else {
            max = Color.maxSafeChromaForL(l);
            s = c / max * 100.0;
        }
        return new HPLuv(h, s / 100.0, l / 100.0);
    }



    public LuvLCh blend(LuvLCh other, double t) {
        switch (this) {
            case LuvLCh(double l1, double c1, double h1) -> {
                switch (other) {
                    case LuvLCh(double l2, double c2, double h2) -> {
                        // We know that h are both in [0..360]
                        return new LuvLCh(l1+t*(l2-l1), c1+t*(c2-c1), interp_angle(h1, h2, t));

                    }
                }
            }
        }
    }

    public LuvLCh blend(LuvLCh other) {
        return blend(other, 0.5);
    }
}
