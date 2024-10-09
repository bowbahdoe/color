package dev.mccue.color;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.random.RandomGenerator;
import static dev.mccue.color.Util.interp_angle;

/// A Color.
///
/// Colors aren't a physical phenomena, they are a perceptual one.
///
/// Most light sources (except something exotic like
/// sodium lamps) don't output light in just one wavelength. They have
/// what we call a
///
///
/// This interface defines conversions to different color spaces.
/// At a minimum a color must be convertible to {@link sRGB}.
///
/// If a color is going to be an intermediate in a conversion between
/// color spaces then it shouldn't go out of its way to clamp its values
/// into an acceptable range. This applies to anything that would lose
/// precision from floating point math like {@link sRGB} but not
/// things like inherently lose a large amount of precision like {@link RGB255}.
///
/// Implementation of this interface should be
/// - Immutable
/// - Thread-Safe
/// - Value Classes (ideally)
public interface Color {
    /// Convert this color to the {@link sRGB}
    /// color space.
    ///
    /// @return This color in the {@link sRGB} color space.
    sRGB sRGB();

    /// Convert this color to the {@link LinearRGB}
    /// color space.
    ///
    /// @return This color in the {@link LinearRGB} color space.
    default LinearRGB LinearRGB() {
        return sRGB().LinearRGB();
    }

    /// Convert this color to the {@link Lab}
    /// color space.
    ///
    /// @return This color in the {@link Lab} color space.
    default Lab Lab() {
        return sRGB().Lab();
    }

    /// Convert this color to the {@link Lab}
    /// color space.
    ///
    /// @param referenceWhite The reference white to use.
    /// @return This color in the {@link Lab} color space.
    default Lab Lab(ReferenceWhite referenceWhite) {
        return sRGB().Lab(referenceWhite);
    }

    /// Convert this color to the {@link HSLuv}
    /// color space.
    ///
    /// @return This color in the {@link HSLuv} color space.
    default HSLuv HSLuv() {
        return sRGB().HSLuv();
    }

    /// Convert this color to the {@link Luv}
    /// color space.
    ///
    /// @return This color in the {@link Luv} color space.
    default Luv Luv() {
        return sRGB().Luv();
    }

    /// Convert this color to the {@link Luv}
    /// color space.
    ///
    /// @param referenceWhite The reference white to use.
    /// @return This color in the {@link Luv} color space.
    default Luv Luv(ReferenceWhite referenceWhite) {
        return sRGB().Luv(referenceWhite);
    }

    /// Convert this color to the {@link HPLuv}
    /// color space.
    ///
    /// @return This color in the {@link HPLuv} color space.
    default HPLuv HPLuv() {
        return sRGB().HPLuv();
    }

    /// Convert this color to the {@link LuvLCh}
    /// color space.
    ///
    /// @return This color in the {@link LuvLCh} color space.
    default LuvLCh LuvLCh() {
        return sRGB().LuvLCh();
    }

    /// Convert this color to the {@link HSV}
    /// color space.
    ///
    /// @return This color in the {@link HSV} color space.
    default HSV HSV() {
        return sRGB().HSV();
    }

    /// Convert this color to the {@link LabLCh}
    /// color space.
    ///
    /// @return This color in the {@link LabLCh} color space.
    default LabLCh HCL() {
        return sRGB().HCL();
    }

    /// Convert this color to the {@link LabLCh}
    /// color space.
    ///
    /// @param referenceWhite The reference white to use.
    /// @return This color in the {@link LabLCh} color space.
    default LabLCh HCL(ReferenceWhite referenceWhite) {
        return sRGB().HCL(referenceWhite);
    }

    /// Convert this color to the {@link RGB255}
    /// color space.
    ///
    /// @return This color in the {@link RGB255} color space.
    default RGB255 RGB255() {
        return sRGB().RGB255();
    }

    /// Convert this color to the {@link HSL}
    /// color space.
    ///
    /// @return This color in the {@link HSL} color space.
    default HSL HSL() {
        return sRGB().HSL();
    }

    /// Convert this color to a hex string starting with a `#`.
    ///
    /// @return This color as a hex string.
    default String hex() {
        return RGB255().hex();
    }

    /// Convert this color to the {@link xyY}
    /// color space.
    ///
    /// @return This color in the {@link xyY} color space.
    default xyY xyY() {
        return sRGB().xyY();
    }

