package dev.mccue.color;

final class OkLabColorSpace implements ColorSpace<OkLab> {
    private OkLabColorSpace() {}

    static final OkLabColorSpace INSTANCE = new OkLabColorSpace();

    /// @see <a href="https://bottosson.github.io/posts/oklab/#converting-from-linear-srgb-to-oklab">https://bottosson.github.io/posts/oklab/#converting-from-linear-srgb-to-oklab</a>
    @Override
    public OkLab fromColor(Color c) {
        return c.XYZ().OkLab();
    }

    @Override
    public Color toColor(OkLab okLab) {
        return Color.XYZ(okLab.XYZ());
    }
}
