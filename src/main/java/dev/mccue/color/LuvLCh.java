package dev.mccue.color;

public record LuvLCh(double L, double C, double h) {
    public HSLuv HSLuv() {
        // [-1..1] but the code expects it to be [-100..100]
        var c = C * 100.0;
        var l = L * 100.0;

        double s;
        double max;
        if (l > 99.9999999 || l < 0.00000001) {
            s = 0.0;
        } else {
            max = Color.maxChromaForLH(l, h);
            s = c / max * 100.0;
        }
        return new HSLuv(
                h, Math.clamp(s / 100.0, 0, 1), Math.clamp(l / 100.0, 0, 1)
        );
    }

    public Luv Luv() {
        double L;
        double u;
        double v;

        var H = 0.01745329251994329576 * h; // Deg2Rad
        u = this.C * Math.cos(H);
        v = this.C * Math.sin(H);
        L = this.L;
        return new Luv(L, u, v);
    }

    public HPLuv HPLuv() {
        var c = C;
        var l = L;
        // [-1..1] but the code expects it to be [-100..100]
        c *= 100.0;
        l *= 100.0;

        double s;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            s = 0.0;
        } else {
            max = Color.maxSafeChromaForL(l);
            s = c / max * 100.0;
        }
        return new HPLuv(h, s / 100.0, l / 100.0);
    }
}
