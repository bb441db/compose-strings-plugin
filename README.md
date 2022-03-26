# Compose Strings Plugin

## Example Usage
```xml
<resources>
    <string name="main.hello">Hello %1$s!</string>
    <string name="main.world">World</string>
</resources>
```

```kt
// String resource without any format specifiers will become getters
@Composable
fun MainView() {
    Greeter(name = Strings.Main.world)
}

// Resources with at least one format specifier will become a composable function
@Composable
fun Greeter(name: String) {
    Text(text = Strings.Main.hello(p1 = name))
}
```

## Generated Code Example
```kt
public object Strings {
  public object App {
    public val name: String
      @Composable
      get() = stringResource(id = R.string.app_name)
  }

  public object Main {
    public val world: String
      @Composable
      get() = stringResource(id = R.string.main_world)

    @Composable
    public fun hello(p1: String): String = stringResource(id = R.string.main_hello, p1)
  }
}
```

## TODOs
* Convert generated `object`'s to `namespace` when added to kotlin [KT-11968](https://youtrack.jetbrains.com/issue/KT-11968)
* Named arguments instead of p1, p2, p3...
* Copy comments in XML file to the generated functions