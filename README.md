# color

Color library for the JVM.

Heavily based on [go-colorful](https://github.com/lucasb-eyer/go-colorful/tree/master)
by [Lucas Beyer](https://github.com/lucasb-eyer).

```xml
<dependency>
    <groupId>dev.mccue</groupId>
    <artifactId>color</artifactId>
    <version>2024.10.09</version>
</dependency>
```

## Status

I am still working through this and working toward actually understanding
colors, color spaces, etc.

As such I might revisit some design choices without mercy. Notably the `HCL`
type might have a bad name and the auto-clamping behavior on some color space
component records like `sRGB` might be a bad call.

Keep that in mind if you want to use it for anything. You can always reach out
to me directly to make sure some particular piece of API surface is stable.

Other than that, I need to do a lot more documentation. Both javadocs and
tutorials for appropriate usage.

## Usage

You can create a color using one of the static factory methods on `Color`.

```java
var red = Color.sRGB(1, 0, 0);
var blue = Color.hex("#0000FF");
```

And you can get the components of a color in a particular color space using matching instance methods.

```java
// RGB in 0..1
var srgb = Color.hex("#00FF0F").sRGB();
// RBG in 0..255
var rgb = Color.hex("#00FF0F").RGB255();
// HSL
var hsl = Color.hex("#00FF0F").HSL();
```

To blend between different colors you can use the various blend methods.
These are tailored to blending in a particular color space.

```java
var red = Color.sRGB(1, 0, 0);
var blue = Color.hex("#0000FF");
var purple = red.blendLuv(blue);
```

There are also utilities for generating palettes of colors
and for sorting colors by their "distance" to each-other.

```java
var happy = Color.happy(10);
var sorted = Color.sort(happy);
```

