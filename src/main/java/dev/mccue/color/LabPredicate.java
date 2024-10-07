package dev.mccue.color;

import java.util.function.Predicate;

/// Says whether a color in the {@link Lab} color space
/// passes some test.
///
/// Used in palette generation.
interface LabPredicate extends Predicate<Lab> {
}
