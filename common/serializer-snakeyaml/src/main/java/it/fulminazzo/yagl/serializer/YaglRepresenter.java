package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

/**
 * A custom representer for SnakeYAML to serialize YAGL objects.
 * <br>
 * Used in conjunction with {@link YaglConstructor},
 * they can be used to serialize and deserialize YAGL objects
 * (and custom types, registered through {@link SerializersLoader})
 * directly from the SnakeYAML API.
 * <br>
 * Usage:
 * <pre>{@code
 * LoaderOptions loaderOptions = new LoaderOptions();
 * DumpOptions dumpOptions = new DumpOptions();
 *
 * YaglConstructor constructor = new YaglConstructor(loaderOptions);
 * YaglRepresenter representer = new YaglRepresenter(dumpOptions);
 *
 * Yaml yaml = new Yaml(constructor, representer, dumperOptions, loaderOptions);
 * }</pre>
 *
 * @see YaglConstructor
 * @see Serializer
 * @see SerializersLoader
 * @see YaglSerializer
 */
public final class YaglRepresenter extends Representer {
    private final @NotNull YaglSerializer yaglSerializer = YaglSerializer.getInstance();

    public YaglRepresenter(final @NotNull DumperOptions options) {
        super(options);
        Represent represent = new YaglRepresent();
        for (Class<?> type : yaglSerializer.getRegisteredTypes())
            this.multiRepresenters.put(type, represent);
    }

    private final class YaglRepresent implements Represent {

        @Override
        public Node representData(final @Nullable Object data) {
            try {
                return YaglRepresenter.this.representData(yaglSerializer.serialize(data));
            } catch (SerializationException e) {
                throw new YAMLException(e);
            }
        }

    }


}
