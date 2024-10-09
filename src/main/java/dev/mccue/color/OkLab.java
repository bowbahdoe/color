package dev.mccue.color;

/// Components of a color in the OkLab color space.
///
/// @see <a href="https://bottosson.github.io/posts/oklab/">https://bottosson.github.io/posts/oklab/</a>
public record OkLab(
        /// Perceived lightness
        double L,
        /// How green/red the color is
        double a,
        /// How blue/yellow the color is
        double b
) implements Color {

    @Override
    public OkLab OkLab() {
        return this;
    }

    @Override
    public XYZ XYZ() {
        var l = L;
        var l_ = 0.9999999984505196*l + 0.39633779217376774*a + 0.2158037580607588*b;
        var m_ = 1.0000000088817607*l - 0.10556134232365633*a - 0.0638541747717059*b;
        var s_ = 1.0000000546724108*l - 0.08948418209496574*a - 1.2914855378640917*b;

        var ll = Math.pow(l_, 3);
        var m = Math.pow(m_, 3);
        var s = Math.pow(s_, 3);

        var x = 1.2268798733741557*ll - 0.5578149965554813*m + 0.28139105017721594*s;
        var y = -0.04057576262431372*ll + 1.1122868293970594*m - 0.07171106666151696*s;
        var z = -0.07637294974672142*ll - 0.4214933239627916*m + 1.5869240244272422*s;

        return new XYZ(x, y, z);
    }

    @Override
    public OkLch OkLch() {
        var c = Math.sqrt((a * a) + (b * b));
        var h = Math.atan2(b, a);
        if (h < 0) {
            h += 2 * Math.PI;
        }

        return new OkLch(L, c, h * 180 / Math.PI);
    }

    @Override
    public sRGB sRGB() {
        return XYZ().sRGB();
    }
}
