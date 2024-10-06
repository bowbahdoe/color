package dev.mccue.color;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.function.Predicate;

/// A color.
///
/// In physical reality colors are an emergent property of light.
///
/// When our eyes see light of a certain wavelength we perceive that
/// as a Color. So if the wavelength of light is between 490 and 580
/// nanometers that is what we generally associate with "green."
///
/// Computers tend to want to handle this differently
///
/// This class represents a color. Its internal representation of
/// these values is in sRGB (standard RGB). This maps to common
/// displays which display colors by powering separate Red, Green, and Blue
/// lights in specific proportions.
///
/// This representation can be transformed (or recovered in the case of sRGB)
/// by using the {@link Color#fromComponents(ColorSpace, Object)} method
/// in conjunction with a {@link ColorSpace}. For known color spaces
/// this - as well as the inverse of {@link Color#toComponents(ColorSpace)} -
/// are also available via convenience methods.
///
/// @apiNote This class does not validate or clamp values so that they
/// are in the 0-1 range that sRGB wants. This is to make it more practical
/// to use as an intermediate when converting between spaces that might have
/// temporary values which fall outside the human perceptible spectrum. If
/// you need something which does that clamping you should use {@link Color#sRGB()}
/// or the combination of {@link Color#isValid()} and {@link Color#clamped()}.
public final class Color {
    // A color is stored internally using sRGB (standard RGB) values in the range 0-1
    final double r;
    final double g;
    final double b;

    Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /// Convert this color to a {@link ColorSpace} and recover the components of
    /// that representation.
    ///
    /// @param colorSpace   The color space to convert to.
    /// @param <Components> The type for the components of colors in that space.
    /// @return The components for the color.
    public <Components> Components toComponents(ColorSpace<Components> colorSpace) {
        return colorSpace.fromColor(this);
    }

    /// Construct a color given the proper components and a {@link ColorSpace}.
    ///
    /// @param colorSpace   The color space
    /// @param components   The components for the color.
    /// @param <Components> The type for the components of colors in that space.
    public static <Components> Color fromComponents(ColorSpace<Components> colorSpace, Components components) {
        return colorSpace.toColor(components);
    }

    public static Color sRGB(double r, double g, double b) {
        return sRGB(new sRGB(r, g, b));
    }

    public static Color sRGB(sRGB sRGB) {
        return fromComponents(ColorSpace.sRGB(), sRGB);
    }

    public sRGB sRGB() {
        return toComponents(ColorSpace.sRGB());
    }

    public static Color HSL(double h, double s, double l) {
        return HSL(new HSL(h, s, l));
    }

    public static Color HSL(HSL hsl) {
        return fromComponents(ColorSpace.HSL(), hsl);
    }

    public HSL HSL() {
        return toComponents(ColorSpace.HSL());
    }

    public static Color HCL(double h, double c, double l) {
        return HCL(new HCL(h, c, l));
    }

    public static Color HCL(HCL hcl) {
        return fromComponents(ColorSpace.HCL(), hcl);
    }

    public HCL HCL() {
        return toComponents(ColorSpace.HCL());
    }

    public static Color HSV(double h, double s, double v) {
        return HSV(new HSV(h, s, v));
    }

    public static Color HSV(HSV hsv) {
        return fromComponents(ColorSpace.HSV(), hsv);
    }

    public HSV HSV() {
        return toComponents(ColorSpace.HSV());
    }

    public static Color Lab(double L, double a, double b) {
        return Lab(new Lab(L, a, b));
    }

    public static Color Lab(Lab lab) {
        return fromComponents(ColorSpace.Lab(), lab);
    }

    public Lab Lab() {
        return toComponents(ColorSpace.Lab());
    }

    public static Color LinearRGB(double R, double G, double B) {
        return LinearRGB(new LinearRGB(R, G, B));
    }

    public static Color LinearRGB(LinearRGB linearRGB) {
        return fromComponents(ColorSpace.linearRGB(), linearRGB);
    }

    public LinearRGB LinearRGB() {
        return toComponents(ColorSpace.linearRGB());
    }

    public static Color hex(String hex) {
        return fromComponents(ColorSpace.hex(), hex);
    }

    public String hex() {
        return toComponents(ColorSpace.hex());
    }

    public static Color XYZ(double X, double Y, double Z) {
        return XYZ(new XYZ(X, Y, Z));
    }

    public static Color XYZ(XYZ XYZ) {
        return fromComponents(ColorSpace.XYZ(), XYZ);
    }

