package it.angrybear.yagl.particles;

import it.fulminazzo.fulmicollection.structures.Tuple;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#TILE_BREAK} and {@link LegacyParticleType#TILE_DUST}.
 */
public class MaterialDataOption extends ParticleOption<Tuple<String, Integer>> {
    private static final String REGEX = "^([^\\[]+)(?:\\[(\\d+)])?$";
    private final @NotNull String material;
    private final Integer data;

    /**
     * Instantiates a new Material data option.
     *
     * @param materialData the material data
     */
    public MaterialDataOption(final @NotNull String materialData) {
        String[] parsed = BlockDataOption.parseRaw(materialData, REGEX);
        this.material = parsed[0];
        String rawData = parsed[1];
        if (rawData.trim().isEmpty()) this.data = 0;
        else this.data = Integer.valueOf(rawData);
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
