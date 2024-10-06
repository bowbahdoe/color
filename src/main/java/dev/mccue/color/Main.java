package dev.mccue.color;

import java.util.List;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        var srgb = Color.HSL(300, 1, 0.83).sRGB();
        r(Color::happy);
    }

    static void r(Function<Integer, List<Color>> f) {
        var gens = Color.sort(f.apply(40));

        int idx = 0;
        for (int i = 0; i < 40; i++) {
            System.out.print(gens.get(idx));
            idx++;
        }

        System.out.println();
    }
}