    public XYZ XYZ() {
        return toComponents(ColorSpace.XYZ());
    }

    public static Color Luv(double L, double u, double v) {
        return Luv(new Luv(L, u, v));
    }

    public static Color Luv(Luv luv) {
        return fromComponents(ColorSpace.Luv(), luv);
    }

    public Luv Luv() {
        return toComponents(ColorSpace.Luv());
    }

    public static Color RGB255(int R, int G, int B) {
        return RGB255(new RGB255(R, G, B));
    }

    public static Color RGB255(RGB255 rgb255) {
        return fromComponents(ColorSpace.RGB255(), rgb255);
    }

    public RGB255 RGB255() {
        return toComponents(ColorSpace.RGB255());
    }

    public static Color OkLab(double L, double a, double b) {
        return OkLab(new OkLab(L, a, b));
    }

    public static Color OkLab(OkLab okLab) {
        return fromComponents(ColorSpace.OkLab(), okLab);
    }

    public OkLab OkLab() {
        return toComponents(ColorSpace.OkLab());
    }

    public static Color OkLch(double L, double c, double h) {
        return OkLch(new OkLch(L, c, h));
    }

    public static Color OkLch(OkLch okLch) {
        return fromComponents(ColorSpace.OkLch(), okLch);
    }

    public OkLch OkLch() {
        return toComponents(ColorSpace.OkLch());
    }

    /// Checks whether the color exists in RGB space, i.e. all values are in [0..1]
    ///
    /// @return Whether the color exists in RGB space.
    public boolean isValid() {
        return 0.0 <= r && r <= 1.0 &&
                0.0 <= g && g <= 1.0 &&
                0.0 <= b && b <= 1.0;
    }

    /// Clamps the {@link Color} into valid range, clamping each value to [0..1]
    /// If the {@link Color} is valid already, this is a no-op.
    ///
    /// @return A valid {@link Color}.
    public Color clamped() {
        return new Color(
                Math.clamp(r, 0, 1),
                Math.clamp(g, 0, 1),
                Math.clamp(b, 0, 1)
        );
    }

