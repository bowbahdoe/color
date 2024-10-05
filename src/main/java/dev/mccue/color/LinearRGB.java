package dev.mccue.color;

public record LinearRGB(
        double R,
        double G,
        double B
) {
    public LinearRGB(
            double R,
            double G,
            double B
    ) {
        this.R = Math.clamp(R, 0, 1);
        this.G = Math.clamp(G, 0, 1);
        this.B = Math.clamp(B, 0, 1);
    }

    XYZ XYZ() {
        var x = 0.41239079926595948*R + 0.35758433938387796*G + 0.18048078840183429*B;
        var y = 0.21263900587151036*R + 0.71516867876775593*G + 0.072192315360733715*B;
        var z = 0.019330818715591851*R + 0.11919477979462599*G + 0.95053215224966058*B;
        return new XYZ(x, y, z);
    }
}
