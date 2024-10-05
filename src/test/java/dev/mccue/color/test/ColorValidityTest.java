package dev.mccue.color.test;

import dev.mccue.color.Color;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ColorValidityTest {
    @Test
    public void testColorValidity() {
        int seed = Instant.now().getNano();
        var rand = new Random();
        rand.setSeed(seed);

        for (int i = 0; i < 100; i++) {
            var col = Color.warm(rand);
            // assertTrue(col.isValid(), "Invalid Color: seed: " + seed);
        }
    }
}