    @Override
    public String toString() {
        var hex = toComponents(ColorSpace.hex());
        return "\033[48:2::%d:%d:%dm \033[49m"
                .formatted(
                        Integer.parseInt(hex.substring(1, 3), 16),
                        Integer.parseInt(hex.substring(3, 5), 16),
                        Integer.parseInt(hex.substring(5, 7), 16)
                );
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Color c && r == c.r && g == c.g && b == c.b;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    /// Computes the distance between two colors in RGB space.
    ///
    /// @apiNote This is not a good measure! Rather do it in Lab space.
    public double distanceRgb(Color c2) {
        var c1 = this;
        return Math.sqrt(sq(c1.r - c2.r) + sq(c1.g - c2.g) + sq(c1.b - c2.b));
    }

    /// Computes the distance between two colors in linear RGB
    /// space. This is not useful for measuring how humans perceive color, but
    /// might be useful for other things, like dithering.
    public double distanceLinearRGB(Color c2) {
        var c1 = this;
        switch (c1.LinearRGB()) {
            case LinearRGB(var r1, var g1, var b1) -> {
                switch (c2.LinearRGB()) {
                    case LinearRGB(var r2, var g2, var b2) -> {
                        return Math.sqrt(sq(r1 - r2) + sq(g1 - g2) + sq(b1 - b2));
                    }
                }
            }
        }
    }

    /// Color distance algorithm developed by Thiadmer Riemersma.
    /// It uses RGB coordinates, but he claims it has similar results to CIELUV.
    /// This would make it both fast and accurate.
    ///
    /// Sources:
    ///
    /// @see <a href="https://www.compuphase.com/cmetric.htm">https://www.compuphase.com/cmetric.htm</a>
    /// @see <a href="https://github.com/lucasb-eyer/go-colorful/issues/52">https://github.com/lucasb-eyer/go-colorful/issues/52</a>
    public double distanceRiemersma(Color c1, Color c2) {
        var rAvg = (c1.r + c2.r) / 2.0;
        // Deltas
        var dR = c1.r - c2.r;
        var dG = c1.g - c2.g;
        var dB = c1.b - c2.b;

        return Math.sqrt((2 + rAvg) * dR * dR + 4 * dG * dG + (2 + (1 - rAvg)) * dB * dB);
    }

    /// Uses the Delta E 2000 formula to calculate color
    /// distance. It is more expensive but more accurate than both DistanceLab
    /// and DistanceCIE94.
    public double distanceCIEDE2000(Color c2) {
        return distanceCIEDE2000klch(c2, 1, 1, 1);
    }

    /// Uses the Delta E 2000 formula with custom values
    /// for the weighting factors kL, kC, and kH.
    ///
    /// @param cr The color to compare to.
    /// @param kL weighting factor
    /// @param kC weighting factor
    /// @param kH weighting factor
    /// @return Distance between colors.
    public double distanceCIEDE2000klch(Color cr, double kL, double kC, double kH) {
        var cl = this;
        var lab1 = cl.Lab();
        var lab2 = cr.Lab();

        double l1 = lab1.L();
        double a1 = lab1.a();
        double b1 = lab1.b();

        double l2 = lab2.L();
        double a2 = lab2.a();
        double b2 = lab2.b();

        // As with CIE94, we scale up the ranges of L,a,b beforehand and scale
        // them down again afterwards.
        l1 = l1 * 100;
        a1 = a1 * 100;
        b1 = b1 * 100;
        l2 = l2 * 100;
        a2 = a2 * 100;
        b2 = b2 * 100;

        var cab1 = Math.sqrt(sq(a1) + sq(b1));
        var cab2 = Math.sqrt(sq(a2) + sq(b2));
        var cabmean = (cab1 + cab2) / 2;

        var g = 0.5 * (1 - Math.sqrt(Math.pow(cabmean, 7) / (Math.pow(cabmean, 7) + Math.pow(25, 7))));
        var ap1 = (1 + g) * a1;
        var ap2 = (1 + g) * a2;
        var cp1 = Math.sqrt(sq(ap1) + sq(b1));
        var cp2 = Math.sqrt(sq(ap2) + sq(b2));

        var hp1 = 0.0;
        if (b1 != ap1 || ap1 != 0) {
            hp1 = Math.atan2(b1, ap1);
            if (hp1 < 0) {
                hp1 += Math.PI * 2;
            }
            hp1 *= 180 / Math.PI;
        }
        var hp2 = 0.0;
        if (b2 != ap2 || ap2 != 0) {
            hp2 = Math.atan2(b2, ap2);
            if (hp2 < 0) {
                hp2 += Math.PI * 2;
            }
            hp2 *= 180 / Math.PI;
        }

        var deltaLp = l2 - l1;
        var deltaCp = cp2 - cp1;
        var dhp = 0.0;
        var cpProduct = cp1 * cp2;
        if (cpProduct != 0) {
            dhp = hp2 - hp1;
            if (dhp > 180) {
                dhp -= 360;
            } else if (dhp < -180) {
                dhp += 360;
            }
        }
        var deltaHp = 2 * Math.sqrt(cpProduct) * Math.sin(dhp / 2 * Math.PI / 180);

        var lpmean = (l1 + l2) / 2;
        var cpmean = (cp1 + cp2) / 2;
        var hpmean = hp1 + hp2;
        if (cpProduct != 0) {
            hpmean /= 2;
            if (Math.abs(hp1 - hp2) > 180) {
                if (hp1 + hp2 < 360) {
                    hpmean += 180;
                } else {
                    hpmean -= 180;
                }
            }
        }

        var t = 1 - 0.17 * Math.cos((hpmean - 30) * Math.PI / 180) + 0.24 * Math.cos(2 * hpmean * Math.PI / 180) + 0.32 * Math.cos((3 * hpmean + 6) * Math.PI / 180) - 0.2 * Math.cos((4 * hpmean - 63) * Math.PI / 180);
        var deltaTheta = 30 * Math.exp(-sq((hpmean - 275) / 25));
        var rc = 2 * Math.sqrt(Math.pow(cpmean, 7) / (Math.pow(cpmean, 7) + Math.pow(25, 7)));
        var sl = 1 + (0.015 * sq(lpmean - 50)) / Math.sqrt(20 + sq(lpmean - 50));
        var sc = 1 + 0.045 * cpmean;
        var sh = 1 + 0.015 * cpmean * t;
        var rt = -Math.sin(2 * deltaTheta * Math.PI / 180) * rc;

        return Math.sqrt(sq(deltaLp / (kL * sl)) + sq(deltaCp / (kC * sc)) + sq(deltaHp / (kH * sh)) + rt * (deltaCp / (kC * sc)) * (deltaHp / (kH * sh))) * 0.01;

    }

    // DistanceLab is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    public double distanceLab(Color c2) {
        var c1 = this;
        switch (c1.Lab()) {
            case Lab(double l1, double a1, double b1) -> {
                switch (c2.Lab()) {
                    case Lab(double l2, double a2, double b2) -> {
                        return Math.sqrt(sq(l1 - l2) + sq(a1 - a2) + sq(b1 - b2));
                    }
                }
            }
        }
    }

