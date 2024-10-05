package dev.mccue.color;

public record sRGB(
        double R,
        double G,
        double B
) {
    public sRGB(
            double R,
            double G,
            double B
    ) {
        this.R = Math.clamp(R, 0, 1);
        this.G = Math.clamp(G, 0, 1);
        this.B = Math.clamp(B, 0, 1);
    }
}