    /// Convert this color to the {@link xyY}
    /// color space.
    ///
    /// @param referenceWhite The reference white to use.
    /// @return This color in the {@link xyY} color space.
    default xyY xyY(ReferenceWhite referenceWhite) {
        return sRGB().xyY(referenceWhite);
    }

    /// Convert this color to the {@link XYZ}
    /// color space.
    ///
    /// @return This color in the {@link XYZ} color space.
    default XYZ XYZ() {
        return sRGB().XYZ();
    }

    /// Convert this color to the {@link XYZ}
    /// color space.
    ///
    /// @param referenceWhite The reference white to use.
    /// @return This color in the {@link XYZ} color space.
    default XYZ XYZ(ReferenceWhite referenceWhite) {
        return sRGB().XYZ(referenceWhite);
    }

    /// Convert this color to the {@link OkLab}
    /// color space.
    ///
    /// @return This color in the {@link OkLab} color space.
    default OkLab OkLab() {
        return XYZ().OkLab();
    }

    /// Convert this color to the {@link OkLch}
    /// color space.
    ///
    /// @return This color in the {@link OkLch} color space.
    default OkLch OkLch() {
        return OkLab().OkLch();
    }

    static LabLCh LabLCh(double L, double C, double h) {
        return new LabLCh(L, C, h);
    }

    static HPLuv HPLuv(double L, double u, double v) {
        return new HPLuv(L, u, v);
    }

    static HSL HSL(double H, double S, double L) {
        return new HSL(H, S, L);
    }

    static HSLuv HSLuv(double H, double S, double L) {
        return new HSLuv(H, S, L);
    }

    static HSV HSV(double H, double S, double V) {
        return new HSV(H, S, V);
    }

    static Lab Lab(double L, double a, double b) {
        return new Lab(L, a, b);
    }

    static LinearRGB LinearRGB(double R, double G, double B) {
        return new LinearRGB(R, G, B);
    }

    static Luv Luv(double L, double u, double v) {
        return new Luv(L, u, v);
    }

    static LuvLCh LuvLCh(double L, double c, double h) {
        return new LuvLCh(L, c, h);
    }

    static OkLab OkLab(double L, double a, double b) {
        return new OkLab(L, a, b);
    }

    static OkLch OkLch(double L, double c, double h) {
        return new OkLch(L, c, h);
    }

    static RGB255 RGB255(int value) {
        return new RGB255(value);
    }

    static RGB255 RGB255(int R, int G, int B) {
        return new RGB255(R, G, B);
    }

    static RGB255 RGB255(byte R, byte G, byte B) {
        return new RGB255(R, G, B);
    }

    static RGB255 hex(String hex) {
        return RGB255.hex(hex);
    }

    static sRGB sRGB(double R, double G, double B) {
        return new sRGB(R, G, B);
    }

    static xyY xyY(double x, double y, double Y) {
        return new xyY(x, y, Y);
    }

    static XYZ XYZ(double X, double Y, double Z) {
        return new XYZ(X, Y, Z);
    }

    /// Computes the distance between two colors in RGB space.
    ///
    /// Note: This is not a good measure! Rather do it in Lab space.
    default double distanceRGB(Color other) {
        return this.sRGB().distance(other.sRGB());
    }

    /// Computes the distance between two colors in linear RGB
    /// space. This is not useful for measuring how humans perceive color, but
    /// might be useful for other things, like dithering.
    default double distanceLinearRGB(Color other) {
        return this.LinearRGB().distance(other.LinearRGB());
    }

    /// Color distance algorithm developed by Thiadmer Riemersma.
    /// It uses RGB coordinates, but he claims it has similar results to CIELUV.
    /// This would make it both fast and accurate.
    ///
    /// Sources:
    ///
    /// @see <a href="https://www.compuphase.com/cmetric.htm">https://www.compuphase.com/cmetric.htm</a>
    /// @see <a href="https://github.com/lucasb-eyer/go-colorful/issues/52">https://github.com/lucasb-eyer/go-colorful/issues/52</a>
    default double distanceRiemersma(Color other) {
        return this.sRGB().distanceRiemersma(other.sRGB());
    }

