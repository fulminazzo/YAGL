`item` is the module designed to handling simple items designing and association of custom recipes.
It includes various submodules that can be used for specific tasks.

| **Modules**     |
|-----------------|
| [items](#items) |

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

