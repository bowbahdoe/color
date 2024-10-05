package dev.mccue.color;

/// CIE-XYZ: CIE'S standard color space, almost in [0..1].
public record XYZ(
        double X,
        double Y,
        double Z
) {
    LinearRGB LinearRGB() {
        var r = 3.2409699419045214* X - 1.5373831775700935* Y - 0.49861076029300328* Z;
        var g = -0.96924363628087983* X + 1.8759675015077207* Y + 0.041555057407175613* Z;
        var b = 0.055630079696993609* X - 0.20397695888897657* Y + 1.0569715142428786* Z;
        return new LinearRGB(r, g, b);
    }

    static final double[] D65 = new double[] { 0.95047, 1.00000, 1.08883 };

    Lab Lab() {
        // Use D65 white as reference point by default.
        // http://www.fredmiranda.com/forum/topic/1035332
        // http://en.wikipedia.org/wiki/Standard_illuminant
        return Lab(D65);
    }

    static double lab_f(double t) {
        if (t > 6.0/29.0*6.0/29.0*6.0/29.0) {
            return Math.cbrt(t);
        }
        return t/3.0*29.0/6.0*29.0/6.0 + 4.0/29.0;
    }

    Lab Lab(double[] wref) {
        var fy = lab_f(Y / wref[1]);
        var l = 1.16*fy - 0.16;
        var a = 5.0 * (lab_f(X /wref[0]) - fy);
        var b = 2.0 * (fy - lab_f(Z /wref[2]));

        return new Lab(l, a, b);
    }

    Luv Luv() {
        return Luv(D65);
    }

    // For this part, we do as R's graphics.hcl does, not as wikipedia does.
    // Or is it the same?
    private record UV(double u, double v) {}
    private static UV xyz_to_uv(double x, double y, double z) {
        var denom = x + 15.0*y + 3.0*z;
        if (denom == 0.0) {
            return new UV(0, 0);
        } else {
            return new UV(
                    4.0 * x / denom,
                    9.0 * y / denom
            );
        }
    }

    Luv Luv(double[] wref) {
        double l;
        double u;
        double v;
        if (Y/wref[1] <= 6.0/29.0*6.0/29.0*6.0/29.0) {
            l = Y / wref[1] * (29.0 / 3.0 * 29.0 / 3.0 * 29.0 / 3.0) / 100.0;
        } else {
            l = 1.16*Math.cbrt(Y/wref[1]) - 0.16;
        }
        var uv = xyz_to_uv(X, Y, Z);
        var ubis = uv.u;
        var vbis = uv.v;

        switch (xyz_to_uv(wref[0], wref[1], wref[2])) {
            case UV(var un, var vn) -> {
                u = 13.0 * l * (ubis - un);
                v = v = 13.0 * l * (vbis - vn);
                return new Luv(l, u, v);
            }
        }
    }
}
