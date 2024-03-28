package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.structures.Tuple;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An option used by {@link LegacyParticleType#TILE_BREAK} and {@link LegacyParticleType#TILE_DUST}.
 */
@Getter
public class MaterialDataOption extends ParticleOption<Tuple<String, Integer>> {
    private static final String REGEX = "^([^\\[]*)(?:\\[(\\d+)])?$";
    private final @NotNull String material;
    private final Integer data;

    /**
     * Instantiates a new Material data option.
     *
     * @param materialData the material data
     */
    public MaterialDataOption(final @NotNull String materialData) {
        Matcher matcher = Pattern.compile(REGEX).matcher(materialData);
        if (matcher.matches()) {
            this.material = BlockDataOption.parseMaterial(matcher.group(1));
            String nbt = matcher.group(2);
            if (nbt == null || nbt.trim().isEmpty()) this.data = null;
            else this.data = Integer.parseInt(nbt.trim());
        } else throw new IllegalArgumentException(String.format("Invalid material data '%s'", materialData));
    }

    /**
     * Instantiates a new Material data option.
     *
     * @param material the material
     * @param data     the data
     */
    public MaterialDataOption(final @NotNull String material, final int data) {
        this.material = BlockDataOption.parseMaterial(material);
        this.data = data;
    }

    @Override
    public Tuple<String, Integer> getOption() {
        return new Tuple<>(this.material, this.data);
    }
}
