package it.angrybear.items;

import it.angrybear.utils.MaterialUtils;
import org.jetbrains.annotations.NotNull;

public class BukkitItem extends ItemImpl {

    @Override
    public Item setMaterial(@NotNull String material) {
        MaterialUtils.getMaterial(material, true);
        return super.setMaterial(material);
    }
}
