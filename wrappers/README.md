The `wrappers` module is the most **innovative** and **unique technology** in the library.
It **apparently** does nothing apart from introducing more overhead to a project.
But its real power is the capability to **extract Minecraft objects** and making them 
**independent, portable and cross-version compatible**.
To the developer is given the **power** to **easily serialize and deserialize Minecraft features**
like **sounds**, **enchantments** and more, while still allowing an **easy maintenance** for the user,
thanks to its simple **modification system**.
Not only that, but the coder can also **transfer with ease** any object across **versions** or **even platforms**.

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
    <groupId>it.fulminazzo.YAGL.wrappers</groupId>
    <artifactId>wrappers-{MODULE}</artifactId>
    <version>{LATEST}</version>
</dependency>
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```xml
<dependency>
    <groupId>it.fulminazzo.YAGL</groupId>
    <artifactId>wrappers</artifactId>
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
    implementation 'it.fulminazzo.YAGL.wrappers:wrappers-{MODULE}:{LATEST}'
}
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```groovy
dependencies {
    implementation 'it.fulminazzo.YAGL:wrappers:{LATEST}'
}
```
  
## Submodules
### Base


### Serializer


### Bukkit

