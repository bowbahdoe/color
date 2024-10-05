package dev.mccue.color;

final class RGB255ColorSpace implements ColorSpace<RGB255> {
    static final RGB255ColorSpace INSTANCE = new RGB255ColorSpace();

    private RGB255ColorSpace() {}

    @Override
    public RGB255 fromColor(Color c) {
        int r = (int) (c.r*255.0 + 0.5);
        int g = (int) (c.g*255.0 + 0.5);
        int b = (int) (c.b*255.0 + 0.5);
        return new RGB255(r, g, b);
    }

    @Override
    public Color toColor(RGB255 rgb255) {
        return Color.sRGB(
                rgb255.R() / 255.0,
                rgb255.G() / 255.0,
                rgb255.B() / 255.0
        );
    }
}
