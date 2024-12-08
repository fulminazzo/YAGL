`common` is a base module which contains useful utility functions and classes, much like a library.
While being used through the whole **YAGL project**, by itself it has **minimum use**,
therefore it is suggested to **not use it**.

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
  <groupId>it.fulminazzo.YAGL.common</groupId>
  <artifactId>common-{MODULE}</artifactId>
  <version>{LATEST}</version>
</dependency>
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`, 
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```xml
<dependency>
  <groupId>it.fulminazzo.YAGL</groupId>
  <artifactId>common</artifactId>
  <version>{LATEST}</version>
</dependency>
```
  
## Submodules
### Base
The `base` submodule contains various **special data structures** and **utility classes**
for **enums**, **objects** and more.

### Serializer
The `serializer` submodule olds few **important classes** for successfully **serializing** and **deserializing**
**YAGL data**.

### Bukkit
At the time of writing, the `bukkit` module does nothing.
