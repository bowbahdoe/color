package dev.mccue.color;

public record OkLch(
        double L,
        double c,
        double h
) {
    OkLab OkLab() {
        var h = this.h * Math.PI / 180;
        var a = c * Math.cos(h);
        var b = c * Math.sin(h);
        return new OkLab(L, a, b);
    }

    XYZ XYZ() {
        return OkLab().XYZ();
    }
}
