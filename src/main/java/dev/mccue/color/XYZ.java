package dev.mccue.color;

/// CIE-XYZ: CIE'S standard color space, almost in [0..1].
@ColorSpace(
        names = "CIE-XYZ",
        references = {
                "http://www.sjbrown.co.uk/2004/05/14/gamma-correct-rendering/",
                "https://en.wikipedia.org/wiki/CIE_1931_color_space"
        }
)
public record XYZ(
        double X,
        double Y,
        double Z
) {
    // XyzToLinearRgb converts from CIE XYZ-space to Linear RGB space.
    LinearRGB LinearRGB() {
        var r = 3.2409699419045214* X - 1.5373831775700935* Y - 0.49861076029300328* Z;
        var g = -0.96924363628087983* X + 1.8759675015077207* Y + 0.041555057407175613* Z;
        var b = 0.055630079696993609* X - 0.20397695888897657* Y + 1.0569715142428786* Z;
        return new LinearRGB(r, g, b);
    }

    Lab Lab() {
        // Use D65 white as reference point by default.
        // http://www.fredmiranda.com/forum/topic/1035332
        // http://en.wikipedia.org/wiki/Standard_illuminant
        return Lab(ReferenceWhite.D65);
    }

    private static double lab_f(double t) {
        if (t > 6.0/29.0*6.0/29.0*6.0/29.0) {
            return Math.cbrt(t);
        }
        return t/3.0*29.0/6.0*29.0/6.0 + 4.0/29.0;
    }

    Lab Lab(ReferenceWhite wref) {
        var fy = lab_f(Y / wref._1);
        var l = 1.16*fy - 0.16;
        var a = 5.0 * (lab_f(X /wref._0) - fy);
        var b = 2.0 * (fy - lab_f(Z /wref._2));

        return new Lab(l, a, b);
    }

    Luv Luv() {
        return Luv(ReferenceWhite.D65);
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

    public Luv Luv(ReferenceWhite wref) {
        double l;
        double u;
        double v;
        if (Y/wref._1 <= 6.0/29.0*6.0/29.0*6.0/29.0) {
            l = Y / wref._1 * (29.0 / 3.0 * 29.0 / 3.0 * 29.0 / 3.0) / 100.0;
        } else {
            l = 1.16*Math.cbrt(Y/wref._1) - 0.16;
        }
        var uv = xyz_to_uv(X, Y, Z);
        var ubis = uv.u;
        var vbis = uv.v;

        switch (xyz_to_uv(wref._0, wref._1, wref._2)) {
            case UV(var un, var vn) -> {
                u = 13.0 * l * (ubis - un);
                v = 13.0 * l * (vbis - vn);
                return new Luv(l, u, v);
            }
        }
    }

    public OkLab OkLab() {
        var l_ = Math.cbrt(0.8189330101*X + 0.3618667424*Y - 0.1288597137*Z);
        var m_ = Math.cbrt(0.0329845436*X + 0.9293118715*Y + 0.0361456387*Z);
        var s_ = Math.cbrt(0.0482003018*X + 0.2643662691*Y + 0.6338517070*Z);
        var l = 0.2104542553*l_ + 0.7936177850*m_ - 0.0040720468*s_;
        var a = 1.9779984951*l_ - 2.4285922050*m_ + 0.4505937099*s_;
        var b = 0.0259040371*l_ + 0.7827717662*m_ - 0.8086757660*s_;
        return new OkLab(l, a, b);
    }

    public OkLch OkLch() {
        return OkLab().OkLch();
    }

    public xyY xyY() {
        return xyY(ReferenceWhite.D65);
    }

    public xyY xyY(ReferenceWhite wref) {
        double x;
        double y;
        var N = X + Y + Z;

        if (Math.abs(N) < 1e-14) {
            // When we have black, Bruce Lindbloom recommends to use
            // the reference white's chromacity for x and y.
            x = wref._0 / (wref._0 + wref._1 + wref._2);
            y = wref._1 / (wref._0 + wref._1 + wref._2);
        } else {
            x = X / N;
            y = Y / N;
        }
        return new xyY(x, y, Y);
    }
}
