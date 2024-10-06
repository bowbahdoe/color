package dev.mccue.color;

public record HPLuv(
        double H,
        double S,
        double L
) {
    LuvLCh LuvLCh() {
        // [-1..1] but the code expects it to be [-100..100]
        var l = L * 100.0;
        var s = S * 100.0;

        double c;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            c = 0.0;
        } else {
            max = Color.maxSafeChromaForL(l);
            c = max / 100.0 * s;
        }
        return new LuvLCh(l / 100.0, c / 100.0, H);
    }
}
