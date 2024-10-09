package dev.mccue.color;

public record OkLch(
        double L,
        double c,
        double h
) implements Color {
    @Override
    public OkLch OkLch() {
        return this;
    }

    @Override
    public OkLab OkLab() {
        var h = this.h * Math.PI / 180;
        var a = c * Math.cos(h);
        var b = c * Math.sin(h);
        return new OkLab(L, a, b);
    }

    @Override
    public XYZ XYZ() {
        return OkLab().XYZ();
    }

    @Override
    public sRGB sRGB() {
        return OkLab().sRGB();
    }
}
