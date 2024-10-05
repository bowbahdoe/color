package dev.mccue.color.test;

import dev.mccue.color.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HexTest {
    @Test
    public void longHexValues() {
        assertEquals(Color.sRGB(0, 0, 0), Color.hex("#000000"));
        assertEquals(Color.sRGB(1, 0, 0), Color.hex("#FF0000"));
        assertEquals(Color.sRGB(0, 1, 0), Color.hex("#00FF00"));
        assertEquals(Color.sRGB(0, 0, 1), Color.hex("#0000FF"));
        assertEquals(Color.sRGB(1, 1, 1), Color.hex("#FFFFFF"));
    }

    @Test
    public void shortHexValues() {
        assertEquals(
                Color.sRGB(1.0, 1.0, 1.0),
                Color.hex("#fff")
        );

        assertEquals(
                Color.sRGB(0.6, 1.0, 1.0),
                Color.hex("#9ff")
        );

        assertEquals(
                Color.sRGB(1.0, 0.6, 1.0),
                Color.hex("#f9f")
        );

        assertEquals(
                Color.sRGB(1.0, 1.0, 0.6),
                Color.hex("#ff9")
        );

        assertEquals(
                Color.sRGB(0.6, 0.6, 1.0),
                Color.hex("#99f")
        );

        assertEquals(
                Color.sRGB(1, 0.6, 0.6),
                Color.hex("#f99")
        );

        assertEquals(
                Color.sRGB(0.6, 1, 0.6),
                Color.hex("#9f9")
        );

        assertEquals(
                Color.sRGB(0.6, 0.6, 0.6),
                Color.hex("#999")
        );

        assertEquals(
                Color.sRGB(0, 1, 1),
                Color.hex("#0ff")
        );

        assertEquals(
                Color.sRGB(1, 0, 1),
                Color.hex("#f0f")
        );

        assertEquals(
                Color.sRGB(1, 1, 0),
                Color.hex("#ff0")
        );

        assertEquals(
                Color.sRGB(0, 0, 1),
                Color.hex("#00f")
        );

        assertEquals(
                Color.sRGB(0, 1, 0),
                Color.hex("#0f0")
        );

        assertEquals(
                Color.sRGB(1, 0, 0),
                Color.hex("#f00")
        );

        assertEquals(
                Color.sRGB(0, 0, 0),
                Color.hex("#000")
        );
    }
}
