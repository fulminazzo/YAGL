`item` is the module designed to handling simple items designing and association of custom recipes.
It includes various submodules that can be used for specific tasks.

| **Modules**                 |
|-----------------------------|
| [items](#items)             |
| [bukkit](#bukkit)           |
| [serializers](#serializers) |

# items
```xml
<dependency>
    <groupId>it.angrybear.YAGL</groupId>
    <artifactId>item</artifactId>
    <version>LATEST</version>
</dependency>
```

This is the main core of the module. 
The most important class is [Item](src/main/java/it/angrybear/yagl/items/Item.java)
from which it is possible to create an item:

```java
/* ... */
Item.newItem();
/* ... */
```

In case you are interested in the recipe of the item, an instance of [RecipeItem](src/main/java/it/angrybear/yagl/items/RecipeItem.java)
is available using the function `Item#newRecipeItem`.

## Recipes
Recipes are a way of reconstructing specific items in the crafting table or in the furnace.
To mimic the Minecraft default types, three classes are available:
- [ShapedRecipe](src/main/java/it/angrybear/yagl/items/recipes/ShapedRecipe.java)
for recipes that require a specific shape.
One can set it using `ShapedRecipe#setRecipe(int int)` (maximum values: three rows, three columns);
- [ShapelessRecipe](src/main/java/it/angrybear/yagl/items/recipes/ShapelessRecipe.java)
for recipes that ignore the order in which the items are put;
- [FurnaceRecipe](src/main/java/it/angrybear/yagl/items/recipes/FurnaceRecipe.java)
for items resulted in smelting in the furnace.

# bukkit
```xml
<dependency>
    <groupId>it.angrybear.YAGL.item</groupId>
    <artifactId>bukkit</artifactId>
    <version>LATEST</version>
</dependency>
```

This module depends on the main core and adds some implementation specific features.

First of all, it adds the [BukkitItem](bukkit/src/main/java/it/angrybear/yagl/items/BukkitItem.java) class,
an implementation of [Item](src/main/java/it/angrybear/yagl/items/Item.java)
that provides the `BukkitItem#create()` and `BukkitItem#create(Class<M  extends ItemMeta>, Consumer<M extends ItemMeta>)` 
methods to convert an Item in Spigot ItemStacks.

## PersistentItem
An implementation of [BukkitItem](bukkit/src/main/java/it/angrybear/yagl/items/BukkitItem.java) are
[PersistentItem](bukkit/src/main/java/it/angrybear/yagl/items/PersistentItem.java) and
[MovablePersistentItem](bukkit/src/main/java/it/angrybear/yagl/items/MovablePersistentItem.java).
These are special items that prevents them from being moved from the player's inventory ([PersistentItem](bukkit/src/main/java/it/angrybear/yagl/items/PersistentItem.java) 
disallows movement even inside it).

**NOTE:** in order for this to function, it is required that [PersistentListener](bukkit/src/main/java/it/angrybear/yagl/listeners/PersistentListener.java)
has been registered by the providing plugin.

These provide various methods to handle events, specifically:
- `PersistentItem#setDeathAction`: one of the option from [DeathAction](bukkit/src/main/java/it/angrybear/yagl/persistent/DeathAction.java);
- `PersistentItem#onInteract`: executes the given [InteractItemAction](bukkit/src/main/java/it/angrybear/yagl/actions/InteractItemAction.java) upon interaction;
- `PersistentItem#onClick`: executes the given [ClickItemAction](bukkit/src/main/java/it/angrybear/yagl/actions/ClickItemAction.java) upon clicking.

# serializers
```xml
<dependency>
    <groupId>it.angrybear.YAGL.item</groupId>
    <artifactId>serializers</artifactId>
    <version>LATEST</version>
</dependency>
```

A simple module containing various classes to save items using [YAMLParser](https://github.com/Fulminazzo/YAMLParser).
Specifically, it is suggested `YAGLParser#addAllParsers()` to correctly load and save everything.