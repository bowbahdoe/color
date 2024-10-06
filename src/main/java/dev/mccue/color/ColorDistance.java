package dev.mccue.color;

/// Computes the distance between two colors in some color
/// space.
///
/// @see Color#distanceCIEDE2000(Color)
/// @see Color#distanceCIEDE2000klch(Color, double, double, double)
/// @see Color#distanceLab(Color)
/// @see Color#distanceCIE76(Color)
/// @see Color#distanceCIE94(Color)
/// @see Color#distanceLinearRGB(Color)
/// @see Color#distanceLuv(Color)
/// @see Color#distanceRgb(Color)
/// @see Color#distanceRiemersma(Color)
/// @see Color#distanceHPLuv(Color)
/// @see Color#distanceHSLuv(Color)
@FunctionalInterface
public interface ColorDistance {
    /// Computes the distance between two colors.
    ///
    /// @param c1 The first color
    /// @param c2 The second color
    /// @return The distance between `c1` and `c2`
    double distance(Color c1, Color c2);
}
