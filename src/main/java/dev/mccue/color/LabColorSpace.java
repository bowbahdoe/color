package dev.mccue.color;

final class LabColorSpace implements ColorSpace<Lab> {
    private LabColorSpace() {}

    static final LabColorSpace INSTANCE = new LabColorSpace();

    @Override
    public Lab fromColor(Color c) {
        return c.toComponents(ColorSpace.XYZ()).Lab();
    }

    @Override
    public Color toColor(Lab lab) {
        return Color.fromComponents(ColorSpace.XYZ(), lab.XYZ());
    }
}
