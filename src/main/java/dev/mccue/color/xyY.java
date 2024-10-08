package dev.mccue.color;


// http://www.brucelindbloom.com/Eqn_XYZ_to_xyY.html
/// The CIE xyY color system.
public record xyY(
        double x,
        double y,
        double Y
) implements Color {
    @Override
    public xyY xyY() {
        return this;
    }

    @Override
    public XYZ XYZ() {
        double X;
        double Z;
        if (-1e-14 < y && y < 1e-14) {
            X = 0.0;
            Z = 0.0;
        } else {
            X = Y / y * x;
            Z = Y / y * (1.0 - x - y);
        }

        return new XYZ(X, Y, Z);
    }

    // Generates a color by using data given in CIE xyY space.
    // x, y and Y are in [0..1]
    @Override
    public sRGB sRGB() {
        return XYZ().sRGB();
    }
}
