package dev.mccue.color;

final class HSLColorSpace implements ColorSpace<HSL> {
    static final HSLColorSpace INSTANCE = new HSLColorSpace();

    private HSLColorSpace() {}

    @Override
    public HSL fromColor(Color c) {
        var min = Math.min(Math.min(c.r, c.g), c.b);
        var max = Math.max(Math.max(c.r, c.g), c.b);

        double h;
        double s;
        double l;

        l = (max + min) / 2;

        if (min == max) {
            s = 0;
            h = 0;
        } else {
            if (l < 0.5) {
                s = (max - min) / (max + min);
            } else {
                s = (max - min) / (2.0 - max - min);
            }

            if (max == c.r) {
                h = (c.g - c.b) / (max - min);
            } else if (max == c.g) {
                h = 2.0 + (c.b-c.r)/(max-min);
            } else {
                h = 4.0 + (c.r-c.g)/(max-min);
            }

            h *= 60;

            if (h < 0) {
                h += 360;
            }
        }
        return new HSL(h, s, l);
    }

    @Override
    public Color toColor(HSL hsl) {
        double h = hsl.H();
        double s = hsl.S();
        double l = hsl.L();

        if (s == 0) {
            return new Color(l, l, l);
        }

        double r;
        double g;
        double b;

        double t1;
        double t2;
        double tr;
        double tg;
        double tb;

        if (l < 0.5) {
            t1 = l * (1.0 + s);
        } else {
            t1 = l + s - l*s;
        }

        t2 = 2*l - t1;
        h /= 360;
        tr = h + 1.0/3.0;
        tg = h;
        tb = h - 1.0/3.0;

        if (tr < 0) {
            tr++;
        }
        if (tr > 1) {
            tr--;
        }
        if (tg < 0) {
            tg++;
        }
        if (tg > 1) {
            tg--;
        }
        if (tb < 0) {
            tb++;
        }
        if (tb > 1) {
            tb--;
        }

        // Red
        if (6*tr < 1) {
            r = t2 + (t1-t2)*6*tr;
        } else if (2*tr < 1) {
            r = t1;
        } else if (3*tr < 2) {
            r = t2 + (t1-t2)*(2.0/3.0-tr)*6;
        } else {
            r = t2;
        }

        // Green
        if (6*tg < 1) {
            g = t2 + (t1-t2)*6*tg;
        } else if (2*tg < 1) {
            g = t1;
        } else if (3*tg < 2) {
            g = t2 + (t1-t2)*(2.0/3.0-tg)*6;
        } else {
            g = t2;
        }

        // Blue
        if (6*tb < 1) {
            b = t2 + (t1-t2)*6*tb;
        } else if (2*tb < 1) {
            b = t1;
        } else if (3*tb < 2) {
            b = t2 + (t1-t2)*(2.0/3.0-tb)*6;
        } else {
            b = t2;
        }

        return new Color(r, g, b);
    }
}
