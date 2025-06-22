`gui` is the main focus of the **whole YAGL project**.
They represent **visual menus** with which the **user** can **interact, view data and execute actions**.
**YAGL** introduces an **advanced system** to include **as many features as possible**
while still **maintaining simplicity and ease of use**.

Keep in mind that **`gui` relies heavily on the [Item](../item) and [Wrappers](../wrapper) modules**,
therefore it is suggested to study and learn from their documentations before approaching `gui`.

It is also **thanks to these modules** that `gui` is capable of **storing and loading data** 
in a **simple yet convenient way** for both **developers and admins**.

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
    <groupId>it.fulminazzo.yagl.gui</groupId>
    <artifactId>gui-{MODULE}</artifactId>
    <version>{LATEST}</version>
</dependency>
```

substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:

```xml
<dependency>
    <groupId>it.fulminazzo.yagl</groupId>
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
    implementation 'it.fulminazzo.yagl.gui:gui-{MODULE}:{LATEST}'
}
```

substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:

```groovy
dependencies {
    implementation 'it.fulminazzo.yagl:gui:{LATEST}'
}
```

## Submodules

### Base

The `base` submodules introduces all the **components and features** of the module.
It provides all the [types of GUIs](../wiki/GUI-For-Developers#types)
with their associated classes, all the [GUI contents](../wiki/GUI-Contents-For-Developers)
as well as different types of [actions](../wiki/GUI-For-Developers#actions).
All of these and more can be found in the 
[detailed documentation in the Wiki](../wiki/GUI-For-Developers).

### Serializer

The `serializer` submodule is entitled to **saving and loading** all the **guis related objects**.
A developer **should not be concerned** with the **contents** of this module as it should **never be used explicitly**.
However, for it to work properly, it is required to execute `GUIYAGLParser#addAllParsers()` **before any serialization operation**.
Note that this **will include calls to `WrappersYAGLParser#addAllParsers()` and `ItemYAGLParser#addAllParsers()`**,
therefore it is **not necessary** to include them later.

### Bukkit

The `bukkit` module contains **Bukkit** related objects and converters.
It provides two main classes:

- [GUIManager](bukkit/src/main/java/it/fulminazzo/yagl/GUIManager.java), 
  which acts as a **listener** for all the **GUI events**. 
  As already stated from the Javadoc, the user **should not register it** in their plugin,
  since this process is **automatic**;
- [GUIAdapter](bukkit/src/main/java/it/fulminazzo/yagl/GUIAdapter.java),
  which allows the developer to directly **convert YAGL GUIs to Bukkit inventories** and viceversa.
