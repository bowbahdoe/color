package dev.mccue.color;

import java.io.Serial;

/// Exception to throw when converting a color to or from a color
/// space is impossible.
public final class ColorSpaceException
        extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ColorSpaceException() {}

    public ColorSpaceException(Throwable t) {
        super(t);
    }

    public ColorSpaceException(String message) {
        super(message);
    }

    public ColorSpaceException(String message, Throwable cause) {
        super(message, cause);
    }
}
