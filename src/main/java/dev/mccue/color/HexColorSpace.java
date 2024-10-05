package dev.mccue.color;

final class HexColorSpace implements ColorSpace<String> {
    static final HexColorSpace INSTANCE = new HexColorSpace();

    private HexColorSpace() {}

    @Override
    public String fromColor(Color c) {
        return "#%02x%02x%02x".formatted(
                (int) Math.clamp(c.r*255.0+0.5, 0, 255),
                (int) Math.clamp(c.g*255.0+0.5, 0, 255),
                (int) Math.clamp(c.b*255.0+0.5, 0, 255)
        );
    }

    /// Parses a "html" hex color-string, either in the 3 "#f0c" or 6 "#ff1034" digits form.
    @Override
    public Color toColor(String s) {
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

        return new Color(((double) r) * factor, ((double) g) * factor, ((double) b) * factor);
    }
}