    // DistanceCIE76 is the same as DistanceLab.
    public double distanceCIE76(Color c2) {
        return distanceLab(c2);
    }


    // Uses the CIE94 formula to calculate color distance. More accurate than
    // DistanceLab, but also more work.
    public double distanceCIE94(Color cr) {
        var cl = this;

        switch (cl.Lab()) {
            case Lab(var l1, var a1, var b1) -> {
                switch (cr.Lab()) {
                    case Lab(var l2, var a2, var b2) -> {
                        // NOTE: Since all those formulas expect L,a,b values 100x larger than we
                        //       have them in this library, we either need to adjust all constants
                        //       in the formula, or convert the ranges of L,a,b before, and then
                        //       scale the distances down again. The latter is less error-prone.
                        l1 = 100 * l1;
                        a1 = 100 * a1;
                        b1 = 100 * b1;

                        l2 = 100 * l2;
                        a2 = 100 * a2;
                        b2 = 100 * b2;

                        var kl = 1.0; // 2.0 for textiles
                        var kc = 1.0;
                        var kh = 1.0;
                        var k1 = 0.045; // 0.048 for textiles
                        var k2 = 0.015; // 0.014 for textiles.

                        var deltaL = l1 - l2;
                        var c1 = Math.sqrt(sq(a1) + sq(b1));
                        var c2 = Math.sqrt(sq(a2) + sq(b2));
                        var deltaCab = c1 - c2;

                        // Not taking Sqrt here for stability, and it's unnecessary.
                        var deltaHab2 = sq(a1 - a2) + sq(b1 - b2) - sq(deltaCab);
                        var sl = 1.0;
                        var sc = 1.0 + k1 * c1;
                        var sh = 1.0 + k2 * c1;

                        var vL2 = sq(deltaL / (kl * sl));
                        var vC2 = sq(deltaCab / (kc * sc));
                        var vH2 = deltaHab2 / sq(kh * sh);

                        return Math.sqrt(vL2 + vC2 + vH2) * 0.01; // See above.
                    }
                }
            }
        }
    }

