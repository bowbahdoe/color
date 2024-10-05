package dev.mccue.color;

final class LuvColorSpace implements ColorSpace<Luv> {
    private LuvColorSpace() {}

    static final LuvColorSpace INSTANCE = new LuvColorSpace();

    @Override
    public Luv fromColor(Color c) {
        return null;
    }

    @Override
    public Color toColor(Luv luv) {
        return null;
    }
}
