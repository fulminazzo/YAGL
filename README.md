**Yet Another GUI Library** (**YAGL**) is a library thought to be as **platform independent** as possible from **Minecraft**.

Its core functions relate to the [Bukkit project](https://dev.bukkit.org/), but one can use the **base** modules across any preferred platform.

Since every module provides a **serializer** one to load and save the objects,
it is possible to store or create **GUIs** and **custom Items** in unrelated platforms;
then, send the actual data to a [Bukkit](https://dev.bukkit.org/) backend and display it.

For example, a `/hub` plugin that works with [BungeeCord](https://www.spigotmc.org/wiki/bungeecord/) or [Velocity](https://papermc.io/software/velocity).

## Structure
As anticipated, every module is composed of the **same structure**.
There are **three submodules** present:
- **base**: contains the base functionality of the module;
- **serializer**: contains serializers and parsers for [YAMLParser](https://github.com/Fulminazzo/YAMLParser) to handle data;
- **bukkit**: the actual implementation of the base module for [Bukkit](https://dev.bukkit.org/).

You can access them individually, by specifying them as `<main-module-name>-<name>`, or access all of them from the main module.

For example, with the [wrappers](wrappers/README.md) module:
- access only the **base**:
```xml
<dependency>
    <groupId>it.angrybear.yagl.wrappers</groupId>
    <artifactId>wrappers-base</artifactId>
    <version>LATEST</version>
</dependency>
```
- access **base**, **bukkit** and **serializer**:
```xml
<dependency>
    <groupId>it.angrybear.yagl</groupId>
    <artifactId>wrappers</artifactId>
    <version>LATEST</version>
</dependency>
```