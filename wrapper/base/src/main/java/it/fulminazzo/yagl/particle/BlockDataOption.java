package it.fulminazzo.yagl.particle;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An option used by {@link ParticleType#BLOCK_CRACK}, {@link ParticleType#BLOCK_DUST} and {@link ParticleType#FALLING_DUST}.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public final class BlockDataOption extends ParticleOption<String> {
    static final Pattern NBT_REGEX = Pattern.compile("^([^\\[]+)(?:\\[(.*)])?$");
    private static final Pattern INVALID_MATERIAL_REGEX = Pattern.compile("[\r\n\t :]");

    private static final String INVALID_DATA = "Invalid data '%s'";
    private static final String INVALID_MATERIAL = "Invalid material '%s'";
    private static final String MINECRAFT_IDENTIFIER = "minecraft:";

    @Getter
    private final @NotNull String material;
    private final @NotNull String nbt;

    /**
     * Instantiates a new Block data option.
     *
     * @param blockData the raw block data
     */
    public BlockDataOption(final @NotNull String blockData) {
        String[] parsed = parseRaw(blockData);
        this.material = parsed[0];
        this.nbt = parsed[1];
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
     * Gets the NBT tag associated with this option.
     *
     * @return the nbt
     */
    public @NotNull String getNBT() {
        return this.nbt;
    }

    /**
     * Converts the given data to a pair based on the NBT format.
     * Throws an {@link IllegalArgumentException} in case of failure.
     *
     * @param data the data
     * @return the pair
     */
    static String @NotNull [] parseRaw(final @NotNull String data) {
        Matcher matcher = BlockDataOption.NBT_REGEX.matcher(data);
        if (matcher.matches()) {
            String nbt = matcher.group(2);
            if (nbt == null) nbt = "";
            return new String[]{parseMaterial(matcher.group(1)), nbt.trim()};
        } else throw new IllegalArgumentException(String.format(INVALID_DATA, data));
    }

    /**
     * Parses the given raw material by removing any white-space or {@code minecraft:} prepending the actual material.
     * Throws an {@link IllegalArgumentException} in case the material is invalid.
     *
     * @param material the material
     * @return the string
     */
    static @NotNull String parseMaterial(@NotNull String material) {
        material = material.toLowerCase(Locale.ROOT);
        if (material.startsWith(MINECRAFT_IDENTIFIER)) material = material.substring(MINECRAFT_IDENTIFIER.length());
        if (material.trim().isEmpty() || INVALID_MATERIAL_REGEX.matcher(material).find())
            throw new IllegalArgumentException(String.format(INVALID_MATERIAL, material));
        return material;
    }

    @Override
    public @NotNull String getOption() {
        String output = this.material;
        if (!this.nbt.isEmpty()) output += '[' + this.nbt + ']';
        return output;
    }

}
