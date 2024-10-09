package dev.mccue.color;

public record HSL(double H, double S, double L) implements Color {
    public HSL(
            /// Hue
            double H,
            /// Saturation
            double S,
            /// Lightness
            double L
    ) {
        this.H = H % 360;
        this.S = S;
        this.L = L;
    }

    @Override
    public HSL HSL() {
        return this;
    }

    @Override
    public sRGB sRGB() {
        double h = this.H();
        double s = this.S();
        double l = this.L();

        if (s == 0) {
            return new sRGB(l, l, l);
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

        return new sRGB(r, g, b);
    }
}
