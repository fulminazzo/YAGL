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
The `base` submodule contains many classes responsible for **creation and customization** of **items**,
but the most important (and useful) ones are [Item](https://github.com/Fulminazzo/YAGL/wiki/Item-For-Developers#items)
and [Recipes](https://github.com/Fulminazzo/YAGL/wiki/Item-For-Developers#recipes).

### Serializer
The `serializer` submodule is entitled to **saving and loading** all the **items related objects**.
A developer **should not be concerned** with the **contents** of this module as it should **never be used explicitly**.
However, for it to work properly, it is required to execute `ItemYAGLParser#addAllParsers()` **before any serialization operation**.
Note that this **will include calls to `WrappersYAGLParser#addAllParsers()`**,
therefore it is **not necessary** to include it later.

### Bukkit
`bukkit` allows the direct **conversion** from a **YAGL `Item`** to a **Bukkit `ItemStack`**.
It does so thanks to
[ItemAdapter](bukkit/src/main/java/it/fulminazzo/yagl/ItemAdapter.java) and
[BukkitItem](bukkit/src/main/java/it/fulminazzo/yagl/items/BukkitItem.java),
a special **extension of `Item`** with which it is possible to work **directly** with **Bukkit classes** such as
`Material`, or `ItemMeta`.
More specifically, thanks to the `BukkitItem#create(Class, Consumer)`, it is possible to **create an `ItemStack` from an `Item`**
by **manipulating its `ItemMeta` first**.
