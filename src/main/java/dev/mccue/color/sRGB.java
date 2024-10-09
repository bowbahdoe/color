package dev.mccue.color;

public record sRGB(
        double R,
        double G,
        double B
) implements Color {
    public sRGB clamped() {
        return new sRGB(
                Math.clamp(R, 0, 1),
                Math.clamp(G, 0, 1),
                Math.clamp(B, 0, 1)
        );
    }

    /// Checks whether the color exists in RGB space, i.e. all values are in [0..1]
    ///
    /// @return Whether the color exists in RGB space.
    public boolean isValid() {
        return 0.0 <= R && R <= 1.0 &&
                0.0 <= G && G <= 1.0 &&
                0.0 <= B && B <= 1.0;
    }

    @Override
    public sRGB sRGB() {
        return this;
    }

    static double linearize(double v) {
        if (v <= 0.04045) {
            return v / 12.92;
        }
        return Math.pow((v+0.055)/1.055, 2.4);
    }

    public LinearRGB LinearRGB() {
        var c = this;
        var r = linearize(c.R);
        var g = linearize(c.G);
        var b = linearize(c.B);
        return new LinearRGB(r, g, b);
    }

    public XYZ XYZ() {
        var c = this;
        return c.LinearRGB().XYZ();
    }

    // Converts the given color to CIE L*a*b* space using D65 as reference white.
    public Lab Lab() {
        return XYZ().Lab();
    }

    // Converts the given color to CIE L*a*b* space, taking into account
    // a given reference white. (i.e. the monitor's white)
    public Lab Lab(ReferenceWhite referenceWhite) {
        return XYZ().Lab(referenceWhite);
    }

    public HSL HSL() {
        var c = this;
        var min = Math.min(Math.min(c.R, c.G), c.B);
        var max = Math.max(Math.max(c.R, c.G), c.B);

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

            if (max == c.R) {
                h = (c.G - c.B) / (max - min);
            } else if (max == c.G) {
                h = 2.0 + (c.B-c.R)/(max-min);
            } else {
                h = 4.0 + (c.R-c.G)/(max-min);
            }

            h *= 60;

            if (h < 0) {
                h += 360;
            }
        }
        return new HSL(h, s, l);
    }

    // Generates a color by using data given in HCL space using D65 as reference white.
    // H values are in [0..360], C and L values are in [0..1]
    // WARNING: many combinations of `h`, `c`, and `l` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public static sRGB HCL(HCL hcl) {
        return HCL(hcl, ReferenceWhite.D65);
    }

    // Generates a color by using data given in HCL space, taking
    // into account a given reference white. (i.e. the monitor's white)
    // H values are in [0..360], C and L values are in [0..1]
    public static sRGB HCL(HCL hcl, ReferenceWhite referenceWhite) {
        return hcl.Lab().XYZ(referenceWhite).sRGB();
    }

    // Converts the given color to HCL space using D65 as reference white.
    // H values are in [0..360], C and L values are in [0..1] although C can overshoot 1.0
    public HCL HCL() {
        return HCL(ReferenceWhite.D65);
    }

    // Converts the given color to HCL space, taking into account
    // a given reference white. (i.e. the monitor's white)
    // H values are in [0..360], C and L values are in [0..1]
    public HCL HCL(ReferenceWhite referenceWhite) {
        var c = this;
        return c.Lab(referenceWhite).HCL();
    }

    // Converts the given color to CIE L*u*v* space using D65 as reference white.
    // L* is in [0..1] and both u* and v* are in about [-1..1]
    public Luv Luv() {
        var c = this;
        return c.XYZ().Luv();
    }

    // Converts the given color to CIE L*u*v* space, taking into account
    // a given reference white. (i.e. the monitor's white)
    // L* is in [0..1] and both u* and v* are in about [-1..1]
    public Luv Luv(ReferenceWhite referenceWhite) {
        var c = this;
        return c.XYZ().Luv(referenceWhite);
    }


    public HSV HSV() {
        var c = this;
        double h;
        double s;
        double v;

        var min = Math.min(Math.min(c.R, c.G), c.B);
        v = Math.max(Math.max(c.R, c.G), c.B);
        var C = v - min;

        s = 0.0;
        if (v != 0.0) {
            s = C / v;
        }

        h = 0.0; // We use 0 instead of undefined as in wp.
        if (min != v) {
            if (v == c.R) {
                h = ((c.G-c.B)/C) % 6.0;
            }
            if (v == c.G) {
                h = (c.B-c.R)/C + 2.0;
            }
            if (v == c.B) {
                h = (c.R-c.G)/C + 4.0;
            }
            h *= 60.0;
            if (h < 0.0) {
                h += 360.0;
            }
        }
        return new HSV(h, s, v);
    }


    // Converts the given color to LuvLCh space using D65 as reference white.
    // h values are in [0..360], C and L values are in [0..1] although C can overshoot 1.0
    public LuvLCh LuvLCh() {
        return this.LuvLCh(ReferenceWhite.D65);
    }

    // Converts the given color to LuvLCh space, taking into account
    // a given reference white. (i.e. the monitor's white)
    // h values are in [0..360], c and l values are in [0..1]
    public LuvLCh LuvLCh(ReferenceWhite referenceWhite) {
        return this.Luv(referenceWhite).LuvLCh();
    }

    public RGB255 RGB255() {
        var c = this;
        int r = (int) (c.R*255.0 + 0.5);
        int g = (int) (c.G*255.0 + 0.5);
        int b = (int) (c.B*255.0 + 0.5);
        return new RGB255(r, g, b);
    }



    // A much faster and still quite precise linearization using a 6th-order Taylor approximation.
    // See the accompanying Jupyter notebook for derivation of the constants.
    private static double linearize_fast(double v) {
        var v1 = v - 0.5;
        var v2 = v1 * v1;
        var v3 = v2 * v1;
        var v4 = v2 * v2;
        // v5 := v3*v2
        return -0.248750514614486 + 0.925583310193438*v + 1.16740237321695*v2 + 0.280457026598666*v3 - 0.0757991963780179*v4; //+ 0.0437040411548932*v5
    }

    public LinearRGB LinearRGB_fast() {
        var c = this;
        var r = linearize_fast(c.R);
        var g = linearize_fast(c.G);
        var b = linearize_fast(c.B);
        return new LinearRGB(r, g, b);
    }


    // Converts the given color to CIE xyY space using D65 as reference white.
    // (Note that the reference white is only used for black input.)
    // x, y and Y are in [0..1]
    @Override
    public xyY xyY() {
        return this.XYZ().xyY();
    }

    // Converts the given color to CIE xyY space, taking into account
    // a given reference white. (i.e. the monitor's white)
    // (Note that the reference white is only used for black input.)
    // x, y and Y are in [0..1]
    public xyY xyY(ReferenceWhite referenceWhite) {
        var c = this;
        return c.XYZ().xyY(referenceWhite);
    }

    // HPLuv returns the Hue, Saturation and Luminance of the color in the HSLuv
    // color space. Hue in [0..360], a Saturation [0..1], and a Luminance
    // (lightness) in [0..1].
    //
    // Note that HPLuv can only represent pastel colors, and so the Saturation
    // value could be much larger than 1 for colors it can't represent.
    @Override
    public HPLuv HPLuv() {
        return LuvLCh(ReferenceWhite.hSLuvD65).HPLuv();
    }

    // HSLuv returns the Hue, Saturation and Luminance of the color in the HSLuv
    // color space. Hue in [0..360], a Saturation [0..1], and a Luminance
    // (lightness) in [0..1].
    @Override
    public HSLuv HSLuv() {
        return LuvLCh(ReferenceWhite.hSLuvD65)
                .HSLuv();
    }

    private static double sq(double v) {
        return v * v;
    }

    public double distance(sRGB c2) {
        var c1 = this;
        return Math.sqrt(sq(c1.R - c2.R) + sq(c1.G - c2.G) + sq(c1.B - c2.B));
    }

    public double distanceRiemersma(sRGB other) {
        var c1 = this;
        var c2 = other;
        var rAvg = (c1.R() + c2.R()) / 2.0;
        // Deltas
        var dR = c1.R() - c2.R();
        var dG = c1.G() - c2.G();
        var dB = c1.B() - c2.B();

        return Math.sqrt((2 + rAvg) * dR * dR + 4 * dG * dG + (2 + (1 - rAvg)) * dB * dB);
    }

}
