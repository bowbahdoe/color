package dev.mccue.color;

final class sRGBColorSpace implements ColorSpace<sRGB> {
    static final sRGBColorSpace INSTANCE = new sRGBColorSpace();

    private sRGBColorSpace() {}

    @Override
    public sRGB fromColor(Color c) {
        c = c.clamped();
        return new sRGB(c.r, c.g, c.b);
    }

    @Override
    public Color toColor(sRGB sRGB) {
        return new Color(sRGB.R(), sRGB.G(), sRGB.B());
    }
}
