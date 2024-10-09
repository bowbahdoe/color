package dev.mccue.color;

public record Luv(
        double L,
        double u,
        double v
) implements Color {
    @Override
    public Luv Luv() {
        return this;
    }

    @Override
    public XYZ XYZ() {
        return XYZ(ReferenceWhite.D65);
    }

    private static double cub(double v) {
        return v * v * v;
    }

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

    public XYZ XYZ(ReferenceWhite wref) {
        double x;
        double y;
        double z = 0;

        // y = wref[1] * lab_finv((l + 0.16) / 1.16)
        if (L <= 0.08) {
            y = wref._1 * L * 100.0 * 3.0 / 29.0 * 3.0 / 29.0 * 3.0 / 29.0;
        } else {
            y = wref._1 * cub((L+0.16)/1.16);
        }
        switch (xyz_to_uv(wref._0, wref._1, wref._2)) {
            case UV(var un, var vn) -> {
                if (L != 0.0) {
                    var ubis = u/(13.0*L) + un;
                    var vbis = v/(13.0*L) + vn;
                    x = y * 9.0 * ubis / (4.0 * vbis);
                    z = y * (12.0 - 3.0*ubis - 20.0*vbis) / (4.0 * vbis);
                } else {
                    x = 0;
                    y = 0;
                }

                return new XYZ(x, y, z);
            }
        }
    }

    private static double sq(double v) {
        return v * v;
    }

    public LuvLCh LuvLCh() {
        // Oops, floating point workaround necessary if u ~= v and both are very small (i.e. almost zero).
        double l;
        double c;
        double h;
        if (Math.abs(v-u) > 1e-4 && Math.abs(u) > 1e-4) {
            h = (57.29577951308232087721*Math.atan2(v, u)+360.0) % 360.0; // Rad2Deg
        } else {
            h = 0.0;
        }
        l = L;
        c = Math.sqrt(sq(u) + sq(v));

        return new LuvLCh(l, c, h);
    }

    // Generates a color by using data given in CIE L*u*v* space using D65 as reference white.
    // L* is in [0..1] and both u* and v* are in about [-1..1]
    // WARNING: many combinations of `l`, `u`, and `v` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public sRGB sRGB() {
        return this.XYZ().sRGB();
    }

    public sRGB sRGB(ReferenceWhite referenceWhite) {
        return this.XYZ(referenceWhite).sRGB();
    }

    public double distance(Luv c2) {
        var l1 = L;
        var u1 = u;
        var v1 = v;

        var l2 = c2.L;
        var u2 = c2.u;
        var v2 = c2.v;
        return Math.sqrt(sq(l1 - l2) + sq(u1 - u2) + sq(v1 - v2));
    }

}
