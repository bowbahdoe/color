package dev.mccue.color.test;

import dev.mccue.color.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorSortTest {
    @Test
    public void testColorSort() {
        // Sort a list of reds and blues.
        var in = new ArrayList<Color>();
        for (int i = 0; i < 3; i++) {
            in.add(Color.sRGB(1.0 - (double) (i + 1) * 0.25, 0, 0)); // Reds
            in.add(Color.sRGB(0.0, 0.0, 1.0 - (double) (i + 1) * 0.25));
        }

        var out = Color.sort(in);

        assertEquals(
                List.of(
                        Color.sRGB(0.25, 0, 0),
                        Color.sRGB(0.5, 0, 0),
                        Color.sRGB(0.75, 0, 0),
                        Color.sRGB(0, 0, 0.25),
                        Color.sRGB(0, 0, 0.5),
                        Color.sRGB(0, 0, 0.75)
                ),
                out
        );
    }
}
