package dev.mccue.color;

final class OkLchColorSpace implements ColorSpace<OkLch> {
    static final OkLchColorSpace INSTANCE = new OkLchColorSpace();

    private OkLchColorSpace() {}

    @Override
    public OkLch fromColor(Color c) {
        return c.OkLab().OkLch();
    }

    @Override
    public Color toColor(OkLch okLch) {
        return Color.XYZ(okLch.XYZ());
    }
}
