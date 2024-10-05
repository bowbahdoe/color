package dev.mccue.color;

/// Components of a color in the OkLab color space.
///
/// @see <a href="https://bottosson.github.io/posts/oklab/">https://bottosson.github.io/posts/oklab/</a>
public record OkLab(
        /// Perceived lightness
        double L,
        /// How green/red the color is
        double a,
        /// How blue/yellow the color is
        double b
) {
}
