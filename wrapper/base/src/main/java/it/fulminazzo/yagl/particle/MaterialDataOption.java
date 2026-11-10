package it.fulminazzo.yagl.particle;

import it.fulminazzo.yagl.structure.Pair;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

/**
 * An option used by {@link LegacyParticleType#TILE_BREAK} and {@link LegacyParticleType#TILE_DUST}.
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public final class MaterialDataOption extends ParticleOption<Pair<String, Integer>> {
    private final @NotNull String material;
    private final @NotNull Integer data;

    /**
     * Instantiates a new Material data option.
     *
     * @param materialData the material data
     */
    public MaterialDataOption(final @NotNull String materialData) {
        String[] parsed = BlockDataOption.parseRaw(materialData);
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
    public @NotNull Pair<String, Integer> getOption() {
        return new Pair<>(this.material, this.data);
    }

}
