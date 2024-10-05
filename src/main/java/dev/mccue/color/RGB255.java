package dev.mccue.color;

public record RGB255(
        int R,
        int G,
        int B
) {
    public RGB255(
            int R,
            int G,
            int B
    ) {
        this.R = Math.clamp(R, 0, 255);
        this.G = Math.clamp(G, 0, 255);
        this.B = Math.clamp(B, 0, 255);
    }
}
