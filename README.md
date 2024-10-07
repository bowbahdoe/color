# color

Color library for the JVM.

Heavily based on [go-colorful](https://github.com/lucasb-eyer/go-colorful/tree/master)
by [Lucas Beyer](https://github.com/lucasb-eyer).


## Usage

You can create a color using one of the static factory methods on `Color`.

```java
var red = Color.sRGB(1, 0, 0);
var blue = Color.hex("#0000FF");
```

And you can get the components of a color using matching instance methods.
