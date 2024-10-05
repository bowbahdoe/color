package dev.mccue.color;

final class HCLColorSpace implements ColorSpace<HCL> {
    private HCLColorSpace() {}

    static final HCLColorSpace INSTANCE = new HCLColorSpace();

    @Override
    public HCL fromColor(Color c) {
        return null;
    }

    @Override
    public Color toColor(HCL hcl) {
        return null;
    }
}
