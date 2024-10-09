package dev.mccue.color;

import static dev.mccue.color.Util.sq;

/// @see <a href="https://www.sjbrown.co.uk/2004/05/14/gamma-correct-rendering/">https://www.sjbrown.co.uk/2004/05/14/gamma-correct-rendering/</a>
/// @see <a href="http://www.brucelindbloom.com/Eqn_RGB_to_XYZ.html">https://www.brucelindbloom.com/Eqn_RGB_to_XYZ.html</a>
public record LinearRGB(
        double R,
        double G,
        double B
) implements Color {
    @Override
    public LinearRGB LinearRGB() {
        return this;
    }

    @Override
    public XYZ XYZ() {
        var x = 0.41239079926595948*R + 0.35758433938387796*G + 0.18048078840183429*B;
        var y = 0.21263900587151036*R + 0.71516867876775593*G + 0.072192315360733715*B;
        var z = 0.019330818715591851*R + 0.11919477979462599*G + 0.95053215224966058*B;
        return new XYZ(x, y, z);
    }

    static double delinearize(double v) {
        if (v <= 0.0031308) {
            return 12.92 * v;
        }
        return 1.055*Math.pow(v, 1.0/2.4) - 0.055;
    }

    @Override
    public sRGB sRGB() {
        return new sRGB(
                delinearize(this.R()),
                delinearize(this.G()),
                delinearize(this.B())
        );
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

    public sRGB sRGB_fast() {
        return new sRGB(
                delinearize_fast(R()),
                delinearize_fast(G()),
                delinearize_fast(B())
        );
    }

    public double distance(LinearRGB c2) {
        var c1 = this;
        var r1 = c1.R;
        var g1 = c1.G;
        var b1 = c1.B;
        var r2 = c2.R;
        var g2 = c2.G;
        var b2 = c2.B;
        return Math.sqrt(sq(r1 - r2) + sq(g1 - g2) + sq(b1 - b2));
    }
}