    /// Uses the Delta E 2000 formula to calculate color
    /// distance. It is more expensive but more accurate than both DistanceLab
    /// and DistanceCIE94.
    default double distanceCIEDE2000(Color c2) {
        return this.Lab().distanceCIEDE2000(c2.Lab());
    }

    /// Uses the Delta E 2000 formula with custom values
    /// for the weighting factors kL, kC, and kH.
    ///
    /// @param cr The color to compare to.
    /// @param kL weighting factor
    /// @param kC weighting factor
    /// @param kH weighting factor
    /// @return Distance between colors.
    default double distanceCIEDE2000klch(Color cr, double kL, double kC, double kH) {
        return Lab().distanceCIEDE2000klch(cr.Lab(), kL, kC, kH);
    }

    // DistanceLab is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    default double distanceLab(Color c2) {
        return this.Lab().distance(c2.Lab());
    }

    // DistanceCIE76 is the same as DistanceLab.
    default double distanceCIE76(Color c2) {
        return distanceLab(c2);
    }

    // Uses the CIE94 formula to calculate color distance. More accurate than
    // DistanceLab, but also more work.
    default double distanceCIE94(Color cr) {
        return this.Lab().distanceCIE94(cr.Lab());
    }



    private static double[][] getBounds(double l) {
        final double kappa = 903.2962962962963;
        final double epsilon = 0.0088564516790356308;

        final double[][] m = {
                {3.2409699419045214, -1.5373831775700935, -0.49861076029300328},
                {-0.96924363628087983, 1.8759675015077207, 0.041555057407175613},
                {0.055630079696993609, -0.20397695888897657, 1.0569715142428786},
        };

        double sub2;
        var ret = new double[6][2];
        var sub1 = Math.pow(l+16.0, 3.0) / 1560896.0;
        if (sub1 > epsilon) {
            sub2 = sub1;
        } else {
            sub2 = l / kappa;
        }
        for (int i = 0; i < m.length; i++) {
            for (int k = 0; k < 2; k++) {
                var top1 = (284517.0*m[i][0] - 94839.0*m[i][2]) * sub2;
                var top2 = (838422.0*m[i][2]+769860.0*m[i][1]+731718.0*m[i][0])*l*sub2 - 769860.0*((double) k)*l;
                var bottom = (632260.0*m[i][2]-126452.0*m[i][1])*sub2 + 126452.0*((double) k);
                ret[i*2+k][0] = top1 / bottom;
                ret[i*2+k][1] = top2 / bottom;
            }
        }

        return ret;
    }

    private static double lengthOfRayUntilIntersect(double theta, double x, double y) {
        return y / (Math.sin(theta) - x*Math.cos(theta));
    }

    static double maxChromaForLH(double l, double h) {
        var hRad = h / 360.0 * Math.PI * 2.0;
        var minLength = Double.MAX_VALUE;
        for (var line : getBounds(l)) {
            var length = lengthOfRayUntilIntersect(hRad, line[0], line[1]);
            if (length > 0.0 && length < minLength) {
                minLength = length;
            }
        }
        return minLength;
    }

    static double maxSafeChromaForL(double l) {
        var minLength = Double.MAX_VALUE;
        for (var line : getBounds(l)) {
            var m1 = line[0];
            var b1 = line[1];
            var x = intersectLineLine(m1, b1, -1.0/m1, 0.0);
            var dist = distanceFromPole(x, b1+x*m1);
            if (dist < minLength) {
                minLength = dist;
            }
        }
        return minLength;
    }

    private static double intersectLineLine(double x1, double y1, double x2, double y2) {
        return (y1 - y2) / (x2 - x1);
    }

    private static double distanceFromPole(double x, double y) {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
    }

    // DistanceLuv is a good measure of visual similarity between two colors!
    // A result of 0 would mean identical colors, while a result of 1 or higher
    // means the colors differ a lot.
    default double distanceLuv(Color c2) {
        return this.Luv().distance(c2.Luv());
    }

    // DistanceHSLuv calculates Euclidan distance in the HSLuv colorspace. No idea
    // how useful this is.
    //
    // The Hue value is divided by 100 before the calculation, so that H, S, and L
    // have the same relative ranges.
    default double distanceHSLuv(Color c2) {
        return this.HSLuv().distance(c2.HSLuv());
    }

    // DistanceHPLuv calculates Euclidean distance in the HPLuv colorspace. No idea
    // how useful this is.
    //
    // The Hue value is divided by 100 before the calculation, so that H, S, and L
    // have the same relative ranges.
    default double distanceHPLuv(Color c2) {
        return this.HPLuv().distance(c2.HPLuv());
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

    // allToAllDistancesCIEDE2000 computes the CIEDE2000 distance between each pair of
    // colors.  It returns a map from a pair of indices (u, v) with u < v to a
    // distance.
    private static <C extends Color> EdgeDistances allToAllDistances(List<C> cs, ColorDistance colorDistance) {
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
        var es = new ArrayList<EdgeIdxs>(m.value().size());
        es.addAll(m.value().keySet());

        es.sort(Comparator.comparing((i) -> m.value().getOrDefault(i, 0.0)));
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
            var u = uv._0();
            var v = uv._1();
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
            var u = uv._0();
            var v = uv._1();
            neighs.putIfAbsent(u, new ArrayList<>());
            neighs.get(u).add(v);
        }

        for (var vs : neighs.values()) {
            vs.sort(Comparator.naturalOrder());
        }

        // Walk the tree from a given vertex.
        ArrayList<Integer> order = new ArrayList<Integer>(neighs.size());
        Set<Integer> visited = new HashSet<>();

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

    static <C extends Color> List<C> sort(List<C> cs) {
        return sort(cs, Color::distanceCIEDE2000);
    }

    @SuppressWarnings("unchecked")
    static <C extends Color> List<C> sort(
            List<C> cs,
            ColorDistance colorDistance
    ) {
        if (cs.size() < 2) {
            return List.copyOf(cs);
        }

        // Do nothing in trivial cases.
        var newCs = new Color[cs.size()];

        var dists = allToAllDistances(cs, colorDistance);

        var edges = sortEdges(dists);

        var mst = minSpanTree(cs.size(), edges);

        // Find the darkest color in the list.
        var black = new sRGB(0, 0, 0);
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

        return List.of((C[]) newCs);
    }

    // BlendLab blends two colors in the L*a*b* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    default Color blendLab(Color c2, double t) {
        return this.Lab().blend(c2.Lab(), t);
    }

    default Color blendLab(Color c2) {
        return blendLab(c2, 0.5);
    }

    // BlendLuv blends two colors in the CIE-L*u*v* color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    default Color blendLuv(Color c2, double t) {
        var c1 = this;

        switch (c1.Luv()) {
            case Luv(double l1, double u1, double v1) -> {
                switch (c2.Luv()) {
                    case Luv(double l2, double u2, double v2) -> {
                        return new Luv(l1 + t * (l2 - l1),
                                u1 + t * (u2 - u1),
                                v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    default Color blendLuv(Color c2) {
        return blendLuv(c2, 0.5);
    }

    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    default Color blendRGB(Color other, double t) {
        var c1 = this.sRGB();
        var c2 = other.sRGB();
        return new sRGB(
                c1.R() + t * (c2.R() - c1.R()),
                c1.G() + t * (c2.G() - c1.G()),
                c1.B() + t * (c2.B() - c1.B())
        );
    }

    default Color blendRGB(Color c2) {
        return blendRGB(c2, 0.5);
    }

    // BlendLinearRGB blends two colors in the Linear RGB color-space.
    // Unlike BlendRGB, this will not produce dark color around the center.
    // t == 0 results in c1, t == 1 results in c2
    default Color blendLinearRGB(Color c2, double t) {
        var c1 = this;
        switch (c1.LinearRGB()) {
            case LinearRGB(double r1, double g1, double b1) -> {
                switch (c2.LinearRGB()) {
                    case LinearRGB(double r2, double g2, double b2) -> {
                        return new LinearRGB(
                                r1 + t * (r2 - r1),
                                g1 + t * (g2 - g1),
                                b1 + t * (b2 - b1)
                        );
                    }
                }
            }
        }
    }

    default Color blendLinearRGB(Color c2) {
        return blendLinearRGB(c2, 0.5);
    }


    // You don't really want to use this, do you? Go for BlendLab, BlendLuv or BlendHcl.
    default Color blendHSV(Color c2, double t) {
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
                        return new HSV(interp_angle(h1, h2, t), s1 + t * (s2 - s1), v1 + t * (v2 - v1));
                    }
                }
            }
        }
    }

    default Color blendHSV(Color c2) {
        return blendHSV(c2, 0.5);
    }

    // BlendHcl blends two colors in the CIE-L*C*h° color-space, which should result in a smoother blend.
    // t == 0 results in c1, t == 1 results in c2
    default Color blendHCL(Color col2, double t) {
        var col1 = this;

        switch (col1.HCL()) {
            case LabLCh(var l1, var c1, var h1) -> {
                switch (col2.HCL()) {
                    case LabLCh(var l2, var c2, var h2) -> {
                        // https://github.com/lucasb-eyer/go-colorful/pull/60
                        if (c1 <= 0.00015 && c2 >= 0.00015) {
                            h1 = h2;
                        } else if (c2 <= 0.00015 && c1 >= 0.00015) {
                            h2 = h1;
                        }

                        // We know that h are both in [0..360]
                        return new LabLCh(
                                l1 + t * (l2 - l1),
                                c1 + t * (c2 - c1),
                                interp_angle(h1, h2, t)
                        )
                                .sRGB()
                                .clamped();
                    }
                }
            }
        }
    }

    default Color blendHCL(Color col2) {
        return blendHCL(col2, 0.5);
    }

    // BlendLuvLCh blends two colors in the cylindrical CIELUV color space.
    // t == 0 results in c1, t == 1 results in c2
    default Color blendLuvLCh(Color col2, double t) {
        return this.LuvLCh().blend(col2.LuvLCh(), t);
     }

     default Color blendLuvLCh(Color col2) {
         return this.LuvLCh().blend(col2.LuvLCh());
     }

    static Color warm() {
        return warm(ThreadLocalRandom.current());
    }

    /// Creates a random dark, "warm" color through restricted HCL space.
    /// This is slower than FastWarmColor but will likely give you colors which have
    /// the same "warmness" if you run it many times.
    ///
    /// @param random The random number generator to use.
    /// @return A random dark, "warm" color.
    static Color warm(RandomGenerator random) {
        sRGB c = randomWarm(random).sRGB();
        while (!c.isValid()) {
            c = randomWarm(random).sRGB();
        }
        return c;
    }

    static Color fastWarm() {
        return fastWarm(ThreadLocalRandom.current());
    }

    // Creates a random dark, "warm" color through a restricted HSV space.
    static Color fastWarm(RandomGenerator random) {
        return new HSV(
                random.nextDouble()*360.0,
                0.5+random.nextDouble()*0.3,
                0.3+random.nextDouble()*0.3
        );
    }

    private static Color randomWarm(RandomGenerator random) {
        return new LabLCh(
                0.1 + random.nextDouble() * 0.3,
                0.2 + random.nextDouble() * 0.3,
                random.nextDouble() * 360.0
        );
    }

    static Color fastHappy() {
        return fastHappy(ThreadLocalRandom.current());
    }

    // Creates a random bright, "pimpy" color through a restricted HSV space.
    static Color fastHappy(RandomGenerator random) {
        return new HSV(
                random.nextDouble()*360.0,
                0.7+random.nextDouble()*0.3,
                0.6+random.nextDouble()*0.3
        );
    }

    static Color happy() {
        return happy(ThreadLocalRandom.current());
    }

    // Creates a random bright, "pimpy" color through restricted HCL space.
    // This is slower than FastHappyColor but will likely give you colors which
    // have the same "brightness" if you run it many times.
    static Color happy(RandomGenerator random) {
        sRGB c = randomPimp(random).sRGB();
        while (!c.isValid()) {
            c = randomPimp(random).sRGB();
        }
        return c;
    }

    private static Color randomPimp(RandomGenerator random) {
        return new LabLCh(
                0.5+random.nextDouble()*0.3,
                0.5+random.nextDouble()*0.3,
                random.nextDouble()*360.0
        );
    }

    // Check for equality between colors within the tolerance Delta (1/255).
    default boolean almostEqualRGB(Color other, double delta) {
        var c1 = this.sRGB();
        var c2 = other.sRGB();
        return Math.abs(c1.R() - c2.R()) +
                Math.abs(c1.G() - c2.G()) +
                Math.abs(c1.B() - c2.B()) < 3.0 * delta;
    }

    // Check for equality between colors within the tolerance Delta (1/255).
    default boolean almostEqualRGB(Color c2) {
        double DELTA = 1.0 / 255;
        return almostEqualRGB(c2, DELTA);
    }

    static List<Color> fastWarm(int colorsCount) {
        return fastWarm(colorsCount, ThreadLocalRandom.current());
    }

     static List<Color> fastWarm(int colorsCount, RandomGenerator random) {
        Color[] colorArr = new Color[colorsCount];
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] = new HSV(i * (360.0 / ((double) colorsCount)), 0.55 + random.nextDouble() * 0.2, 0.35 + random.nextDouble() * 0.2);
        }

        return List.of(colorArr);
    }

    static List<Color> fastHappy(int colorsCount) {
        return fastHappy(colorsCount, ThreadLocalRandom.current());
    }

    static List<Color> fastHappy(int colorsCount, RandomGenerator random) {
        Color[] colorArr = new Color[colorsCount];
        for (int i = 0; i < colorArr.length; i++) {
            colorArr[i] =
                    new HSV(i * (360.0 / ((double) colorsCount)), 0.8 + random.nextDouble() * 0.2, 0.65 + random.nextDouble() * 0.2);
        }

        return List.of(colorArr);
    }

    static List<Color> soft(int colorsCount) {
        return soft(colorsCount, ThreadLocalRandom.current());
    }

    static List<Color> soft(int colorsCount, RandomGenerator random) {
        var settings = new PaletteGenerationSettings();
        settings.iterations = 50;
        settings.manySamples = false;

        return soft(colorsCount, settings, random);
    }

    private static List<Color> soft(int colorsCount, PaletteGenerationSettings settings, RandomGenerator random) {
        // Checks whether it's a valid RGB and also fulfills the potentially provided constraint.
        Predicate<Lab> check = (col) -> {
            var c = new Lab(col.L(), col.a(), col.b()).sRGB();
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
        Lab[] means = new Lab[colorsCount];
        for (int i = 0; i < colorsCount; i++) {
            for (
                    means[i] = samples.get(random.nextInt(samples.size()));
                    in(means, i, means[i]);
                    means[i] = samples.get(random.nextInt(samples.size()))
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
                            inewmean = random.nextInt(samples_used.length);
                            samples_used[inewmean];
                            inewmean = random.nextInt(samples_used.length)
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

    public static List<Color> warm(int colorsCount) {
        return warm(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> warm(int colorsCount, RandomGenerator random) {
        LabPredicate warmy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.1 <= c && c <= 0.4 && 0.2 <= l && l <= 0.5;
        };
        var settings = new PaletteGenerationSettings();
        settings.checkColor = warmy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings, random);
    }

    public static List<Color> happy(int colorsCount) {
        return happy(colorsCount, ThreadLocalRandom.current());
    }

    public static List<Color> happy(int colorsCount, RandomGenerator random) {
        LabPredicate pimpy = (lab) -> {
            var l = lab.L();
            var c = lab.HCL().C();
            return 0.3 <= c && 0.4 <= l && l <= 0.8;
        };
        var settings = new PaletteGenerationSettings();
        settings.checkColor = pimpy;
        settings.iterations = 50;
        settings.manySamples = true;
        return soft(colorsCount, settings, random);
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


    private static boolean lab_eq(Lab lab1, Lab lab2) {
        double LAB_DELTA = 1e-6;

        return Math.abs(lab1.L() - lab2.L()) < LAB_DELTA &&
                Math.abs(lab1.a() - lab2.a()) < LAB_DELTA &&
                Math.abs(lab1.b() - lab2.b()) < LAB_DELTA;
    }

    private static Color[] labs2cols(List<Lab> labs) {
        Color[] cols = new Color[labs.size()];
        for (int i = 0; i < labs.size(); i++) {
            var v = labs.get(i);
            cols[i] = v;
        }
        return cols;
    }
}

// An element represents a single element of a set.  It is used to
// implement a disjoint-set forest.
final class Element {
    // Parent element
    Element parent;
    // Rank (approximate depth) of the subtree with this element as root
    int rank;
}

record EdgeIdxs(int _0, int _1) {
}

record EdgeDistances(Map<EdgeIdxs, Double> value) {
}

final class PaletteGenerationSettings {
    static final int DEFAULT_ITERATIONS = 50;
    LabPredicate checkColor = __ -> true;
    int iterations = DEFAULT_ITERATIONS;
    boolean manySamples;
}