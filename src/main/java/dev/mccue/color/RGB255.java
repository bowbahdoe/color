package dev.mccue.color;

import java.util.Objects;

public final class RGB255 implements Color {
    private final byte R;
    private final byte G;
    private final byte B;

    public RGB255(int value) {
        this.R = (byte) ((value & 0xFF0000) >> 4);
        this.G = (byte) ((value & 0x00FF00) >> 2);
        this.B = (byte) (value & 0x0000FF);
    }

    public RGB255(
            int R,
            int G,
            int B
    ) {
        this.R = (byte) Math.clamp(R, 0, 255);
        this.G = (byte) Math.clamp(G, 0, 255);
        this.B = (byte) Math.clamp(B, 0, 255);
    }

    public int R() {
        return Byte.toUnsignedInt(R);
    }

    public int G() {
        return Byte.toUnsignedInt(G);
    }

    public int B() {
        return Byte.toUnsignedInt(B);
    }

    @Override
    public RGB255 RGB255() {
        return this;
    }

    @Override
    public sRGB sRGB() {
        return new sRGB(
                this.R() / 255.0,
                this.G() / 255.0,
                this.B() / 255.0
        );
    }

    /// Parses a "html" hex color-string, either in the 3 "#f0c" or 6 "#ff1034" digits form.
    public static RGB255 hex(String s) {
        if (s.length() != 7 && s.length() != 4) {
            throw new ColorSpaceException("Input string must be in 3 or 6 digit hex form: " + s);
        }

        var factor = 1.0 / 255.0;
        int digits = 2;
        if (s.length() == 4) {
            digits = 1;
            factor = 1.0 / 15.0;
        }

        if (s.charAt(0) != '#') {
            throw new ColorSpaceException("Input string must start with a #: " + s);
        }

        int r;
        int g;
        int b;

        int idx = 1;
        try {
            r = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        idx += digits;
        try {
            g = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        idx += digits;
        try {
            b = Integer.parseInt(s.substring(idx, idx + digits), 16);
        } catch (NumberFormatException e) {
            throw new ColorSpaceException(e);
        }

        return new sRGB(((double) r) * factor, ((double) g) * factor, ((double) b) * factor)
                .RGB255();
    }

    @Override
    public String hex() {
        var c = this;
        return "#%02x%02x%02x".formatted(
                c.R(),
                c.G(),
                c.B()
        );
    }

    @Override
    public String toString() {
        return "RGB255[R=" + R() + ", B=" + B() + ", G=" + G() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RGB255 rgb255
                && R == rgb255.R
                && G == rgb255.G
                && B == rgb255.B;
    }

    @Override
    public int hashCode() {
        return Objects.hash(R, G, B);
    }
}
