package dev.mccue.color.test;

import dev.mccue.color.*;
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
    ) {}

    static List<Val> vals() { return List.of(
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
            )
    ); }

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

    @ParameterizedTest
    @MethodSource("vals")
    public void testHsvConversion(Val tt) {
        var hsv = tt.c.HSV();

    }
}
