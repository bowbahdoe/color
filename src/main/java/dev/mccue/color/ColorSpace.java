package dev.mccue.color;

import java.lang.annotation.*;

/// Used to mark a type as being representative of a
/// color as represented in a color space.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface ColorSpace {
    String[] names();
    String[] references();
}
