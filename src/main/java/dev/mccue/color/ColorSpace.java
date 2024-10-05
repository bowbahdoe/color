package dev.mccue.color;

public interface ColorSpace<Components> {
    Components fromColor(Color c);

    Color toColor(Components components);

    static ColorSpace<HSV> HSV() {
        return HSVColorSpace.INSTANCE;
    }

    static ColorSpace<HSL> HSL() {
        return HSLColorSpace.INSTANCE;
    }

    static ColorSpace<sRGB> sRGB() {
        return sRGBColorSpace.INSTANCE;
    }

    static ColorSpace<String> hex() {
        return HexColorSpace.INSTANCE;
    }

    static ColorSpace<LinearRGB> linearRGB() {
        return LinearRGBColorSpace.INSTANCE;
    }

    static ColorSpace<LinearRGB> fastLinearRGB() {
        return FastLinearRGBColorSpace.INSTANCE;
    }

    static ColorSpace<XYZ> XYZ() {
        return XYZColorSpace.INSTANCE;
    }

    static ColorSpace<Lab> Lab() {
        return LabColorSpace.INSTANCE;
    }

    static ColorSpace<Luv> Luv() {
        return LuvColorSpace.INSTANCE;
    }

    static ColorSpace<HCL> HCL() {
        return HCLColorSpace.INSTANCE;
    }

    static ColorSpace<RGB255> RGB255() {
        return RGB255ColorSpace.INSTANCE;
    }

    static ColorSpace<OkLab> OkLab() {
        return OkLabColorSpace.INSTANCE;
    }
}
