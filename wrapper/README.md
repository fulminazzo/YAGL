The `wrapper` module is the most **innovative** and **unique technology** in the library.

It **apparently** does nothing apart from introducing more overhead to a project.

But its real power is the capability to **extract Minecraft objects** and making them 
**independent, portable and cross-version compatible**.

To the developer is given the **power** to **easily serialize and deserialize Minecraft features**
like **sounds**, **enchantments** and more, while still allowing an **easy maintenance** for the user,
thanks to its simple **modification system**.

Not only that, but the coder can also **transfer with ease** any object across **versions** or **even platforms**.

| **Table of Contents**           |
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
    <groupId>it.fulminazzo.yagl.wrapperit.fulminazzo.yagl.wrapper</groupId>
    <artifactId>wrapper-{MODULE}</artifactId>
    <version>{LATEST}</version>
</dependency>
```

substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:

```xml
<dependency>
    <groupId>it.fulminazzo.yagl</groupId>
    <artifactId>wrapper</artifactId>
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
    implementation 'it.fulminazzo.yagl.wrapper:wrapper-{MODULE}:{LATEST}'
}
```

substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:

```groovy
dependencies {
    implementation 'it.fulminazzo.yagl:wrapper:{LATEST}'
}
```

## Submodules

### Base

`base` contains the **core** of the module.
It holds the most important classes as
[Enchantment](../wiki/Wrappers-For-Developers#enchantment),
[PotionEffect](../wiki/Wrappers-For-Developers#potioneffect),
[Sound](../wiki/Wrappers-For-Developers#sound) and
[Particle](../wiki/Wrappers-For-Developers#particle).

### Serializer

The `serializer` submodule is entitled to **saving and loading** all the **wrapper objects**.
A developer **should not be concerned** with the **contents** of this module as it should **never be used explicitly**.
However, for it to work properly, it is required to execute `WrappersYAGLParser#addAllParsers()` **before any serialization operation**.

### Bukkit

The most useful class introduced by this module is 
[WrappersAdapter](bukkit/src/main/java/it/fulminazzo/yagl/WrappersAdapter.java),
which allows a **one to one conversion** for any **Bukkit object** to a **YAGL one** or **viceversa**.
