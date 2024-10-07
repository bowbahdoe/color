package dev.mccue.color;

public record Lab(
        double L,
        double a,
        double b
) {

    public XYZ XYZ() {
        return XYZ(ReferenceWhite.D65);
    }

    static double lab_finv(double t) {
        if (t > 6.0/29.0) {
            return t * t * t;
        }
        return 3.0 * 6.0 / 29.0 * 6.0 / 29.0 * (t - 4.0/29.0);
    }

    public XYZ XYZ(ReferenceWhite wref) {
        var l2 = (L + 0.16) / 1.16;
        var x = wref._0 * lab_finv(l2+a/5.0);
        var y = wref._1 * lab_finv(l2);
        var z = wref._2 * lab_finv(l2-b/2.0);
        return new XYZ(x, y, z);
    }

    private static double sq(double v) {
        return v * v;
    }

    public HCL HCL() {
        double h;
        double c;
        double l;

        // Oops, floating point workaround necessary if a ~= b and both are very small (i.e. almost zero).
        if (Math.abs(b-a) > 1e-4 && Math.abs(a) > 1e-4) {
            h = (57.29577951308232087721*Math.atan2(b, a)+360.0) % 360.0; // Rad2Deg
        } else {
            h = 0.0;
        }
        c = Math.sqrt(sq(a) + sq(b));
        l = L;
        return new HCL(h, c, l);
    }

    public Lab blend(Lab c2, double t) {
        var c1 = this;

        switch (c1) {
            case Lab(double l1, double a1, double b1) -> {
                switch (c2) {
                    case Lab(double l2, double a2, double b2) -> {
                        return new Lab(l1 + t * (l2 - l1),
                                a1 + t * (a2 - a1),
                                b1 + t * (b2 - b1));
                    }
                }
            }
        }
    }

    public Lab blend(Lab c2) {
        return blend(c2, 0.5);
    }
}
