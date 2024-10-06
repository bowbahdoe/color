package dev.mccue.color.test;

import dev.mccue.color.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorTest {
    public record Val(
            Color c,
            HSL hsl,
            HSV hsv,
            String hex,
            XYZ xyz,
            xyY xyY,
            Lab lab,
            Lab lab50,
            Luv luv,
            Luv luv50,
            HCL hcl,
            HCL hcl50,
            RGB255 rgb255
    ) {
        public Val(
                Color c,
                double[] hsl,
                double[] hsv,
                String hex,
                double[] xyz,
                double[] xyY,
                double[] lab,
                double[] lab50,
                double[] luv,
                double[] luv50,
                double[] hcl,
                double[] hcl50,
                int[] rgb255
        ) {
            this(
                    c,
                    new HSL(hsl[0], hsl[1], hsl[2]),
                    new HSV(hsv[0], hsv[1], hsv[2]),
                    hex,
                    new XYZ(xyz[0], xyz[1], xyz[2]),
                    new xyY(xyY[0], xyY[1], xyY[2]),
                    new Lab(lab[0], lab[1], lab[2]),
                    new Lab(lab50[0], lab50[1], lab50[2]),
                    new Luv(luv[0], luv[1], luv[2]),
                    new Luv(luv50[0], luv50[1], luv50[2]),
                    new HCL(hcl[0], hcl[1], hcl[2]),
                    new HCL(hcl50[0], hcl50[1], hcl50[2]),
                    new RGB255(rgb255[0], rgb255[1], rgb255[2])
            );
        }
    }

    /*

     */

    static List<Val> vals() {
        return List.of(
                new Val(
                        Color.sRGB(1.0, 1.0, 1.0),
                        new HSL(0.0, 0.0, 1.00),
                        new HSV(0.0, 0.0, 1.0),
                        "#ffffff",
                        new XYZ(0.950470, 1.000000, 1.088830),
                        new xyY(0.312727, 0.329023, 1.000000),
                        new Lab(1.000000, 0.000000, 0.000000),
                        new Lab(1.000000, -0.023881, -0.193622),
                        new Luv(1.00000, 0.00000, 0.00000),
                        new Luv(1.00000, -0.14716, -0.25658),
                        new HCL(0.0000, 0.000000, 1.000000),
                        new HCL(262.9688, 0.195089, 1.000000),
                        new RGB255(255, 255, 255)
                ),
                new Val(
                        Color.sRGB(0.5, 1.0, 1.0),
                        new double[]{180.0, 1.0, 0.75},
                        new double[]{180.0, 0.5, 1.0},
                        "#80ffff",
                        new double[]{0.626296, 0.832848, 1.073634},
                        new double[]{0.247276, 0.328828, 0.832848},
                        new double[]{0.931390, -0.353319, -0.108946},
                        new double[]{0.931390, -0.374100, -0.301663},
                        new double[]{0.93139, -0.53909, -0.11630},
                        new double[]{0.93139, -0.67615, -0.35528},
                        new double[]{197.1371, 0.369735, 0.931390},
                        new double[]{218.8817, 0.480574, 0.931390},
                        new int[]{128, 255, 255}
                ),
                new Val(Color.sRGB(1.0, 0.5, 1.0), new double[]{300.0, 1.0, 0.75}, new double[]{300.0, 0.5, 1.0}, "#ff80ff", new double[]{0.669430, 0.437920, 0.995150}, new double[]{0.318397, 0.208285, 0.437920}, new double[]{0.720892, 0.651673, -0.422133}, new double[]{0.720892, 0.630425, -0.610035}, new double[]{0.72089, 0.60047, -0.77626}, new double[]{0.72089, 0.49438, -0.96123}, new double[]{327.0661, 0.776450, 0.720892}, new double[]{315.9417, 0.877257, 0.720892}, new int[]{255, 128, 255}),
                new Val(Color.sRGB(1.0, 1.0, 0.5), new double[]{60.0, 1.0, 0.75}, new double[]{60.0, 0.5, 1.0}, "#ffff80", new double[]{0.808654, 0.943273, 0.341930}, new double[]{0.386203, 0.450496, 0.943273}, new double[]{0.977637, -0.165795, 0.602017}, new double[]{0.977637, -0.188424, 0.470410}, new double[]{0.97764, 0.05759, 0.79816}, new double[]{0.97764, -0.08628, 0.54731}, new double[]{105.3975, 0.624430, 0.977637}, new double[]{111.8287, 0.506743, 0.977637}, new int[]{255, 255, 128}),
                new Val(Color.sRGB(0.5, 0.5, 1.0), new double[]{240.0, 1.0, 0.75}, new double[]{240.0, 0.5, 1.0}, "#8080ff", new double[]{0.345256, 0.270768, 0.979954}, new double[]{0.216329, 0.169656, 0.270768}, new double[]{0.590453, 0.332846, -0.637099}, new double[]{0.590453, 0.315806, -0.824040}, new double[]{0.59045, -0.07568, -1.04877}, new double[]{0.59045, -0.16257, -1.20027}, new double[]{297.5843, 0.718805, 0.590453}, new double[]{290.9689, 0.882482, 0.590453}, new int[]{128, 128, 255}),
                new Val(Color.sRGB(1.0, 0.5, 0.5), new double[]{0.0, 1.0, 0.75}, new double[]{0.0, 0.5, 1.0}, "#ff8080", new double[]{0.527613, 0.381193, 0.248250}, new double[]{0.455996, 0.329451, 0.381193}, new double[]{0.681085, 0.483884, 0.228328}, new double[]{0.681085, 0.464258, 0.110043}, new double[]{0.68108, 0.92148, 0.19879}, new double[]{0.68106, 0.82106, 0.02393}, new double[]{25.2610, 0.535049, 0.681085}, new double[]{13.3347, 0.477121, 0.681085}, new int[]{255, 128, 128}),
                new Val(Color.sRGB(0.5, 1.0, 0.5), new double[]{120.0, 1.0, 0.75}, new double[]{120.0, 0.5, 1.0}, "#80ff80", new double[]{0.484480, 0.776121, 0.326734}, new double[]{0.305216, 0.488946, 0.776121}, new double[]{0.906026, -0.600870, 0.498993}, new double[]{0.906026, -0.619946, 0.369365}, new double[]{0.90603, -0.58869, 0.76102}, new double[]{0.90603, -0.72202, 0.52855}, new double[]{140.2920, 0.781050, 0.906026}, new double[]{149.2134, 0.721640, 0.906026}, new int[]{128, 255, 128}),
                new Val(Color.sRGB(0.5, 0.5, 0.5), new double[]{0.0, 0.0, 0.50}, new double[]{0.0, 0.0, 0.5}, "#808080", new double[]{0.203440, 0.214041, 0.233054}, new double[]{0.312727, 0.329023, 0.214041}, new double[]{0.533890, 0.000000, 0.000000}, new double[]{0.533890, -0.014285, -0.115821}, new double[]{0.53389, 0.00000, 0.00000}, new double[]{0.53389, -0.07857, -0.13699}, new double[]{0.0000, 0.000000, 0.533890}, new double[]{262.9688, 0.116699, 0.533890}, new int[]{128, 128, 128}),
                new Val(Color.sRGB(0.0, 1.0, 1.0), new double[]{180.0, 1.0, 0.50}, new double[]{180.0, 1.0, 1.0}, "#00ffff", new double[]{0.538014, 0.787327, 1.069496}, new double[]{0.224656, 0.328760, 0.787327}, new double[]{0.911132, -0.480875, -0.141312}, new double[]{0.911132, -0.500630, -0.333781}, new double[]{0.91113, -0.70477, -0.15204}, new double[]{0.91113, -0.83886, -0.38582}, new double[]{196.3762, 0.501209, 0.911132}, new double[]{213.6923, 0.601698, 0.911132}, new int[]{0, 255, 255}),
                new Val(Color.sRGB(1.0, 0.0, 1.0), new double[]{300.0, 1.0, 0.50}, new double[]{300.0, 1.0, 1.0}, "#ff00ff", new double[]{0.592894, 0.284848, 0.969638}, new double[]{0.320938, 0.154190, 0.284848}, new double[]{0.603242, 0.982343, -0.608249}, new double[]{0.603242, 0.961939, -0.794531}, new double[]{0.60324, 0.84071, -1.08683}, new double[]{0.60324, 0.75194, -1.24161}, new double[]{328.2350, 1.155407, 0.603242}, new double[]{320.4444, 1.247640, 0.603242}, new int[]{255, 0, 255}),
                new Val(Color.sRGB(1.0, 1.0, 0.0), new double[]{60.0, 1.0, 0.50}, new double[]{60.0, 1.0, 1.0}, "#ffff00", new double[]{0.770033, 0.927825, 0.138526}, new double[]{0.419320, 0.505246, 0.927825}, new double[]{0.971393, -0.215537, 0.944780}, new double[]{0.971393, -0.237800, 0.847398}, new double[]{0.97139, 0.07706, 1.06787}, new double[]{0.97139, -0.06590, 0.81862}, new double[]{102.8512, 0.969054, 0.971393}, new double[]{105.6754, 0.880131, 0.971393}, new int[]{255, 255, 0}),
                new Val(Color.sRGB(0.0, 0.0, 1.0), new double[]{240.0, 1.0, 0.50}, new double[]{240.0, 1.0, 1.0}, "#0000ff", new double[]{0.180437, 0.072175, 0.950304}, new double[]{0.150000, 0.060000, 0.072175}, new double[]{0.322970, 0.791875, -1.078602}, new double[]{0.322970, 0.778150, -1.263638}, new double[]{0.32297, -0.09405, -1.30342}, new double[]{0.32297, -0.14158, -1.38629}, new double[]{306.2849, 1.338076, 0.322970}, new double[]{301.6248, 1.484014, 0.322970}, new int[]{0, 0, 255}),
                new Val(Color.sRGB(0.0, 1.0, 0.0), new double[]{120.0, 1.0, 0.50}, new double[]{120.0, 1.0, 1.0}, "#00ff00", new double[]{0.357576, 0.715152, 0.119192}, new double[]{0.300000, 0.600000, 0.715152}, new double[]{0.877347, -0.861827, 0.831793}, new double[]{0.877347, -0.879067, 0.739170}, new double[]{0.87735, -0.83078, 1.07398}, new double[]{0.87735, -0.95989, 0.84887}, new double[]{136.0160, 1.197759, 0.877347}, new double[]{139.9409, 1.148534, 0.877347}, new int[]{0, 255, 0}),
                new Val(Color.sRGB(1.0, 0.0, 0.0), new double[]{0.0, 1.0, 0.50}, new double[]{0.0, 1.0, 1.0}, "#ff0000", new double[]{0.412456, 0.212673, 0.019334}, new double[]{0.640000, 0.330000, 0.212673}, new double[]{0.532408, 0.800925, 0.672032}, new double[]{0.532408, 0.782845, 0.621518}, new double[]{0.53241, 1.75015, 0.37756}, new double[]{0.53241, 1.67180, 0.24096}, new double[]{39.9990, 1.045518, 0.532408}, new double[]{38.4469, 0.999566, 0.532408}, new int[]{255, 0, 0}),
                new Val(Color.sRGB(0.0, 0.0, 0.0), new double[]{0.0, 0.0, 0.00}, new double[]{0.0, 0.0, 0.0}, "#000000", new double[]{0.000000, 0.000000, 0.000000}, new double[]{0.312727, 0.329023, 0.000000}, new double[]{0.000000, 0.000000, 0.000000}, new double[]{0.000000, 0.000000, 0.000000}, new double[]{0.00000, 0.00000, 0.00000}, new double[]{0.00000, 0.00000, 0.00000}, new double[]{0.0000, 0.000000, 0.000000}, new double[]{0.0000, 0.000000, 0.000000}, new int[]{0, 0, 0})
        );
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testRGB255Conversion(Val val) {
        assertEquals(
                val.c.RGB255(),
                val.rgb255
        );
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHsvCreation(Val val) {
        assertTrue(
                Color.HSV(val.hsv).almostEqualRgb(val.c)
        );
    }

    // Checks whether the relative error is below eps
    private static boolean almosteq_eps(double v1, double v2, double eps) {
        if (Math.abs(v1) > delta) {
            return Math.abs((v1 - v2) / v1) < eps;
        }
        return true;
    }

    private static boolean almosteq(double v1, double v2) {
        return almosteq_eps(v1, v2, delta);
    }

    static final double delta = 1.0 / 256.0;

    @ParameterizedTest
    @MethodSource("vals")
    public void testHsvConversion(Val tt) {
        var hsv = tt.c.HSV();
        assertEquals(hsv.H(), tt.hsv.H(), delta);
        assertEquals(hsv.S(), tt.hsv.S(), delta);
        assertEquals(hsv.V(), tt.hsv.V(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHslCreation(Val tt) {
        var c = Color.HSL(tt.hsl);
        assertTrue(c.almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHslConversion(Val tt) {
        var hsv = tt.c.HSL();
        assertEquals(hsv.H(), tt.hsl.H(), delta);
        assertEquals(hsv.S(), tt.hsl.S(), delta);
        assertEquals(hsv.L(), tt.hsl.L(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHexCreation(Val tt) {
        var c = Color.hex(tt.hex);
        assertTrue(c.almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHEXCreation(Val tt) {
        var c = Color.hex(tt.hex.toUpperCase());
        assertTrue(c.almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHexConversion(Val tt) {
        assertEquals(tt.c.hex(), tt.hex);
    }

    // LinearRgb itself is implicitly tested by XYZ conversions below (they use it).
    // So what we do here is just test that the FastLinearRgb approximation is "good enough"
    @Test
    public void testFastLinearRgb() {
        var eps = 6.0 / 255.0; // We want that "within 6 RGB values total" is "good enough".

        for (double r = 0.0; r < 256.0; r++) {
            for (double g = 0.0; g < 256.0; g++) {
                for (double b = 0.0; b < 256.0; b++) {
                    var c = Color.sRGB(r / 255.0, g / 255.0, b / 255.0);
                    switch (c.LinearRGB()) {
                        case LinearRGB(var r_want, var g_want, var b_want) -> {
                            switch (c.fastLinearRGB()) {
                                case LinearRGB(var r_appr, var g_appr, var b_appr) -> {
                                    var dr = Math.abs(r_want - r_appr);
                                    var dg = Math.abs(g_want - g_appr);
                                    var db = Math.abs(b_want - b_appr);
                                    assertTrue(
                                            dr + dg + db <= eps
                                    );
                                }
                            }
                        }
                    }


                    var c_want = Color.LinearRGB(r / 255.0, g / 255.0, b / 255.0).sRGB();
                    var c_appr = Color.fastLinearRGB(r / 255.0, g / 255.0, b / 255.0).sRGB();
                    var dr = Math.abs(c_want.R() - c_appr.R());
                    var dg = Math.abs(c_want.G() - c_appr.G());
                    var db = Math.abs(c_want.B() - c_appr.B());
                    assertTrue(
                            dr + dg + db <= eps
                    );
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testXyzCreation(Val tt) {
        assertTrue(Color.XYZ(tt.xyz).almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testXyzConversion(Val tt) {
        var xyz = tt.c.XYZ();
        assertEquals(xyz.X(), tt.xyz.X(), delta);
        assertEquals(xyz.Y(), tt.xyz.Y(), delta);
        assertEquals(xyz.Z(), tt.xyz.Z(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testXyyCreation(Val tt) {
        assertTrue(Color.xyY(tt.xyY).almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testXyyConversion(Val tt) {
        var xyY = tt.c.xyY();

        assertEquals(xyY.x(), tt.xyY.x(), delta);
        assertEquals(xyY.y(), tt.xyY.y(), delta);
        assertEquals(xyY.Y(), tt.xyY.Y(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testLabCreation(Val tt) {
        assertTrue(Color.Lab(tt.lab).almostEqualRgb(tt.c));
        assertTrue(Color.Lab(tt.lab50, ReferenceWhite.D50).almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testLabConversion(Val tt) {
        var Lab = tt.c.Lab();

        assertEquals(Lab.L(), tt.lab.L(), delta);
        assertEquals(Lab.a(), tt.lab.a(), delta);
        assertEquals(Lab.b(), tt.lab.b(), delta);

        Lab = tt.c.Lab(ReferenceWhite.D50);

        assertEquals(Lab.L(), tt.lab50.L(), delta);
        assertEquals(Lab.a(), tt.lab50.a(), delta);
        assertEquals(Lab.b(), tt.lab50.b(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testLuvCreation(Val tt) {
        assertTrue(Color.Luv(tt.luv).almostEqualRgb(tt.c));
        assertTrue(Color.Luv(tt.luv50, ReferenceWhite.D50).almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testLuvConversion(Val tt) {
        var Luv = tt.c.Luv();

        assertEquals(Luv.L(), tt.luv.L(), delta);
        assertEquals(Luv.u(), tt.luv.u(), delta);
        assertEquals(Luv.v(), tt.luv.v(), delta);

        Luv = tt.c.Luv(ReferenceWhite.D50);

        assertEquals(Luv.L(), tt.luv50.L(), delta);
        assertEquals(Luv.u(), tt.luv50.u(), delta);
        assertEquals(Luv.v(), tt.luv50.v(), delta);
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHclCreation(Val tt) {
        assertTrue(Color.HCL(tt.hcl).almostEqualRgb(tt.c));
        assertTrue(Color.HCL(tt.hcl50, ReferenceWhite.D50).almostEqualRgb(tt.c));
    }

    @ParameterizedTest
    @MethodSource("vals")
    public void testHclConversion(Val tt) {
        var HCL = tt.c.HCL();
        assertTrue(almosteq(HCL.H(), tt.hcl.H()));
        assertTrue(almosteq(HCL.C(), tt.hcl.C()));
        assertTrue(almosteq(HCL.L(), tt.hcl.L()));
        HCL = tt.c.HCL(ReferenceWhite.D50);
        assertTrue(almosteq(HCL.H(), tt.hcl50.H()));
        assertTrue(almosteq(HCL.C(), tt.hcl50.C()));
        assertTrue(almosteq(HCL.L(), tt.hcl50.L()));
    }

    public record RGBLab(double R, double G, double B, double L, double a, double b) {
    }

    public static List<RGBLab> rgbLabs() {
        return List.of(
                new RGBLab(1, 1, 1, 1.000, 0.000, 0.000),            // white
                new RGBLab(1, 0, 0, 0.627955, 0.224863, 0.125846),   // red
                new RGBLab(0, 1, 0, 0.86644, -0.233888, 0.179498),   // lime
                new RGBLab(0, 0, 1, 0.452014, -0.032457, -0.311528), // blue
                new RGBLab(0, 1, 1, 0.905399, -0.149444, -0.039398), // cyan
                new RGBLab(1, 0, 1, 0.701674, 0.274566, -0.169156),  // magenta
                new RGBLab(1, 1, 0, 0.967983, -0.071369, 0.198570),  // yellow
                new RGBLab(0, 0, 0, 0.000000, 0.000000, 0.000000)   // black
        );
    }

    @ParameterizedTest
    @MethodSource("rgbLabs")
    public void testRgbToOkLab(RGBLab rgbLab) {
        var lab = Color.LinearRGB(rgbLab.R, rgbLab.G, rgbLab.B).XYZ()
                .OkLab();
        assertTrue(almosteq(lab.L(), rgbLab.L));
        assertTrue(almosteq(lab.a(), rgbLab.a));
        assertTrue(almosteq(lab.b(), rgbLab.b));
    }

    public record XYZOkLab(
            double X, double Y, double Z,
            double L, double a, double b
    ) {}

    // https://bottosson.github.io/posts/oklab/#table-of-example-xyz-and-oklab-pairs
    public static List<XYZOkLab> xyzOkLabs() {
        return List.of(
                new XYZOkLab(0.950, 1.000, 1.089, 1.000, 0.000, 0.000),
                new XYZOkLab(1.000, 0.000, 0.000, 0.450, 1.236, -0.019),
                new XYZOkLab(0.000, 1.000, 0.000, 0.922, -0.671, 0.263),
                new XYZOkLab(0.000, 0.000, 1.000, 0.153, -1.415, -0.449)
        );
    }

    @ParameterizedTest
    @MethodSource("xyzOkLabs")
    public void testXYZToOkLab(XYZOkLab xyzOkLab) {
        var lab = new XYZ(xyzOkLab.X, xyzOkLab.Y, xyzOkLab.Z).OkLab();
        assertTrue(almosteq(lab.L(), xyzOkLab.L));
        assertTrue(almosteq(lab.a(), xyzOkLab.a));
        assertTrue(almosteq(lab.b(), xyzOkLab.b));
    }

    @ParameterizedTest
    @MethodSource("xyzOkLabs")
    public void testOkLabToXYZ(XYZOkLab xyzOkLab) {
        var xyz = new OkLab(xyzOkLab.L, xyzOkLab.a, xyzOkLab.b).XYZ();
        assertTrue(almosteq(xyz.X(), xyzOkLab.X));
        assertTrue(almosteq(xyz.Y(), xyzOkLab.Y));
        assertTrue(almosteq(xyz.Z(), xyzOkLab.Z));
    }

    public record OkPair(OkLab lab, OkLch lch) {
    }

    public static List<OkPair> okPairs() {
        return List.of(
                new OkPair(
                        new OkLab(55.0, 0.17, -0.14),
                        new OkLch(55.0, 0.22, 320.528)
                ),

                new OkPair(
                        new OkLab(90.0, 0.32, 0.00),
                        new OkLch(90.0, 0.32, 0.0)
                ),

                new OkPair(
                        new OkLab(10.0, 0.00, -0.40),
                        new OkLch(10.0, 0.40, 270.0)
                )
        );
    }


    @ParameterizedTest
    @MethodSource("okPairs")
    public void testOkLabToOkLch(OkPair pair) {
        var lch = pair.lab.OkLch();
        assertTrue(almosteq(lch.L(), pair.lch.L()));
        assertTrue(almosteq(lch.c(), pair.lch.c()));
        assertTrue(almosteq(lch.h(), pair.lch.h()));
    }

    @ParameterizedTest
    @MethodSource("okPairs")
    public void testOkLchToOkLab(OkPair pair) {
        var lab = pair.lch.OkLab();
        assertTrue(almosteq(lab.L(), pair.lab.L()));
        assertTrue(almosteq(lab.a(), pair.lab.a()));
        assertTrue(almosteq(lab.b(), pair.lab.b()));
    }

    public record Distances(
            Color c1,
            Color c2,
            double d76,
            double d94,
            double d00
    ) {}

    public static List<Distances> distancesList() {
        return List.of(
                new Distances(Color.sRGB(1.0, 1.0, 1.0), Color.sRGB(1.0, 1.0, 1.0), 0.0, 0.0, 0.0),
                new Distances(Color.sRGB(0.0, 0.0, 0.0), Color.sRGB(0.0, 0.0, 0.0), 0.0, 0.0, 0.0),

                // Just pairs of values of the table way above.
                new Distances(Color.Lab(1.000000, 0.000000, 0.000000), Color.Lab(0.931390, -0.353319, -0.108946), 0.37604638, 0.37604638, 0.23528129),
                new Distances(Color.Lab(0.720892, 0.651673, -0.422133), Color.Lab(0.977637, -0.165795, 0.602017), 1.33531088, 0.65466377, 0.75175896),
                new Distances(Color.Lab(0.590453, 0.332846, -0.637099), Color.Lab(0.681085, 0.483884, 0.228328), 0.88317072, 0.42541075, 0.37688153),
                new Distances(Color.Lab(0.906026, -0.600870, 0.498993), Color.Lab(0.533890, 0.000000, 0.000000), 0.86517280, 0.41038323, 0.39960503),
                new Distances(Color.Lab(0.911132, -0.480875, -0.141312), Color.Lab(0.603242, 0.982343, -0.608249), 1.56647162, 0.87431457, 0.57983482),
                new Distances(Color.Lab(0.971393, -0.215537, 0.944780), Color.Lab(0.322970, 0.791875, -1.078602), 2.35146891, 1.11858192, 1.03426977),
                new Distances(Color.Lab(0.877347, -0.861827, 0.831793), Color.Lab(0.532408, 0.800925, 0.672032), 1.70565338, 0.68800270, 0.86608245)
        );
    }

    @ParameterizedTest
    @MethodSource("distancesList")
    public void testDistances(Distances tt) {
        assertTrue(almosteq(tt.c1.distanceCIE76(tt.c2), tt.d76));
        assertTrue(almosteq(tt.c1.distanceCIE94(tt.c2), tt.d94));
        assertTrue(almosteq(tt.c1.distanceCIEDE2000(tt.c2), tt.d00));
    }
    // Issues raised on github ///
    ///////////////////////////////

    // https://github.com/lucasb-eyer/go-colorful/issues/11
    @Test
    public void TestIssue11() {
        var c1hex = "#1a1a46";
        var c2hex = "#666666";

        var c1 = Color.hex(c1hex);
        var c2 = Color.hex(c2hex);

        assertEquals(c1.blendHSV(c2, 0).hex(), c1hex);
        assertEquals(c1.blendHSV(c2, 1).hex(), c2hex);

        assertEquals(c1.blendLuv(c2, 0).hex(), c1hex);
        assertEquals(c1.blendLuv(c2, 1).hex(), c2hex);


        assertEquals(c1.blendRGB(c2, 0).hex(), c1hex);
        assertEquals(c1.blendRGB(c2, 1).hex(), c2hex);

        assertEquals(c1.blendLinearRGB(c2, 0).hex(), c1hex);
        assertEquals(c1.blendLinearRGB(c2, 1).hex(), c2hex);


        assertEquals(c1.blendLab(c2, 0).hex(), c1hex);
        assertEquals(c1.blendLab(c2, 1).hex(), c2hex);

        assertEquals(c1.blendHCL(c2, 0).hex(), c1hex);
        assertEquals(c1.blendHCL(c2, 1).hex(), c2hex);

        assertEquals(c1.blendLuvLCh(c2, 0).hex(), c1hex);
        assertEquals(c1.blendLuvLCh(c2, 1).hex(), c2hex);
    }
}
