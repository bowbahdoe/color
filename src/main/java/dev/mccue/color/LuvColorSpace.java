package dev.mccue.color;

final class LuvColorSpace implements ColorSpace<Luv> {
    private LuvColorSpace() {}

    static final LuvColorSpace INSTANCE = new LuvColorSpace();

    // Converts the given color to CIE L*u*v* space using D65 as reference white.
    // L* is in [0..1] and both u* and v* are in about [-1..1]

    @Override
    public Luv fromColor(Color c) {
        return c.XYZ().Luv();
    }

    // Generates a color by using data given in CIE L*u*v* space using D65 as reference white.
    // L* is in [0..1] and both u* and v* are in about [-1..1]
    // WARNING: many combinations of `l`, `u`, and `v` values do not have corresponding
    // valid RGB values, check the FAQ in the README if you're unsure.
    @Override
    public Color toColor(Luv luv) {
        return Color.XYZ(luv.XYZ());
    }
}
