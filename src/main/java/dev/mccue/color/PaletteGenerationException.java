package dev.mccue.color;

import java.io.Serial;

public final class PaletteGenerationException
        extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PaletteGenerationException() {}

    public PaletteGenerationException(Throwable t) {
        super(t);
    }

    public PaletteGenerationException(String message) {
        super(message);
    }

    public PaletteGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
