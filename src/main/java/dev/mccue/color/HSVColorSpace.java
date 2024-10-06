package dev.mccue.color;

final class HSVColorSpace implements ColorSpace<HSV> {
    static final HSVColorSpace INSTANCE = new HSVColorSpace();

    private HSVColorSpace() {}

    // Hsv returns the Hue [0..360], Saturation and Value [0..1] of the color.
    @Override
    public HSV fromColor(Color c) {
        double h;
        double s;
        double v;

        var min = Math.min(Math.min(c.r, c.g), c.b);
        v = Math.max(Math.max(c.r, c.g), c.b);
        var C = v - min;

        s = 0.0;
        if (v != 0.0) {
            s = C / v;
        }

        h = 0.0; // We use 0 instead of undefined as in wp.
        if (min != v) {
            if (v == c.r) {
                h = ((c.g-c.b)/C) % 6.0;
            }
            if (v == c.g) {
                h = (c.b-c.r)/C + 2.0;
            }
            if (v == c.b) {
                h = (c.r-c.g)/C + 4.0;
            }
            h *= 60.0;
            if (h < 0.0) {
                h += 360.0;
            }
        }
        return new HSV(h, s, v);
    }

    // Hsv creates a new Color given a Hue in [0..360], a Saturation and a Value in [0..1]
    @Override
    public Color toColor(HSV hsv) {
        double H = hsv.H();
        double S = hsv.S();
        double V = hsv.V();

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

        return new Color(m + r, m + g, m + b);
    }
}
