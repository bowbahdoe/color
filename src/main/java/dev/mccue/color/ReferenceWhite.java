package dev.mccue.color;

public final class ReferenceWhite {
    final double _0;
    final double _1;
    final double _2;

    private ReferenceWhite(double _0, double _1, double _2) {
        this._0 = _0;
        this._1 = _1;
        this._2 = _2;
    }


    public static final ReferenceWhite D65
            = new ReferenceWhite(0.95047, 1.00000, 1.08883);
    public static final ReferenceWhite D50
            = new ReferenceWhite(0.96422, 1.00000, 0.82521);

    // HSLuv uses a rounded version of the D65. This has no impact on the final RGB
    // values, but to keep high levels of accuracy for internal operations and when
    // comparing to the test values, this modified white reference is used internally.
    //
    // See this GitHub thread for details on these values:
    //
    //	https://github.com/hsluv/hsluv/issues/79
    static final ReferenceWhite hSLuvD65 = new ReferenceWhite(0.95045592705167, 1.0, 1.089057750759878);
}
