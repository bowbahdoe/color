package dev.mccue.color;

public record Lab(
        double L,
        double a,
        double b
) implements Color {
    public Lab Lab() {
        return this;
    }

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

    public LabLCh HCL() {
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
        return new LabLCh(l, c, h);
    }


    // Generates a color by using data given in CIE L*a*b* space using D65 as reference white.
    // WARNING: many combinations of `l`, `a`, and `b` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public sRGB sRGB() {
        return this.XYZ().sRGB();
    }

    // Generates a color by using data given in CIE L*a*b* space, taking
    // into account a given reference white. (i.e. the monitor's white)
    public sRGB sRGB(ReferenceWhite referenceWhite) {
        return this.XYZ(referenceWhite).sRGB();
    }

    public Lab blend(Lab c2, double t) {
        var c1 = this;

        var l1 = c1.L;
        var a1 = c1.a;
        var b1 = c1.b;

        var l2 = c2.L;
        var a2 = c2.a;
        var b2 = c2.b;

        return new Lab(l1 + t * (l2 - l1),
                a1 + t * (a2 - a1),
                b1 + t * (b2 - b1));
    }

    public Lab blend(Lab c2) {
        return blend(c2, 0.5);
    }

    public double distance(Lab c2) {
        var c1 = this;

        var l1 = c1.L;
        var a1 = c1.a;
        var b1 = c1.b;

        var l2 = c2.L;
        var a2 = c2.a;
        var b2 = c2.b;

        return Math.sqrt(sq(l1 - l2) + sq(a1 - a2) + sq(b1 - b2));
    }
    public double distanceCIEDE2000(Lab c2) {
        return distanceCIEDE2000klch(c2, 1, 1, 1);
    }

    public double distanceCIEDE2000klch(Lab lab2, double kL, double kC, double kH) {
        var lab1 = this;

        double l1 = lab1.L();
        double a1 = lab1.a();
        double b1 = lab1.b();

        double l2 = lab2.L();
        double a2 = lab2.a();
        double b2 = lab2.b();

        // As with CIE94, we scale up the ranges of L,a,b beforehand and scale
        // them down again afterwards.
        l1 = l1 * 100;
        a1 = a1 * 100;
        b1 = b1 * 100;
        l2 = l2 * 100;
        a2 = a2 * 100;
        b2 = b2 * 100;

        var cab1 = Math.sqrt(sq(a1) + sq(b1));
        var cab2 = Math.sqrt(sq(a2) + sq(b2));
        var cabmean = (cab1 + cab2) / 2;

        var g = 0.5 * (1 - Math.sqrt(Math.pow(cabmean, 7) / (Math.pow(cabmean, 7) + Math.pow(25, 7))));
        var ap1 = (1 + g) * a1;
        var ap2 = (1 + g) * a2;
        var cp1 = Math.sqrt(sq(ap1) + sq(b1));
        var cp2 = Math.sqrt(sq(ap2) + sq(b2));

        var hp1 = 0.0;
        if (b1 != ap1 || ap1 != 0) {
            hp1 = Math.atan2(b1, ap1);
            if (hp1 < 0) {
                hp1 += Math.PI * 2;
            }
            hp1 *= 180 / Math.PI;
        }
        var hp2 = 0.0;
        if (b2 != ap2 || ap2 != 0) {
            hp2 = Math.atan2(b2, ap2);
            if (hp2 < 0) {
                hp2 += Math.PI * 2;
            }
            hp2 *= 180 / Math.PI;
        }

        var deltaLp = l2 - l1;
        var deltaCp = cp2 - cp1;
        var dhp = 0.0;
        var cpProduct = cp1 * cp2;
        if (cpProduct != 0) {
            dhp = hp2 - hp1;
            if (dhp > 180) {
                dhp -= 360;
            } else if (dhp < -180) {
                dhp += 360;
            }
        }
        var deltaHp = 2 * Math.sqrt(cpProduct) * Math.sin(dhp / 2 * Math.PI / 180);

        var lpmean = (l1 + l2) / 2;
        var cpmean = (cp1 + cp2) / 2;
        var hpmean = hp1 + hp2;
        if (cpProduct != 0) {
            hpmean /= 2;
            if (Math.abs(hp1 - hp2) > 180) {
                if (hp1 + hp2 < 360) {
                    hpmean += 180;
                } else {
                    hpmean -= 180;
                }
            }
        }

        var t = 1 - 0.17 * Math.cos((hpmean - 30) * Math.PI / 180) + 0.24 * Math.cos(2 * hpmean * Math.PI / 180) + 0.32 * Math.cos((3 * hpmean + 6) * Math.PI / 180) - 0.2 * Math.cos((4 * hpmean - 63) * Math.PI / 180);
        var deltaTheta = 30 * Math.exp(-sq((hpmean - 275) / 25));
        var rc = 2 * Math.sqrt(Math.pow(cpmean, 7) / (Math.pow(cpmean, 7) + Math.pow(25, 7)));
        var sl = 1 + (0.015 * sq(lpmean - 50)) / Math.sqrt(20 + sq(lpmean - 50));
        var sc = 1 + 0.045 * cpmean;
        var sh = 1 + 0.015 * cpmean * t;
        var rt = -Math.sin(2 * deltaTheta * Math.PI / 180) * rc;

        return Math.sqrt(sq(deltaLp / (kL * sl)) + sq(deltaCp / (kC * sc)) + sq(deltaHp / (kH * sh)) + rt * (deltaCp / (kC * sc)) * (deltaHp / (kH * sh))) * 0.01;

    }

    // Uses the CIE94 formula to calculate color distance. More accurate than
    // DistanceLab, but also more work.
    public double distanceCIE94(Lab cr) {
        var cl = this;

        var l1 = cl.L;
        var a1 = cl.a;
        var b1 = cl.b;

        var l2 = cr.L;
        var a2 = cr.a;
        var b2 = cr.b;

        // NOTE: Since all those formulas expect L,a,b values 100x larger than we
        //       have them in this library, we either need to adjust all constants
        //       in the formula, or convert the ranges of L,a,b before, and then
        //       scale the distances down again. The latter is less error-prone.
        l1 = 100 * l1;
        a1 = 100 * a1;
        b1 = 100 * b1;

        l2 = 100 * l2;
        a2 = 100 * a2;
        b2 = 100 * b2;

        var kl = 1.0; // 2.0 for textiles
        var kc = 1.0;
        var kh = 1.0;
        var k1 = 0.045; // 0.048 for textiles
        var k2 = 0.015; // 0.014 for textiles.

        var deltaL = l1 - l2;
        var c1 = Math.sqrt(sq(a1) + sq(b1));
        var c2 = Math.sqrt(sq(a2) + sq(b2));
        var deltaCab = c1 - c2;

        // Not taking Sqrt here for stability, and it's unnecessary.
        var deltaHab2 = sq(a1 - a2) + sq(b1 - b2) - sq(deltaCab);
        var sl = 1.0;
        var sc = 1.0 + k1 * c1;
        var sh = 1.0 + k2 * c1;

        var vL2 = sq(deltaL / (kl * sl));
        var vC2 = sq(deltaCab / (kc * sc));
        var vH2 = deltaHab2 / sq(kh * sh);

        return Math.sqrt(vL2 + vC2 + vH2) * 0.01; // See above.
    }
}
