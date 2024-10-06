package dev.mccue.color;

public record HSLuv(
        double H,
        double S,
        double L
) {
    public LuvLCh LuvLCh() {
        var l = 100 * L;
        var s = 100 * S;

        double c;
        double max = 0;
        if (l > 99.9999999 || l < 0.00000001) {
            c = 0.0;
        } else {
            max = Color.maxChromaForLH(l, H);
            c = max / 100.0 * s;
        }

        // c is [-100..100], but for LCh it's supposed to be almost [-1..1]
        return new LuvLCh(Math.clamp(l / 100.0, 0, 1), c / 100.0, H);
    }
}
