package dev.mccue.color;

final class OkLabColorSpace implements ColorSpace<OkLab> {
    private OkLabColorSpace() {}

    static final OkLabColorSpace INSTANCE = new OkLabColorSpace();

    /// @see <a href="https://bottosson.github.io/posts/oklab/#converting-from-linear-srgb-to-oklab">https://bottosson.github.io/posts/oklab/#converting-from-linear-srgb-to-oklab</a>
    @Override
    public OkLab fromColor(Color c) {
        double l = 0.4122214708f * c.r + 0.5363325363f * c.g + 0.0514459929f * c.b;
        double m = 0.2119034982f * c.r + 0.6806995451f * c.g + 0.1073969566f * c.b;
        double s = 0.0883024619f * c.r + 0.2817188376f * c.g + 0.6299787005f * c.b;

        double l_ = Math.cbrt(l);
        double m_ = Math.cbrt(m);
        double s_ = Math.cbrt(s);

        return new OkLab(
                0.2104542553f*l_ + 0.7936177850f*m_ - 0.0040720468f*s_,
                1.9779984951f*l_ - 2.4285922050f*m_ + 0.4505937099f*s_,
                0.0259040371f*l_ + 0.7827717662f*m_ - 0.8086757660f*s_
        );
    }

    @Override
    public Color toColor(OkLab okLab) {
        double l_ = okLab.L() + 0.3963377774f * okLab.a() + 0.2158037573f * okLab.b();
        double m_ = okLab.L() - 0.1055613458f * okLab.a() - 0.0638541728f * okLab.b();
        double s_ = okLab.L() - 0.0894841775f * okLab.a() - 1.2914855480f * okLab.b();

        double l = l_*l_*l_;
        double m = m_*m_*m_;
        double s = s_*s_*s_;

        return new Color(
                +4.0767416621f * l - 3.3077115913f * m + 0.2309699292f * s,
                -1.2684380046f * l + 2.6097574011f * m - 0.3413193965f * s,
                -0.0041960863f * l - 0.7034186147f * m + 1.7076147010f * s
        );
    }
}
