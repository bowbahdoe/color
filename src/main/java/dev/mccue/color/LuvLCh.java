package dev.mccue.color;

public record LuvLCh(double L, double C, double h) {
    private static final double kappa = 903.2962962962963;
    private static final double epsilon = 0.0088564516790356308;

    private  static final double[][] m = {
        {3.2409699419045214, -1.5373831775700935, -0.49861076029300328},
        {-0.96924363628087983, 1.8759675015077207, 0.041555057407175613},
        {0.055630079696993609, -0.20397695888897657, 1.0569715142428786},
    };

    private static double[][] getBounds(double l) {
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

    private static double maxChromaForLH(double l, double h) {
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

    HSLuv HSLuv() {
        // [-1..1] but the code expects it to be [-100..100]
        var c = C * 100.0;
        var l = L * 100.0;

        double s;
        double max;
        if (l > 99.9999999 || l < 0.00000001) {
            s = 0.0;
        } else {
            max = maxChromaForLH(l, h);
            s = c / max * 100.0;
        }
        return new HSLuv(
                h, Math.clamp(s / 100.0, 0, 1), Math.clamp(l / 100.0, 0, 1)
        );
    }
}
