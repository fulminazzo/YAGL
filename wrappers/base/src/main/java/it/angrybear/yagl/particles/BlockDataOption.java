package it.angrybear.yagl.particles;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO: javadoc
public class BlockDataOption extends ParticleOption<String> {
    private static final String NBT_REGEX = "^([^\\[]*)(?:\\[(.*)])?$";
    @Getter
    private final String material;
    private final String nbt;

    /**
     * Instantiates a new Block data option.
     *
     * @param blockData the raw block data
     */
    public BlockDataOption(final @NotNull String blockData) {
        Matcher matcher = Pattern.compile(BlockDataOption.NBT_REGEX).matcher(blockData);
        if (matcher.matches()) {
            this.material = parseMaterial(matcher.group(1));
            this.nbt = matcher.groupCount() > 1 ? matcher.group(2) : "";
        } else throw new IllegalArgumentException(String.format("Invalid block data '%s'", blockData));
    }

    /**
     * Instantiates a new Block data option.
     *
     * @param material the material
     * @param nbt      the nbt
     */
    public BlockDataOption(final @NotNull String material, final @NotNull String nbt) {
        this.material = parseMaterial(material);
        this.nbt = nbt;
    }

    /**
     * Gets nbt.
     *
     * @return the nbt
     */
    public String getNBT() {
        return this.nbt;
    }

    private String parseMaterial(@NotNull String material) {
        material = material.toLowerCase();
        if (material.startsWith("minecraft:")) material = material.substring("minecraft:".length());
        if (material.trim().isEmpty() || Pattern.compile("[\r\n\t :]").matcher(material).find())
            throw new IllegalArgumentException(String.format("Invalid material '%s'", material));
        return material;
    }

    @Override
    public String getOption() {
        String output = this.material;
        if (!this.nbt.isEmpty()) output += String.format("[%s]", this.nbt);
        System.out.println("Returning option" + output);
        return output;
    }
}
