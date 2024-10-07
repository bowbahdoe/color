package dev.mccue.color;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;

/// This class represents a color.
///
/// Its internal representation of is in sRGB (standard RGB). This maps to common
/// displays which display colors by powering separate Red, Green, and Blue
/// lights in specific proportions.
///
/// @apiNote This class does not validate or clamp values so that they
/// are in the 0-1 range that sRGB wants. This is to make it more practical
/// to use as an intermediate when converting between spaces that might have
/// temporary values which fall outside the human perceptible spectrum. If
/// you need something which does that clamping you should use {@link Color#sRGB()}
/// or the combination of {@link Color#isValid()} and {@link Color#clamped()}.
public final class Color {
    // A color is stored internally using sRGB (standard RGB) values in the range 0-1
    final double r;
    final double g;
    final double b;

    Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color sRGB(double r, double g, double b) {
        return sRGB(new sRGB(r, g, b));
    }

    public static Color sRGB(sRGB sRGB) {
        return new Color(sRGB.R(), sRGB.G(), sRGB.B());
    }

    public sRGB sRGB() {
        var c = this;
        c = c.clamped();
        return new sRGB(c.r, c.g, c.b);
    }

    public static Color HSL(double h, double s, double l) {
        return HSL(new HSL(h, s, l));
    }

