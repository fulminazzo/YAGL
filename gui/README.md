

| Table of contents               |
|---------------------------------|
| [How to import](#how-to-import) |
| [Maven](#maven)                 |
| [Gradle](#gradle)               |
| [Submodules](#submodules)       |
| [Base](#base)                   |
| [Serializer](#serializer)       |
| [Bukkit](#bukkit)               |

## How to import
It can be imported using both **Maven** and **Gradle** and specifying **one of its submodules**.

### Maven
First introduce the **Fulminazzo repository**, from where the artifacts will be downloaded.
```xml
<repository>
    <id>fulminazzo-repo</id>
    <url>https://repo.fulminazzo.it/releases</url>
</repository>
```

Then, add the **dependency**
```xml
<dependency>
    <groupId>it.fulminazzo.YAGL.gui</groupId>
    <artifactId>gui-{MODULE}</artifactId>
    <version>{LATEST}</version>
</dependency>
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```xml
<dependency>
    <groupId>it.fulminazzo.YAGL</groupId>
    <artifactId>gui</artifactId>
    <version>{LATEST}</version>
</dependency>
```

### Gradle
First introduce the **Fulminazzo repository**, from where the artifacts will be downloaded.
```groovy
repositories {
    maven { url = "https://repo.fulminazzo.it/releases" }
}
```

Then, add the **dependency**
```groovy
dependencies {
    implementation 'it.fulminazzo.YAGL.gui:gui-{MODULE}:{LATEST}'
}
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```groovy
dependencies {
    implementation 'it.fulminazzo.YAGL:gui:{LATEST}'
}
```

## Submodules
### Base

### Serializer
The `serializer` submodule is entitled to **saving and loading** all the **guis related objects**.
A developer **should not be concerned** with the **contents** of this module as it should **never be used explicitly**.
However, for it to work properly, it is required to execute `GUIYAGLParser#addAllParsers()` **before any serialization operation**.
Note that this **will include calls to `WrappersYAGLParser#addAllParsers()` and `ItemYAGLParser#addAllParsers()`**,
therefore it is **not necessary** to include them later.

### Bukkit