    // DistanceLuv is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    public double distanceLuv(Color c2) {
        var c1 = this;
        switch (c1.Luv()) {
            case Luv(double l1, double u1, double v1) -> {
                switch (c2.Luv()) {
                    case Luv(double l2, double u2, double v2) -> {
                        return Math.sqrt(sq(l1 - l2) + sq(u1 - u2) + sq(v1 - v2));
                    }
                }
            }
        }
    }

    // An element represents a single element of a set.  It is used to
    // implement a disjoint-set forest.
    private static final class Element {
        // Parent element
        Element parent;
        // Rank (approximate depth) of the subtree with this element as root
        int rank;
    }

    // newElement creates a singleton set and returns its sole element.
    private static Element newElement() {
        var s = new Element();
        s.parent = s;
        return s;
    }

    // find returns an arbitrary element of a set when invoked on any element of
    // the set, The important feature is that it returns the same value when
    // invoked on any element of the set.  Consequently, it can be used to test if
    // two elements belong to the same set.
    private static Element find(Element e) {
        for (; e.parent != e; ) {
            e.parent = e.parent.parent;
            e = e.parent;
        }
        return e;
    }

    // union establishes the union of two sets when given an element from each set.
    // Afterwards, the original sets no longer exist as separate entities.
    private static void union(Element e1, Element e2) {
        // Ensure the two elements aren't already part of the same union.
        var e1Root = find(e1);
        var e2Root = find(e2);
        if (e1Root == e2Root) {
            return;
        }

        // Create a union by making the shorter tree point to the root of the
        // larger tree.
        if (e1Root.rank < e2Root.rank) {
            e1Root.parent = e2Root;
        } else if (e1Root.rank > e2Root.rank) {
            e2Root.parent = e1Root;
        } else {
            e2Root.parent = e1Root;
            e1Root.rank++;
        }
    }

    private record EdgeIdxs(int _0, int _1) {
    }

    private record EdgeDistances(Map<EdgeIdxs, Double> value) {
    }

    // allToAllDistancesCIEDE2000 computes the CIEDE2000 distance between each pair of
    // colors.  It returns a map from a pair of indices (u, v) with u < v to a
    // distance.
    private static EdgeDistances allToAllDistances(List<Color> cs, ColorDistance colorDistance) {
        var nc = cs.size();
        var m = new HashMap<EdgeIdxs, Double>(nc * nc);
        for (int u = 0; u < nc - 1; u++) {
            for (int v = u + 1; v < nc; v++) {
                m.put(new EdgeIdxs(u, v), colorDistance.distance(cs.get(u), cs.get(v)));
            }
        }
        return new EdgeDistances(m);
    }

    // sortEdges sorts all edges in a distance map by increasing vertex distance.
    private static ArrayList<EdgeIdxs> sortEdges(EdgeDistances m) {
        var es = new ArrayList<EdgeIdxs>(m.value.size());
        es.addAll(m.value.keySet());

        es.sort(Comparator.comparing((i) -> m.value.getOrDefault(i, 0.0)));
        return es;
    }

    // minSpanTree computes a minimum spanning tree from a vertex count and a
    // distance-sorted edge list.  It returns the subset of edges that belong to
    // the tree, including both (u, v) and (v, u) for each edge.
    private static Set<EdgeIdxs> minSpanTree(int nc, List<EdgeIdxs> es) {
        // Start with each vertex in its own set.
        var elts = new Element[nc];
        for (int i = 0; i < elts.length; i++) {
            elts[i] = newElement();
        }

        // Run Kruskal's algorithm to construct a minimal spanning tree.
        var mst = new LinkedHashSet<EdgeIdxs>(nc);
        for (var uv : es) {
            var u = uv._0;
            var v = uv._1;
            if (find(elts[u]) == find(elts[v])) {
                continue; // Same set: edge would introduce a cycle.
            }
            mst.add(uv);
            mst.add(new EdgeIdxs(v, u));
            union(elts[u], elts[v]);
        }

        return mst;
    }

    // traverseMST walks a minimum spanning tree in prefix order.
    private static ArrayList<Integer> traverseMST(Set<EdgeIdxs> mst, int root) {
        // Compute a list of neighbors for each vertex.
        var neighs = new LinkedHashMap<Integer, ArrayList<Integer>>(mst.size());
        for (var uv : mst) {
            var u = uv._0;
            var v = uv._1;
            neighs.putIfAbsent(u, new ArrayList<>());
            neighs.get(u).add(v);
        }

        for (var vs : neighs.values()) {
            vs.sort(Comparator.naturalOrder());
        }

        // Walk the tree from a given vertex.
        ArrayList<Integer> order = new ArrayList<Integer>(neighs.size());
        Set<Integer> visited = new LinkedHashSet<>();

        IntConsumer walkFrom = new IntConsumer() {
            @Override
            public void accept(int r) {
                // Visit the starting vertex.
                order.add(r);
                visited.add(r);

                // Recursively visit each child in turn.
                for (var c : neighs.get(r)) {
                    if (!visited.contains(c)) {
                        this.accept(c);
                    }
                }
            }
        };

        walkFrom.accept(root);

        return order;
    }

    public static List<Color> sort(List<Color> cs) {
        return sort(cs, Color::distanceCIEDE2000);
    }

    public static List<Color> sort(List<Color> cs, ColorDistance colorDistance) {
        if (cs.size() < 2) {
            return List.copyOf(cs);
        }

        // Do nothing in trivial cases.
        var newCs = new Color[cs.size()];

        var dists = allToAllDistances(cs, colorDistance);

        var edges = sortEdges(dists);

        var mst = minSpanTree(cs.size(), edges);

        // Find the darkest color in the list.
        var black = Color.sRGB(0, 0, 0);
        int dIdx = 0; // Index of darkest color
        var light = Double.MAX_VALUE; // Lightness of darkest color (distance from black)

        for (int i = 0; i < cs.size(); i++) {
            var c = cs.get(i);
            var d = colorDistance.distance(black, c);
            if (d < light) {
                dIdx = i;
                light = d;
            }
        }

        // Traverse the tree starting from the darkest color.
        var idxs = traverseMST(mst, dIdx);

        // Convert the index list to a list of colors, overwriting the input.
        for (int i = 0; i < idxs.size(); i++) {
            var idx = idxs.get(i);
            newCs[i] = cs.get(idx);
        }

        return List.of(newCs);
    }

    // BlendLab blends two colors in the L*a*b* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLab(Color c2, double t) {
        var c1 = this;

        switch (c1.Lab()) {
            case Lab(double l1, double a1, double b1) -> {
                switch (c2.Lab()) {
                    case Lab(double l2, double a2, double b2) -> {
                        return Lab(l1 + t * (l2 - l1),
                                a1 + t * (a2 - a1),
                                b1 + t * (b2 - b1));
                    }
                }
            }
        }
    }

    // BlendLuv blends two colors in the CIE-L*u*v* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLuv(Color c2, double t) {
        var c1 = this;

        switch (c1.Luv()) {
            case Luv(double l1, double u1, double v1) -> {
                switch (c2.Luv()) {
                    case Luv(double l2, double u2, double v2) -> {
                        return Luv(l1 + t * (l2 - l1),
                                u1 + t * (u2 - u1),
                                v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    public Color blendRgb(Color c2, double t) {
        var c1 = this;
        return new Color(
                c1.r + t * (c2.r - c1.r),
                c1.g + t * (c2.g - c1.g),
                c1.b + t * (c2.b - c1.b)
        );
    }

    // BlendLinearRgb blends two colors in the Linear RGB color-space.
    // Unlike BlendRgb, this will not produce dark color around the center.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendLinearRgb(Color c2, double t) {
        var c1 = this;
        switch (c1.LinearRGB()) {
            case LinearRGB(double r1, double g1, double b1) -> {
                switch (c2.LinearRGB()) {
                    case LinearRGB(double r2, double g2, double b2) -> {
                        return Color.LinearRGB(
                                r1 + t * (r2 - r1),
                                g1 + t * (g2 - g1),
                                b1 + t * (b2 - b1)
                        );
                    }
                }
            }
        }
    }

    // Utility used by Hxx color-spaces for interpolating between two angles in [0,360].
    private static double interp_angle(double a0, double a1, double t) {
        // Based on the answer here: http://stackoverflow.com/a/14498790/2366315
        // With potential proof that it works here: http://math.stackexchange.com/a/2144499
        var delta = (((a1 - a0 % 360.0) + 540) % 360.0) - 180.0;
        return (a0 + t * delta + 360.0) % 360.0;
    }

    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    public Color blendHSV(Color c2, double t) {
        var c1 = this;
        switch (c1.HSV()) {
            case HSV(double h1, double s1, double v1) -> {
                switch (c2.HSV()) {
                    case HSV(double h2, double s2, double v2) -> {
                        // https://github.com/lucasb-eyer/go-colorful/pull/60
                        if (s1 == 0 && s2 != 0) {
                            h1 = h2;
                        } else if (s2 == 0 && s1 != 0) {
                            h2 = h1;
                        }

                        // We know that h are both in [0..360]
                        return HSV(interp_angle(h1, h2, t), s1 + t * (s2 - s1), v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    // BlendHcl blends two colors in the CIE-L*C*h° color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    public Color blendHCL(Color col2, double t) {
        var col1 = this;

        switch (col1.HCL()) {
            case HCL(var h1, var c1, var l1) -> {
                switch (col2.HCL()) {
                    case HCL(var h2, var c2, var l2) -> {
                        // https://github.com/lucasb-eyer/go-colorful/pull/60
                        if (c1 <= 0.00015 && c2 >= 0.00015) {
                            h1 = h2;
                        } else if (c2 <= 0.00015 && c1 >= 0.00015) {
                            h2 = h1;
                        }

                        // We know that h are both in [0..360]
                        return HCL(interp_angle(h1, h2, t), c1 + t * (c2 - c1), l1 + t * (l2 - l1)).clamped();
                    }
                }
            }
        }
    }

    // BlendLuvLCh blends two colors in the cylindrical CIELUV color space.
    // t == 0 results in c1, t == 1 results in c2
    /* public Color BlendLuvLCh(Color col2, double t) {
        l1, c1, h1 := col1.LuvLCh()
        l2, c2, h2 := col2.LuvLCh()

        // We know that h are both in [0..360]
        return LuvLCh(l1+t*(l2-l1), c1+t*(c2-c1), interp_angle(h1, h2, t))
    } */

    public static Color warm() {
        return warm(ThreadLocalRandom.current());
    }

    /// Creates a random dark, "warm" color through restricted HCL space.
    /// This is slower than FastWarmColor but will likely give you colors which have
    /// the same "warmness" if you run it many times.
    ///
    /// @param random The random number generator to use.
    /// @return A random dark, "warm" color.
    public static Color warm(Random random) {
        Color c = randomWarm(random);
        while (!c.isValid()) {
            c = randomWarm(random);
        }
        return c;
    }

    private static Color randomWarm(Random random) {
        return HCL(
                random.nextDouble() * 360.0,
                0.1 + random.nextDouble() * 0.3,
                0.2 + random.nextDouble() * 0.3
        );
    }

    // Check for equality between colors within the tolerance Delta (1/255).
    public boolean almostEqualRgb(Color c2, double delta) {
        var c1 = this;
        return Math.abs(c1.r - c2.r) +
                Math.abs(c1.g - c2.g) +
                Math.abs(c1.b - c2.b) < 3.0 * delta;
    }

    private static final double DELTA = 1.0 / 255;

    // Check for equality between colors within the tolerance Delta (1/255).
    public boolean almostEqualRgb(Color c2) {
        return almostEqualRgb(c2, DELTA);
    }

    public static List<Color> fastWarm(int colorsCount) {
        Color[] colorArr = new Color[colorsCount];
        var random = ThreadLocalRandom.current();
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] = Color.fromComponents(
                    HSVColorSpace.INSTANCE,
                    new HSV(i * (360.0 / ((double) colorsCount)), 0.55 + random.nextDouble() * 0.2, 0.35 + random.nextDouble() * 0.2)
            );
        }

        return List.of(colorArr);
    }

    public static List<Color> fastHappy(int colorsCount) {
        Color[] colorArr = new Color[colorsCount];
        var random = ThreadLocalRandom.current();
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] = Color.fromComponents(
                    HSVColorSpace.INSTANCE,
                    new HSV(i * (360.0 / ((double) colorsCount)), 0.8 + random.nextDouble() * 0.2, 0.65 + random.nextDouble() * 0.2)
            );
        }

        return List.of(colorArr);
    }

    private static final class SoftPaletteSettings {
        Predicate<Lab> checkColor = __ -> true;
        int iterations = 50;
        boolean manySamples;
    }

    public static List<Color> soft(int colorsCount) {
        var settings = new SoftPaletteSettings();
        settings.iterations = 50;
        settings.manySamples = false;

        return soft(colorsCount, settings);
    }

    private static List<Color> soft(int colorsCount, SoftPaletteSettings settings) {
        // Checks whether it's a valid RGB and also fulfills the potentially provided constraint.
        Predicate<Lab> check = (col) -> {
            var c = Color.fromComponents(ColorSpace.Lab(), new Lab(col.L(), col.a(), col.b()));
            return c.isValid() && settings.checkColor.test(col);
        };

        // Sample the color space. These will be the points k-means is run on.
        var dl = 0.05;
        var dab = 0.1;
        if (settings.manySamples) {
            dl = 0.01;
            dab = 0.05;
        }

        ArrayList<Lab> samples = new ArrayList<>((int) (1.0 / dl * 2.0 / dab * 2.0 / dab));
        for (double l = 0.0; l <= 1.0; l += dl) {
            for (double a = -1.0; a <= 1.0; a += dab) {
                for (double b = -1.0; b <= 1.0; b += dab) {
                    var lab = new Lab(l, a, b);
                    if (check.test(lab)) {
                        samples.add(lab);
                    }
                }
            }
        }

        // That would cause some infinite loops down there...
        if (samples.size() < colorsCount) {
            throw new PaletteGenerationException(
                    "More colors requested (%d) than samples available (%d). Your requested color count may be wrong, you might want to use many samples or your constraint function makes the valid color space too small".formatted(colorsCount, samples.size())
            );
        } else if (samples.size() == colorsCount) {
            return List.of(labs2cols(samples)); // Oops?
        }

        // We take the initial means out of the samples, so they are in fact medoids.
        // This helps us avoid infinite loops or arbitrary cutoffs with too restrictive constraints.
        var rand = ThreadLocalRandom.current();
        Lab[] means = new Lab[colorsCount];
        for (int i = 0; i < colorsCount; i++) {
            for (
                    means[i] = samples.get(rand.nextInt(samples.size()));
                    in(means, i, means[i]);
                    means[i] = samples.get(rand.nextInt(samples.size()))
            ) {
            }

        }

        int[] clusters = new int[samples.size()];
        boolean[] samples_used = new boolean[samples.size()];

        // The actual k-means/medoid iterations
        for (int i = 0; i < settings.iterations; i++) {
            // Reassing the samples to clusters, i.e. to their closest mean.
            // By the way, also check if any sample is used as a medoid and if so, mark that.
            for (int isample = 0; isample < samples.size(); isample++) {
                var sample = samples.get(isample);
                samples_used[isample] = false;
                var mindist = Double.POSITIVE_INFINITY;
                for (int imean = 0; imean < means.length; imean++) {
                    var mean = means[imean];
                    var dist = lab_dist(sample, mean);
                    if (dist < mindist) {
                        mindist = dist;
                        clusters[isample] = imean;
                    }

                    if (lab_eq(sample, mean)) {
                        samples_used[isample] = true;
                    }
                }
            }


            // Compute new means according to the samples.
            for (int imean = 0; imean < means.length; imean++) {
                // The new mean is the average of all samples belonging to it..
                var nsamples = 0;
                var newmean = new Lab(0, 0, 0);
                for (int isample = 0; isample < samples.size(); isample++) {
                    var sample = samples.get(isample);
                    if (clusters[isample] == imean) {
                        nsamples++;
                        newmean = new Lab(
                                newmean.L() + sample.L(),
                                newmean.a() + sample.a(),
                                newmean.b() + sample.b()
                        );
                    }
                }
                if (nsamples > 0) {
                    newmean = new Lab(
                            newmean.L() / nsamples,
                            newmean.a() / nsamples,
                            newmean.b() / nsamples
                    );
                } else {
                    // That mean doesn't have any samples? Get a new mean from the sample list!
                    int inewmean;
                    for (
                            inewmean = rand.nextInt(samples_used.length);
                            samples_used[inewmean];
                            inewmean = rand.nextInt(samples_used.length)
                    ) {
                    }
                    newmean = samples.get(inewmean);
                    samples_used[inewmean] = true;
                }

                // But now we still need to check whether the new mean is an allowed color.
                if (nsamples > 0 && check.test(newmean)) {
                    // It does, life's good (TM)
                    means[imean] = newmean;
                } else {
                    // New mean isn't an allowed color or doesn't have any samples!
                    // Switch to medoid mode and pick the closest (unused) sample.
                    // This should always find something thanks to len(samples) >= colorsCount
                    var mindist = Double.POSITIVE_INFINITY;
                    for (int isample = 0; isample < samples.size(); isample++) {
                        var sample = samples.get(isample);
                        if (!samples_used[isample]) {
                            var dist = lab_dist(sample, newmean);
                            if (dist < mindist) {
                                mindist = dist;
                                newmean = sample;
                            }
                        }
                    }
                }
            }

        }

        return List.of(labs2cols(Arrays.asList(means)));
    }

    static List<Color> warm(int colorsCount) {
        Predicate<Lab> warmy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.1 <= c && c <= 0.4 && 0.2 <= l && l <= 0.5;
        };
        var settings = new SoftPaletteSettings();
        settings.checkColor = warmy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings);
    }

    static List<Color> happy(int colorsCount) {
        Predicate<Lab> pimpy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.3 <= c && 0.4 <= l && l <= 0.8;
        };
        var settings = new SoftPaletteSettings();
        settings.checkColor = pimpy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings);
    }

    private static boolean in(Lab[] haystack, int upto, Lab needle) {
        for (int i = 0; i < upto && i < haystack.length; i++) {
            if (Objects.equals(haystack[i], needle)) {
                return true;
            }
        }
        return false;
    }

    private static double sq(double v) {
        return v * v;
    }

    // That's faster than using colorful's DistanceLab since we would have to
    // convert back and forth for that. Here is no conversion.
    private static double lab_dist(Lab lab1, Lab lab2) {
        return Math.sqrt(sq(lab1.L() - lab2.L()) + sq(lab1.a() - lab2.a()) + sq(lab1.b() - lab2.b()));
    }

    private static final double LAB_DELTA = 1e-6;

    private static boolean lab_eq(Lab lab1, Lab lab2) {
        return Math.abs(lab1.L() - lab2.L()) < LAB_DELTA &&
                Math.abs(lab1.a() - lab2.a()) < LAB_DELTA &&
                Math.abs(lab1.b() - lab2.b()) < LAB_DELTA;
    }

    private static Color[] labs2cols(List<Lab> labs) {
        Color[] cols = new Color[labs.size()];
        for (int i = 0; i < labs.size(); i++) {
            var v = labs.get(i);
            cols[i] = Color.Lab(v);
        }
        return cols;
    }
}
