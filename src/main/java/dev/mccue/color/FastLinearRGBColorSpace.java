package dev.mccue.color;

final class FastLinearRGBColorSpace implements ColorSpace<LinearRGB> {
    static final FastLinearRGBColorSpace INSTANCE = new FastLinearRGBColorSpace();

    private FastLinearRGBColorSpace() {}

    // A much faster and still quite precise linearization using a 6th-order Taylor approximation.
    // See the accompanying Jupyter notebook for derivation of the constants.
    private static double linearize_fast(double v) {
        var v1 = v - 0.5;
        var v2 = v1 * v1;
        var v3 = v2 * v1;
        var v4 = v2 * v2;
        // v5 := v3*v2
        return -0.248750514614486 + 0.925583310193438*v + 1.16740237321695*v2 + 0.280457026598666*v3 - 0.0757991963780179*v4; //+ 0.0437040411548932*v5
    }

    @Override
    public LinearRGB fromColor(Color c) {
        var r = linearize_fast(c.r);
        var g = linearize_fast(c.g);
        var b = linearize_fast(c.b);
        return new LinearRGB(r, g, b);
    }

    private static double delinearize_fast(double v) {
        // This function (fractional root) is much harder to linearize, so we need to split.
        if (v > 0.2) {
            var v1 = v - 0.6;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            return 0.442430344268235 + 0.592178981271708*v - 0.287864782562636*v2 + 0.253214392068985*v3 - 0.272557158129811*v4 + 0.325554383321718*v5;
        } else if (v > 0.03) {
            var v1 = v - 0.115;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            return 0.194915592891669 + 1.55227076330229*v - 3.93691860257828*v2 + 18.0679839248761*v3 - 101.468750302746*v4 + 632.341487393927*v5;
        } else {
            var v1 = v - 0.015;
            var v2 = v1 * v1;
            var v3 = v2 * v1;
            var v4 = v2 * v2;
            var v5 = v3 * v2;
            // You can clearly see from the involved constants that the low-end is highly nonlinear.
            return 0.0519565234928877 + 5.09316778537561*v - 99.0338180489702*v2 + 3484.52322764895*v3 - 150028.083412663*v4 + 7168008.42971613*v5;
        }
    }

    @Override
    public Color toColor(LinearRGB linearRGB) {
        return new Color(
                delinearize_fast(linearRGB.R()),
                delinearize_fast(linearRGB.G()),
                delinearize_fast(linearRGB.B())
        );
    }
}
