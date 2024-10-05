package dev.mccue.color;

final class XYZColorSpace implements ColorSpace<XYZ> {
    static final XYZColorSpace INSTANCE
            = new XYZColorSpace();

    private XYZColorSpace() {}

    @Override
    public XYZ fromColor(Color c) {
        return c.toComponents(ColorSpace.linearRGB()).XYZ();
    }

    @Override
    public Color toColor(XYZ xyz) {
        return Color.fromComponents(ColorSpace.linearRGB(), xyz.LinearRGB());
    }
}
