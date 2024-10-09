package dev.mccue.color;

/// The CIE-Lab ({@link Lab}) color space in cylindrical coordinates.
public record LabLCh(
        double L,
        double C,
        double h
) implements Color {
    public LabLCh(
            double L,
            double C,
            double h
    ) {
        this.L = L;
        this.C = C;
        this.h = h % 360;
    }

    @Override
    public LabLCh LabLCh() {
        return this;
    }

    @Override
    public Lab Lab() {
        var H = 0.01745329251994329576 * this.h; // Deg2Rad
        double a = C * Math.cos(H);
        double b = C * Math.sin(H);
        double L = this.L;
        return new Lab(L, a, b);
    }

    @Override
    public sRGB sRGB() {
        return this.sRGB(ReferenceWhite.D65);
    }

    // Generates a color by using data given in HCL space, taking
    // into account a given reference white. (i.e. the monitor's white)
    // H values are in [0..360], C and L values are in [0..1]
    public sRGB sRGB(ReferenceWhite referenceWhite) {
        return this.Lab().XYZ(referenceWhite).sRGB();
    }
}