    public static Color HSL(HSL hsl) {

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

    public HSL HSL() {
        var c = this;
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

    public static Color HCL(double h, double c, double l) {
        return HCL(new HCL(h, c, l));
    }

    // Generates a color by using data given in HCL space using D65 as reference white.
    // H values are in [0..360], C and L values are in [0..1]
    // WARNING: many combinations of `h`, `c`, and `l` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public static Color HCL(HCL hcl) {
        return HCL(hcl, ReferenceWhite.D65);
    }

    // Generates a color by using data given in HCL space, taking
    // into account a given reference white. (i.e. the monitor's white)
    // H values are in [0..360], C and L values are in [0..1]
    public static Color HCL(HCL hcl, ReferenceWhite referenceWhite) {
        return XYZ(hcl.Lab().XYZ(referenceWhite));
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


    public static Color HSV(double h, double s, double v) {
        return HSV(new HSV(h, s, v));
    }

    public static Color HSV(HSV hsv) {

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

    public HSV HSV() {
        var c = this;
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

    public static Color Lab(double L, double a, double b) {
        return Lab(new Lab(L, a, b));
    }

    // Generates a color by using data given in CIE L*a*b* space using D65 as reference white.
    // WARNING: many combinations of `l`, `a`, and `b` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public static Color Lab(Lab lab) {
        return XYZ(lab.XYZ());
    }

    // Generates a color by using data given in CIE L*a*b* space, taking
    // into account a given reference white. (i.e. the monitor's white)
    public static Color Lab(Lab lab, ReferenceWhite referenceWhite) {
        return XYZ(lab.XYZ(referenceWhite));
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

    public static Color LinearRGB(double R, double G, double B) {
        return LinearRGB(new LinearRGB(R, G, B));
    }

    static double delinearize(double v) {
        if (v <= 0.0031308) {
            return 12.92 * v;
        }
        return 1.055*Math.pow(v, 1.0/2.4) - 0.055;
    }

    public static Color LinearRGB(LinearRGB linearRGB) {
        return new Color(
                delinearize(linearRGB.R()),
                delinearize(linearRGB.G()),
                delinearize(linearRGB.B())
        );
    }


    static double linearize(double v) {
        if (v <= 0.04045) {
            return v / 12.92;
        }
        return Math.pow((v+0.055)/1.055, 2.4);
    }

    public LinearRGB LinearRGB() {
        var c = this;
        var r = linearize(c.r);
        var g = linearize(c.g);
        var b = linearize(c.b);
        return new LinearRGB(r, g, b);
    }

    /// Parses a "html" hex color-string, either in the 3 "#f0c" or 6 "#ff1034" digits form.
    public static Color hex(String s) {
        if (s.length() != 7 && s.length() != 4) {
            throw new ColorSpaceException("Input string must be in 3 or 6 digit hex form: " + s);
        }

        var factor = 1.0 / 255.0;
        int digits = 2;
        if (s.length() == 4) {
            digits = 1;
            factor = 1.0 / 15.0;
        }

        if (s.charAt(0) != '#') {
            throw new ColorSpaceException("Input string must start with a #: " + s);
        }

        int r;
        int g;
        int b;

        int idx = 1;
        try {
            r = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        idx += digits;
        try {
            g = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        idx += digits;
        try {
            b = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        return new Color(((double) r) * factor, ((double) g) * factor, ((double) b) * factor);
    }

    public String hex() {
        var c = this;
        return "#%02x%02x%02x".formatted(
                (int) Math.clamp(c.r*255.0+0.5, 0, 255),
                (int) Math.clamp(c.g*255.0+0.5, 0, 255),
                (int) Math.clamp(c.b*255.0+0.5, 0, 255)
        );
    }

    public static Color XYZ(double X, double Y, double Z) {
        return XYZ(new XYZ(X, Y, Z));
    }

    public static Color XYZ(XYZ XYZ) {
        return LinearRGB(XYZ.LinearRGB());
    }

    public XYZ XYZ() {
        var c = this;
        return c.LinearRGB().XYZ();
    }

    public static Color Luv(double L, double u, double v) {
        return Luv(new Luv(L, u, v));
    }

    // Generates a color by using data given in CIE L*u*v* space using D65 as reference white.
    // L* is in [0..1] and both u* and v* are in about [-1..1]
    // WARNING: many combinations of `l`, `u`, and `v` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public static Color Luv(Luv luv) {
        return Color.XYZ(luv.XYZ());
    }

    public static Color Luv(Luv luv, ReferenceWhite referenceWhite) {
        return Color.XYZ(luv.XYZ(referenceWhite));
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

    public static Color LuvLCh(double L, double C, double h) {
        return LuvLCh(new LuvLCh(L, C, h));
    }

    // Generates a color by using data given in LuvLCh space using D65 as reference white.
    // h values are in [0..360], C and L values are in [0..1]
    // WARNING: many combinations of `l`, `c`, and `h` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    public static Color LuvLCh(LuvLCh luvLCh) {
        return LuvLCh(luvLCh, ReferenceWhite.D65);
    }

    // Generates a color by using data given in LuvLCh space, taking
    // into account a given reference white. (i.e. the monitor's white)
    // h values are in [0..360], C and L values are in [0..1]
    public static Color LuvLCh(LuvLCh luvLCh, ReferenceWhite referenceWhite) {
        return Luv(luvLCh.Luv(), referenceWhite);
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


    public static Color RGB255(int R, int G, int B) {
        return RGB255(new RGB255(R, G, B));
    }

    public static Color RGB255(RGB255 rgb255) {
        return Color.sRGB(
                rgb255.R() / 255.0,
                rgb255.G() / 255.0,
                rgb255.B() / 255.0
        );
    }

    public RGB255 RGB255() {
        var c = this;
        int r = (int) (c.r*255.0 + 0.5);
        int g = (int) (c.g*255.0 + 0.5);
        int b = (int) (c.b*255.0 + 0.5);
        return new RGB255(r, g, b);
    }

    public static Color OkLab(double L, double a, double b) {
        return OkLab(new OkLab(L, a, b));
    }

    public static Color OkLab(OkLab okLab) {
        return XYZ(okLab.XYZ());
    }

    public OkLab OkLab() {
        var c = this;
        return c.XYZ().OkLab();
    }

    public static Color OkLch(double L, double c, double h) {
        return OkLch(new OkLch(L, c, h));
    }

    public static Color OkLch(OkLch okLch) {
        return OkLab(okLch.OkLab());
    }

    public OkLch OkLch() {
        var c = this;
        return c.OkLab().OkLch();
    }

    public static Color fastLinearRGB(double R, double G, double B) {
        return fastLinearRGB(new LinearRGB(R, G, B));
    }


    private static double delinearize_fast(double v) {
        // This function (fractional root) is much harder to linearize, so we need to split.
        if (v > 0.2) {
            var v1 = v - 0.6;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            return 0.442430344268235 + 0.592178981271708*v - 0.287864782562636*v2 + 0.253214392068985*v3 - 0.272557158129811*v4 + 0.325554383321718*v5;
        } else if (v > 0.03) {
            var v1 = v - 0.115;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            return 0.194915592891669 + 1.55227076330229*v - 3.93691860257828*v2 + 18.0679839248761*v3 - 101.468750302746*v4 + 632.341487393927*v5;
        } else {
            var v1 = v - 0.015;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            // You can clearly see from the involved constants that the low-end is highly nonlinear.
            return 0.0519565234928877 + 5.09316778537561*v - 99.0338180489702*v2 + 3484.52322764895*v3 - 150028.083412663*v4 + 7168008.42971613*v5;
        }
    }

    public static Color fastLinearRGB(LinearRGB linearRGB) {
        return new Color(
                delinearize_fast(linearRGB.R()),
                delinearize_fast(linearRGB.G()),
                delinearize_fast(linearRGB.B())
        );
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

    public LinearRGB fastLinearRGB() {
        var c = this;
        var r = linearize_fast(c.r);
        var g = linearize_fast(c.g);
        var b = linearize_fast(c.b);
        return new LinearRGB(r, g, b);
    }

    public static Color xyY(double x, double y, double Y) {
        return xyY(new xyY(x, y, Y));
    }

    // Generates a color by using data given in CIE xyY space.
    // x, y and Y are in [0..1]
    public static Color xyY(xyY xyY) {
        return XYZ(xyY.XYZ());
    }

    // Converts the given color to CIE xyY space using D65 as reference white.
    // (Note that the reference white is only used for black input.)
    // x, y and Y are in [0..1]
    public xyY xyY() {
        var c = this;
        return c.XYZ().xyY();
    }

    // Converts the given color to CIE xyY space, taking into account
    // a given reference white. (i.e. the monitor's white)
    // (Note that the reference white is only used for black input.)
    // x, y and Y are in [0..1]
    public xyY xyY(ReferenceWhite referenceWhite) {
        var c = this;
        return c.XYZ().xyY(referenceWhite);
    }

    public static Color HPLuv(double h, double s, double l) {
        return HPLuv(new HPLuv(h, s, l));
    }

    // HPLuv creates a new Color from values in the HPLuv color space.
    // Hue in [0..360], a Saturation [0..1], and a Luminance (lightness) in [0..1].
    //
    // The returned color values are clamped (using .Clamped), so this will never output
    // an invalid color.
    public static Color HPLuv(HPLuv hpLuv) {
        // HPLuv -> LuvLCh -> CIELUV -> CIEXYZ -> Linear RGB -> sRGB
        return LinearRGB(
                hpLuv
                        .LuvLCh()
                        .Luv()
                        .XYZ(ReferenceWhite.hSLuvD65)
                        .LinearRGB()
        ).clamped();
    }

    // HPLuv returns the Hue, Saturation and Luminance of the color in the HSLuv
    // color space. Hue in [0..360], a Saturation [0..1], and a Luminance
    // (lightness) in [0..1].
    //
    // Note that HPLuv can only represent pastel colors, and so the Saturation
    // value could be much larger than 1 for colors it can't represent.
    public HPLuv HPLuv() {
        return LuvLCh(ReferenceWhite.hSLuvD65).HPLuv();
    }

    // HSLuv returns the Hue, Saturation and Luminance of the color in the HSLuv
    // color space. Hue in [0..360], a Saturation [0..1], and a Luminance
    // (lightness) in [0..1].
    public HSLuv HSLuv() {
        return LuvLCh(ReferenceWhite.hSLuvD65)
                .HSLuv();
    }

    public static Color HSLuv(HSLuv hsLuv) {
        return LinearRGB(
                hsLuv.LuvLCh().Luv().XYZ(ReferenceWhite.hSLuvD65).LinearRGB()
        ).clamped();
    }

    /// Checks whether the color exists in RGB space, i.e. all values are in [0..1]
    ///
    /// @return Whether the color exists in RGB space.
    public boolean isValid() {
        return 0.0 <= r && r <= 1.0 &&
                0.0 <= g && g <= 1.0 &&
                0.0 <= b && b <= 1.0;
    }

    /// Clamps the {@link Color} into valid range, clamping each value to [0..1]
    /// If the {@link Color} is valid already, this is a no-op.
    ///
    /// @return A valid {@link Color}.
    public Color clamped() {
        return new Color(
                Math.clamp(r, 0, 1),
                Math.clamp(g, 0, 1),
                Math.clamp(b, 0, 1)
        );
    }

    @Override
    public String toString() {
        var rgb = RGB255();
        return "\033[48:2::%d:%d:%dm \033[49m"
                .formatted(
                        rgb.R(),
                        rgb.G(),
                        rgb.B()
                );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color c && r == c.r && g == c.g && b == c.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    /// Computes the distance between two colors in RGB space.
    ///
    /// @apiNote This is not a good measure! Rather do it in Lab space.
    public double distanceRGB(Color c2) {
        var c1 = this;
        return Math.sqrt(sq(c1.r - c2.r) + sq(c1.g - c2.g) + sq(c1.b - c2.b));
    }

    /// Computes the distance between two colors in linear RGB
    /// space. This is not useful for measuring how humans perceive color, but
    /// might be useful for other things, like dithering.
    public double distanceLinearRGB(Color c2) {
        var c1 = this;
        switch (c1.LinearRGB()) {
            case LinearRGB(var r1, var g1, var b1) -> {
                switch (c2.LinearRGB()) {
                    case LinearRGB(var r2, var g2, var b2) -> {
                        return Math.sqrt(sq(r1 - r2) + sq(g1 - g2) + sq(b1 - b2));
                    }
                }
            }
        }
    }

    /// Color distance algorithm developed by Thiadmer Riemersma.
    /// It uses RGB coordinates, but he claims it has similar results to CIELUV.
    /// This would make it both fast and accurate.
    ///
    /// Sources:
    ///
    /// @see <a href="https://www.compuphase.com/cmetric.htm">https://www.compuphase.com/cmetric.htm</a>
    /// @see <a href="https://github.com/lucasb-eyer/go-colorful/issues/52">https://github.com/lucasb-eyer/go-colorful/issues/52</a>
    public double distanceRiemersma(Color c2) {
        var c1 = this;
        var rAvg = (c1.r + c2.r) / 2.0;
        // Deltas
        var dR = c1.r - c2.r;
        var dG = c1.g - c2.g;
        var dB = c1.b - c2.b;

        return Math.sqrt((2 + rAvg) * dR * dR + 4 * dG * dG + (2 + (1 - rAvg)) * dB * dB);
    }

    /// Uses the Delta E 2000 formula to calculate color
    /// distance. It is more expensive but more accurate than both DistanceLab
    /// and DistanceCIE94.
    public double distanceCIEDE2000(Color c2) {
        return distanceCIEDE2000klch(c2, 1, 1, 1);
    }

    /// Uses the Delta E 2000 formula with custom values
    /// for the weighting factors kL, kC, and kH.
    ///
    /// @param cr The color to compare to.
    /// @param kL weighting factor
    /// @param kC weighting factor
    /// @param kH weighting factor
    /// @return Distance between colors.
    public double distanceCIEDE2000klch(Color cr, double kL, double kC, double kH) {
        var cl = this;
        var lab1 = cl.Lab();
        var lab2 = cr.Lab();

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

    // DistanceLab is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    public double distanceLab(Color c2) {
        var c1 = this;
        switch (c1.Lab()) {
            case Lab(double l1, double a1, double b1) -> {
                switch (c2.Lab()) {
                    case Lab(double l2, double a2, double b2) -> {
                        return Math.sqrt(sq(l1 - l2) + sq(a1 - a2) + sq(b1 - b2));
                    }
                }
            }
        }
    }

    // DistanceCIE76 is the same as DistanceLab.
    public double distanceCIE76(Color c2) {
        return distanceLab(c2);
    }


    // Uses the CIE94 formula to calculate color distance. More accurate than
    // DistanceLab, but also more work.
    public double distanceCIE94(Color cr) {
        var cl = this;

        switch (cl.Lab()) {
            case Lab(var l1, var a1, var b1) -> {
                switch (cr.Lab()) {
                    case Lab(var l2, var a2, var b2) -> {
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
            }
        }
    }

    private static final double kappa = 903.2962962962963;
    private static final double epsilon = 0.0088564516790356308;

    private  static final double[][] m = {
            {3.2409699419045214, -1.5373831775700935, -0.49861076029300328},
            {-0.96924363628087983, 1.8759675015077207, 0.041555057407175613},
            {0.055630079696993609, -0.20397695888897657, 1.0569715142428786},
    };

    private static double[][] getBounds(double l) {
        double sub2;
        var ret = new double[6][2];
        var sub1 = Math.pow(l+16.0, 3.0) / 1560896.0;
        if (sub1 > epsilon) {
            sub2 = sub1;
        } else {
            sub2 = l / kappa;
        }
        for (int i = 0; i < m.length; i++) {
            for (int k = 0; k < 2; k++) {
                var top1 = (284517.0*m[i][0] - 94839.0*m[i][2]) * sub2;
                var top2 = (838422.0*m[i][2]+769860.0*m[i][1]+731718.0*m[i][0])*l*sub2 - 769860.0*((double) k)*l;
                var bottom = (632260.0*m[i][2]-126452.0*m[i][1])*sub2 + 126452.0*((double) k);
                ret[i*2+k][0] = top1 / bottom;
                ret[i*2+k][1] = top2 / bottom;
            }
        }

        return ret;
    }

    private static double lengthOfRayUntilIntersect(double theta, double x, double y) {
        return y / (Math.sin(theta) - x*Math.cos(theta));
    }

    static double maxChromaForLH(double l, double h) {
        var hRad = h / 360.0 * Math.PI * 2.0;
        var minLength = Double.MAX_VALUE;
        for (var line : getBounds(l)) {
            var length = lengthOfRayUntilIntersect(hRad, line[0], line[1]);
            if (length > 0.0 && length < minLength) {
                minLength = length;
            }
        }
        return minLength;
    }

    static double maxSafeChromaForL(double l) {
        var minLength = Double.MAX_VALUE;
        for (var line : getBounds(l)) {
            var m1 = line[0];
            var b1 = line[1];
            var x = intersectLineLine(m1, b1, -1.0/m1, 0.0);
            var dist = distanceFromPole(x, b1+x*m1);
            if (dist < minLength) {
                minLength = dist;
            }
        }
        return minLength;
    }

    private static double intersectLineLine(double x1, double y1, double x2, double y2) {
        return (y1 - y2) / (x2 - x1);
    }

    private static double distanceFromPole(double x, double y) {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
    }

    // DistanceLuv is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    public double distanceLuv(Color c2) {
        var c1 = this;
        switch (c1.Luv()) {
            case Luv(double l1, double u1, double v1) -> {
                switch (c2.Luv()) {
                    case Luv(double l2, double u2, double v2) -> {
                        return Math.sqrt(sq(l1 - l2) + sq(u1 - u2) + sq(v1 - v2));
                    }
                }
            }
        }
    }

    // DistanceHSLuv calculates Euclidan distance in the HSLuv colorspace. No idea
    // how useful this is.
    //
    // The Hue value is divided by 100 before the calculation, so that H, S, and L
    // have the same relative ranges.
    public double distanceHSLuv(Color c2) {
        var c1 = this;
        switch (c1.HSLuv()) {
            case HSLuv(double h1, double s1, double l1) -> {
                switch (c2.HSLuv()) {
                    case HSLuv(double h2, double s2, double l2) -> {
                        return Math.sqrt(sq((h1-h2)/100.0) + sq(s1-s2) + sq(l1-l2));
                    }
                }
            }
        }
    }

    // DistanceHPLuv calculates Euclidean distance in the HPLuv colorspace. No idea
    // how useful this is.
    //
    // The Hue value is divided by 100 before the calculation, so that H, S, and L
    // have the same relative ranges.
    public double distanceHPLuv(Color c2) {
        var c1 = this;
        switch (c1.HPLuv()) {
            case HPLuv(double h1, double s1, double l1) -> {
                switch (c2.HPLuv()) {
                    case HPLuv(double h2, double s2, double l2) -> {
                        return Math.sqrt(sq((h1-h2)/100.0) + sq(s1-s2) + sq(l1-l2));
                    }
                }
            }
        }
    }

    // An element represents a single element of a set.  It is used to
    // implement a disjoint-set forest.
    private static final class Element {
        // Parent element
        Element parent;
        // Rank (approximate depth) of the subtree with this element as root
        int rank;
    }

    // newElement creates a singleton set and returns its sole element.
    private static Element newElement() {
        var s = new Element();
        s.parent = s;
        return s;
    }

    // find returns an arbitrary element of a set when invoked on any element of
    // the set, The important feature is that it returns the same value when
    // invoked on any element of the set.  Consequently, it can be used to test if
    // two elements belong to the same set.
    private static Element find(Element e) {
        for (; e.parent != e; ) {
            e.parent = e.parent.parent;
            e = e.parent;
        }
        return e;
    }

    // union establishes the union of two sets when given an element from each set.
    // Afterwards, the original sets no longer exist as separate entities.
    private static void union(Element e1, Element e2) {
        // Ensure the two elements aren't already part of the same union.
        var e1Root = find(e1);
        var e2Root = find(e2);
        if (e1Root == e2Root) {
            return;
        }

        // Create a union by making the shorter tree point to the root of the
        // larger tree.
        if (e1Root.rank < e2Root.rank) {
            e1Root.parent = e2Root;
        } else if (e1Root.rank > e2Root.rank) {
            e2Root.parent = e1Root;
        } else {
            e2Root.parent = e1Root;
            e1Root.rank++;
        }
    }

    private record EdgeIdxs(int _0, int _1) {
    }

    private record EdgeDistances(Map<EdgeIdxs, Double> value) {
    }

    // allToAllDistancesCIEDE2000 computes the CIEDE2000 distance between each pair of
    // colors.  It returns a map from a pair of indices (u, v) with u < v to a
    // distance.
    private static EdgeDistances allToAllDistances(List<Color> cs, ColorDistance colorDistance) {
        var nc = cs.size();
        var m = new HashMap<EdgeIdxs, Double>(nc * nc);
        for (int u = 0; u < nc - 1; u++) {
            for (int v = u + 1; v < nc; v++) {
                m.put(new EdgeIdxs(u, v), colorDistance.distance(cs.get(u), cs.get(v)));
            }
        }
        return new EdgeDistances(m);
    }

    // sortEdges sorts all edges in a distance map by increasing vertex distance.
    private static ArrayList<EdgeIdxs> sortEdges(EdgeDistances m) {
        var es = new ArrayList<EdgeIdxs>(m.value.size());
        es.addAll(m.value.keySet());

        es.sort(Comparator.comparing((i) -> m.value.getOrDefault(i, 0.0)));
        return es;
    }

    // minSpanTree computes a minimum spanning tree from a vertex count and a
    // distance-sorted edge list.  It returns the subset of edges that belong to
    // the tree, including both (u, v) and (v, u) for each edge.
    private static Set<EdgeIdxs> minSpanTree(int nc, List<EdgeIdxs> es) {
        // Start with each vertex in its own set.
        var elts = new Element[nc];
        for (int i = 0; i < elts.length; i++) {
            elts[i] = newElement();
        }

        // Run Kruskal's algorithm to construct a minimal spanning tree.
        var mst = new LinkedHashSet<EdgeIdxs>(nc);
        for (var uv : es) {
            var u = uv._0;
            var v = uv._1;
            if (find(elts[u]) == find(elts[v])) {
                continue; // Same set: edge would introduce a cycle.
            }
            mst.add(uv);
            mst.add(new EdgeIdxs(v, u));
            union(elts[u], elts[v]);
        }

        return mst;
    }

    // traverseMST walks a minimum spanning tree in prefix order.
    private static ArrayList<Integer> traverseMST(Set<EdgeIdxs> mst, int root) {
        // Compute a list of neighbors for each vertex.
        var neighs = new LinkedHashMap<Integer, ArrayList<Integer>>(mst.size());
        for (var uv : mst) {
            var u = uv._0;
            var v = uv._1;
            neighs.putIfAbsent(u, new ArrayList<>());
            neighs.get(u).add(v);
        }

        for (var vs : neighs.values()) {
            vs.sort(Comparator.naturalOrder());
        }

        // Walk the tree from a given vertex.
        ArrayList<Integer> order = new ArrayList<Integer>(neighs.size());
        Set<Integer> visited = new LinkedHashSet<>();

        IntConsumer walkFrom = new IntConsumer() {
            @Override
            public void accept(int r) {
                // Visit the starting vertex.
                order.add(r);
                visited.add(r);

                // Recursively visit each child in turn.
                for (var c : neighs.get(r)) {
                    if (!visited.contains(c)) {
                        this.accept(c);
                    }
                }
            }
        };

        walkFrom.accept(root);

        return order;
    }

    public static List<Color> sort(List<Color> cs) {
        return sort(cs, Color::distanceCIEDE2000);
    }

    public static List<Color> sort(List<Color> cs, ColorDistance colorDistance) {
        if (cs.size() < 2) {
            return List.copyOf(cs);
        }

        // Do nothing in trivial cases.
        var newCs = new Color[cs.size()];

        var dists = allToAllDistances(cs, colorDistance);

        var edges = sortEdges(dists);

        var mst = minSpanTree(cs.size(), edges);

        // Find the darkest color in the list.
        var black = Color.sRGB(0, 0, 0);
        int dIdx = 0; // Index of darkest color
        var light = Double.MAX_VALUE; // Lightness of darkest color (distance from black)

        for (int i = 0; i < cs.size(); i++) {
            var c = cs.get(i);
            var d = colorDistance.distance(black, c);
            if (d < light) {
                dIdx = i;
                light = d;
            }
        }

        // Traverse the tree starting from the darkest color.
        var idxs = traverseMST(mst, dIdx);

        // Convert the index list to a list of colors, overwriting the input.
        for (int i = 0; i < idxs.size(); i++) {
            var idx = idxs.get(i);
            newCs[i] = cs.get(idx);
        }

        return List.of(newCs);
    }

    // BlendLab blends two colors in the L*a*b* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLab(Color c2, double t) {
        var c1 = this;

        switch (c1.Lab()) {
            case Lab(double l1, double a1, double b1) -> {
                switch (c2.Lab()) {
                    case Lab(double l2, double a2, double b2) -> {
                        return Lab(l1 + t * (l2 - l1),
                                a1 + t * (a2 - a1),
                                b1 + t * (b2 - b1));
                    }
                }
            }
        }
    }

    public Color blendLab(Color c2) {
        return blendLab(c2, 0.5);
    }

    // BlendLuv blends two colors in the CIE-L*u*v* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLuv(Color c2, double t) {
        var c1 = this;

        switch (c1.Luv()) {
            case Luv(double l1, double u1, double v1) -> {
                switch (c2.Luv()) {
                    case Luv(double l2, double u2, double v2) -> {
                        return Luv(l1 + t * (l2 - l1),
                                u1 + t * (u2 - u1),
                                v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    public Color blendLuv(Color c2) {
        return blendLuv(c2, 0.5);
    }

    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    public Color blendRGB(Color c2, double t) {
        var c1 = this;
        return new Color(
                c1.r + t * (c2.r - c1.r),
                c1.g + t * (c2.g - c1.g),
                c1.b + t * (c2.b - c1.b)
        );
    }

    public Color blendRGB(Color c2) {
        return blendRGB(c2, 0.5);
    }

    // BlendLinearRGB blends two colors in the Linear RGB color-space.
    // Unlike BlendRGB, this will not produce dark color around the center.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLinearRGB(Color c2, double t) {
        var c1 = this;
        switch (c1.LinearRGB()) {
            case LinearRGB(double r1, double g1, double b1) -> {
                switch (c2.LinearRGB()) {
                    case LinearRGB(double r2, double g2, double b2) -> {
                        return Color.LinearRGB(
                                r1 + t * (r2 - r1),
                                g1 + t * (g2 - g1),
                                b1 + t * (b2 - b1)
                        );
                    }
                }
            }
        }
    }

    public Color blendLinearRGB(Color c2) {
        return blendLinearRGB(c2, 0.5);
    }

    // Utility used by Hxx color-spaces for interpolating between two angles in [0,360].
    private static double interp_angle(double a0, double a1, double t) {
        // Based on the answer here: http://stackoverflow.com/a/14498790/2366315
        // With potential proof that it works here: http://math.stackexchange.com/a/2144499
        var delta = (((a1 - a0 % 360.0) + 540) % 360.0) - 180.0;
        return (a0 + t * delta + 360.0) % 360.0;
    }

    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    public Color blendHSV(Color c2, double t) {
        var c1 = this;
        switch (c1.HSV()) {
            case HSV(double h1, double s1, double v1) -> {
                switch (c2.HSV()) {
                    case HSV(double h2, double s2, double v2) -> {
                        // https://github.com/lucasb-eyer/go-colorful/pull/60
                        if (s1 == 0 && s2 != 0) {
                            h1 = h2;
                        } else if (s2 == 0 && s1 != 0) {
                            h2 = h1;
                        }

                        // We know that h are both in [0..360]
                        return HSV(interp_angle(h1, h2, t), s1 + t * (s2 - s1), v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    public Color blendHSV(Color c2) {
        return blendHSV(c2, 0.5);
    }

    // BlendHcl blends two colors in the CIE-L*C*h° color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendHCL(Color col2, double t) {
        var col1 = this;

        switch (col1.HCL()) {
            case HCL(var h1, var c1, var l1) -> {
                switch (col2.HCL()) {
                    case HCL(var h2, var c2, var l2) -> {
                        // https://github.com/lucasb-eyer/go-colorful/pull/60
                        if (c1 <= 0.00015 && c2 >= 0.00015) {
                            h1 = h2;
                        } else if (c2 <= 0.00015 && c1 >= 0.00015) {
                            h2 = h1;
                        }

                        // We know that h are both in [0..360]
                        return HCL(interp_angle(h1, h2, t), c1 + t * (c2 - c1), l1 + t * (l2 - l1)).clamped();
                    }
                }
            }
        }
    }

    public Color blendHCL(Color col2) {
        return blendHCL(col2, 0.5);
    }

    // BlendLuvLCh blends two colors in the cylindrical CIELUV color space.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLuvLCh(Color col2, double t) {
        switch (this.LuvLCh()) {
            case LuvLCh(double l1, double c1, double h1) -> {
                switch (col2.LuvLCh()) {
                    case LuvLCh(double l2, double c2, double h2) -> {
                        // We know that h are both in [0..360]
                        return LuvLCh(l1+t*(l2-l1), c1+t*(c2-c1), interp_angle(h1, h2, t));

                    }
                }
            }
        }
     }

     public Color blendLuvLCh(Color col2) {
        return blendLuvLCh(col2, 0.5);
     }

    public static Color warm() {
        return warm(ThreadLocalRandom.current());
    }

    /// Creates a random dark, "warm" color through restricted HCL space.
    /// This is slower than FastWarmColor but will likely give you colors which have
    /// the same "warmness" if you run it many times.
    ///
    /// @param random The random number generator to use.
    /// @return A random dark, "warm" color.
    public static Color warm(RandomGenerator random) {
        Color c = randomWarm(random);
        while (!c.isValid()) {
            c = randomWarm(random);
        }
        return c;
    }

    public static Color fastWarm() {
        return fastWarm(ThreadLocalRandom.current());
    }

    // Creates a random dark, "warm" color through a restricted HSV space.
    public static Color fastWarm(RandomGenerator random) {
        return HSV(
                random.nextDouble()*360.0,
                0.5+random.nextDouble()*0.3,
                0.3+random.nextDouble()*0.3
        );
    }

    private static Color randomWarm(RandomGenerator random) {
        return HCL(
                random.nextDouble() * 360.0,
                0.1 + random.nextDouble() * 0.3,
                0.2 + random.nextDouble() * 0.3
        );
    }

    public static Color fastHappy() {
        return fastHappy(ThreadLocalRandom.current());
    }

    // Creates a random bright, "pimpy" color through a restricted HSV space.
    public static Color fastHappy(RandomGenerator random) {
        return HSV(
                random.nextDouble()*360.0,
                0.7+random.nextDouble()*0.3,
                0.6+random.nextDouble()*0.3
        );
    }

    public static Color happy() {
        return happy(ThreadLocalRandom.current());
    }

    // Creates a random bright, "pimpy" color through restricted HCL space.
    // This is slower than FastHappyColor but will likely give you colors which
    // have the same "brightness" if you run it many times.
    public static Color happy(RandomGenerator random) {
        Color c = randomPimp(random);
        while (!c.isValid()) {
            c = randomPimp(random);
        }
        return c;
    }

    private static Color randomPimp(RandomGenerator random) {
        return HCL(
                random.nextDouble()*360.0,
                0.5+random.nextDouble()*0.3,
                0.5+random.nextDouble()*0.3
        );
    }

    // Check for equality between colors within the tolerance Delta (1/255).
    public boolean almostEqualRGB(Color c2, double delta) {
        var c1 = this;
        return Math.abs(c1.r - c2.r) +
                Math.abs(c1.g - c2.g) +
                Math.abs(c1.b - c2.b) < 3.0 * delta;
    }

    private static final double DELTA = 1.0 / 255;

    // Check for equality between colors within the tolerance Delta (1/255).
    public boolean almostEqualRGB(Color c2) {
        return almostEqualRGB(c2, DELTA);
    }

    public static List<Color> fastWarm(int colorsCount) {
        return fastWarm(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> fastWarm(int colorsCount, RandomGenerator random) {
        Color[] colorArr = new Color[colorsCount];
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] = HSV(
                    new HSV(i * (360.0 / ((double) colorsCount)), 0.55 + random.nextDouble() * 0.2, 0.35 + random.nextDouble() * 0.2)
            );
        }

        return List.of(colorArr);
    }

    public static List<Color> fastHappy(int colorsCount) {
        return fastHappy(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> fastHappy(int colorsCount, RandomGenerator random) {
        Color[] colorArr = new Color[colorsCount];
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] = HSV(
                    new HSV(i * (360.0 / ((double) colorsCount)), 0.8 + random.nextDouble() * 0.2, 0.65 + random.nextDouble() * 0.2)
            );
        }

        return List.of(colorArr);
    }

    private static final class PaletteGenerationSettings {
        Predicate<Lab> checkColor = __ -> true;
        int iterations = 50;
        boolean manySamples;
    }

    public static List<Color> soft(int colorsCount) {
        return soft(colorsCount, ThreadLocalRandom.current());
    }
    public static List<Color> soft(int colorsCount, RandomGenerator random) {
        var settings = new PaletteGenerationSettings();
        settings.iterations = 50;
        settings.manySamples = false;

        return soft(colorsCount, settings, random);
    }

    private static List<Color> soft(int colorsCount, PaletteGenerationSettings settings, RandomGenerator random) {
        // Checks whether it's a valid RGB and also fulfills the potentially provided constraint.
        Predicate<Lab> check = (col) -> {
            var c = Lab(new Lab(col.L(), col.a(), col.b()));
            return c.isValid() && settings.checkColor.test(col);
        };

        // Sample the color space. These will be the points k-means is run on.
        var dl = 0.05;
        var dab = 0.1;
        if (settings.manySamples) {
            dl = 0.01;
            dab = 0.05;
        }

        ArrayList<Lab> samples = new ArrayList<>((int) (1.0 / dl * 2.0 / dab * 2.0 / dab));
        for (double l = 0.0; l <= 1.0; l += dl) {
            for (double a = -1.0; a <= 1.0; a += dab) {
                for (double b = -1.0; b <= 1.0; b += dab) {
                    var lab = new Lab(l, a, b);
                    if (check.test(lab)) {
                        samples.add(lab);
                    }
                }
            }
        }

        // That would cause some infinite loops down there...
        if (samples.size() < colorsCount) {
            throw new PaletteGenerationException(
                    "More colors requested (%d) than samples available (%d). Your requested color count may be wrong, you might want to use many samples or your constraint function makes the valid color space too small".formatted(colorsCount, samples.size())
            );
        } else if (samples.size() == colorsCount) {
            return List.of(labs2cols(samples)); // Oops?
        }

        // We take the initial means out of the samples, so they are in fact medoids.
        // This helps us avoid infinite loops or arbitrary cutoffs with too restrictive constraints.
        Lab[] means = new Lab[colorsCount];
        for (int i = 0; i < colorsCount; i++) {
            for (
                    means[i] = samples.get(random.nextInt(samples.size()));
                    in(means, i, means[i]);
                    means[i] = samples.get(random.nextInt(samples.size()))
            ) {
            }

        }

        int[] clusters = new int[samples.size()];
        boolean[] samples_used = new boolean[samples.size()];

        // The actual k-means/medoid iterations
        for (int i = 0; i < settings.iterations; i++) {
            // Reassing the samples to clusters, i.e. to their closest mean.
            // By the way, also check if any sample is used as a medoid and if so, mark that.
            for (int isample = 0; isample < samples.size(); isample++) {
                var sample = samples.get(isample);
                samples_used[isample] = false;
                var mindist = Double.POSITIVE_INFINITY;
                for (int imean = 0; imean < means.length; imean++) {
                    var mean = means[imean];
                    var dist = lab_dist(sample, mean);
                    if (dist < mindist) {
                        mindist = dist;
                        clusters[isample] = imean;
                    }

                    if (lab_eq(sample, mean)) {
                        samples_used[isample] = true;
                    }
                }
            }


            // Compute new means according to the samples.
            for (int imean = 0; imean < means.length; imean++) {
                // The new mean is the average of all samples belonging to it..
                var nsamples = 0;
                var newmean = new Lab(0, 0, 0);
                for (int isample = 0; isample < samples.size(); isample++) {
                    var sample = samples.get(isample);
                    if (clusters[isample] == imean) {
                        nsamples++;
                        newmean = new Lab(
                                newmean.L() + sample.L(),
                                newmean.a() + sample.a(),
                                newmean.b() + sample.b()
                        );
                    }
                }
                if (nsamples > 0) {
                    newmean = new Lab(
                            newmean.L() / nsamples,
                            newmean.a() / nsamples,
                            newmean.b() / nsamples
                    );
                } else {
                    // That mean doesn't have any samples? Get a new mean from the sample list!
                    int inewmean;
                    for (
                            inewmean = random.nextInt(samples_used.length);
                            samples_used[inewmean];
                            inewmean = random.nextInt(samples_used.length)
                    ) {
                    }
                    newmean = samples.get(inewmean);
                    samples_used[inewmean] = true;
                }

                // But now we still need to check whether the new mean is an allowed color.
                if (nsamples > 0 && check.test(newmean)) {
                    // It does, life's good (TM)
                    means[imean] = newmean;
                } else {
                    // New mean isn't an allowed color or doesn't have any samples!
                    // Switch to medoid mode and pick the closest (unused) sample.
                    // This should always find something thanks to len(samples) >= colorsCount
                    var mindist = Double.POSITIVE_INFINITY;
                    for (int isample = 0; isample < samples.size(); isample++) {
                        var sample = samples.get(isample);
                        if (!samples_used[isample]) {
                            var dist = lab_dist(sample, newmean);
                            if (dist < mindist) {
                                mindist = dist;
                                newmean = sample;
                            }
                        }
                    }
                }
            }

        }

        return List.of(labs2cols(Arrays.asList(means)));
    }

    public static List<Color> warm(int colorsCount) {
        return warm(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> warm(int colorsCount, RandomGenerator random) {
        Predicate<Lab> warmy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.1 <= c && c <= 0.4 && 0.2 <= l && l <= 0.5;
        };
        var settings = new PaletteGenerationSettings();
        settings.checkColor = warmy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings, random);
    }

    public static List<Color> happy(int colorsCount) {
        return happy(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> happy(int colorsCount, RandomGenerator random) {
        Predicate<Lab> pimpy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.3 <= c && 0.4 <= l && l <= 0.8;
        };
        var settings = new PaletteGenerationSettings();
        settings.checkColor = pimpy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings, random);
    }

    private static boolean in(Lab[] haystack, int upto, Lab needle) {
        for (int i = 0; i < upto && i < haystack.length; i++) {
            if (Objects.equals(haystack[i], needle)) {
                return true;
            }
        }
        return false;
    }

    private static double sq(double v) {
        return v * v;
    }

    // That's faster than using colorful's DistanceLab since we would have to
    // convert back and forth for that. Here is no conversion.
    private static double lab_dist(Lab lab1, Lab lab2) {
        return Math.sqrt(sq(lab1.L() - lab2.L()) + sq(lab1.a() - lab2.a()) + sq(lab1.b() - lab2.b()));
    }

    private static final double LAB_DELTA = 1e-6;

    private static boolean lab_eq(Lab lab1, Lab lab2) {
        return Math.abs(lab1.L() - lab2.L()) < LAB_DELTA &&
                Math.abs(lab1.a() - lab2.a()) < LAB_DELTA &&
                Math.abs(lab1.b() - lab2.b()) < LAB_DELTA;
    }

    private static Color[] labs2cols(List<Lab> labs) {
        Color[] cols = new Color[labs.size()];
        for (int i = 0; i < labs.size(); i++) {
            var v = labs.get(i);
            cols[i] = Color.Lab(v);
        }
        return cols;
    }
}
