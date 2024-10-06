package dev.mccue.color;


// http://www.brucelindbloom.com/Eqn_XYZ_to_xyY.html
public record xyY(
        double x,
        double y,
        double Y
) {
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
}
