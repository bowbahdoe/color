package dev.mccue.color;

final class LinearRGBColorSpace implements ColorSpace<LinearRGB> {
    static final LinearRGBColorSpace INSTANCE = new LinearRGBColorSpace();

    private LinearRGBColorSpace() {}

    static double linearize(double v) {
        if (v <= 0.04045) {
            return v / 12.92;
        }
        return Math.pow((v+0.055)/1.055, 2.4);
    }

    @Override
    public LinearRGB fromColor(Color c) {
        var r = linearize(c.r);
        var g = linearize(c.g);
        var b = linearize(c.b);
        return new LinearRGB(r, g, b);
    }


    static double delinearize(double v) {
        if (v <= 0.0031308) {
            return 12.92 * v;
        }
        return 1.055*Math.pow(v, 1.0/2.4) - 0.055;
    }

    @Override
    public Color toColor(LinearRGB linearRGB) {
        return new Color(
                delinearize(linearRGB.R()),
                delinearize(linearRGB.G()),
                delinearize(linearRGB.B())
        );
    }
}
