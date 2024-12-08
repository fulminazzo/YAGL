The `item` module main interest are **items**,
representation of **objects in game** that can be interacted with.

It provides a **unique, fast and reliable** system to **create items**, **modify attributes** such as 
**display name**, **lore**, **enchantments** and even add **custom recipes** bound to the item.
All without the overhead added by the **Bukkit ItemMeta** system:
```java
Item.newItem("diamond_sword")
    .setAmount(1)
    .setDurability(0)
    .setDisplayName("&e&lSuper sword")
    .setLore("&cThis sword belongs to", "&cthe greatest samurai of Tokyo")
    .addEnchantment("sharpness", 5).addEnchantment("unbreaking", 3)
    .addItemFlags(ItemFlag.HIDE_UNBREAKABLE)
    .setUnbreakable(true)
    .setCustomModelData(1337);
```

As already seen in [Wrappers](../wrappers), also `item` supports **saving and loading** using the [serializer](#serializer) submodule, 
without any extra work from the developer. It is possible to save the item in a **nice format for the administrator**, 
who will encounter no issue into **modifying and customizing it** to fit their needs.

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
    <groupId>it.fulminazzo.YAGL.item</groupId>
    <artifactId>item-{MODULE}</artifactId>
    <version>{LATEST}</version>
</dependency>
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```xml
<dependency>
    <groupId>it.fulminazzo.YAGL</groupId>
    <artifactId>item</artifactId>
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
    implementation 'it.fulminazzo.YAGL.item:item-{MODULE}:{LATEST}'
}
```
substitute `{MODULE}` with one among `base`, `serializer` or `bukkit`,
and `{LATEST}` the **version of interest**.

To import **all three submodules together**, use:
```groovy
dependencies {
    implementation 'it.fulminazzo.YAGL:item:{LATEST}'
}
```

## Submodules
### Base

### Serializer

### Bukkit
