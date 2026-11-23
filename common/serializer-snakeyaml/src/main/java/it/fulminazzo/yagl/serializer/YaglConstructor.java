package it.fulminazzo.yagl.serializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.*;

/**
 * A custom constructor for SnakeYAML to deserialize YAGL objects.
 * <br>
 * Used in conjunction with {@link YaglRepresenter},
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
 * @see YaglRepresenter
 * @see Serializer
 * @see SerializersLoader
 * @see YaglSerializer
 */
public final class YaglConstructor extends SafeConstructor {
    private final @NotNull YaglSerializer yaglSerializer = YaglSerializer.getInstance();
    private final @NotNull Construct yaglConstruct = new YaglConstruct();
    private @Nullable Class<?> rootType;

    public YaglConstructor(final @NotNull LoaderOptions options) {
        super(options);
    }

    @Override
    public Object getSingleData(final @NotNull Class<?> type) {
        this.rootType = type;
        try {
            return super.getSingleData(type);
        } finally {
            this.rootType = null;
        }
    }

    @Override
    protected Construct getConstructor(final @NotNull Node node) {
        Class<?> root = this.rootType;
        if (root != null && node.getTag().equals(new Tag(root)) && yaglSerializer.hasSerializer(root))
            return yaglConstruct;
        else return super.getConstructor(node);
    }

    private final class YaglConstruct implements Construct {

        @Override
        public Object construct(final Node node) {
            final Tag originalTag = node.getTag();
            try {
                node.setTag(Tag.MAP);
                Class<?> type = Objects.requireNonNull(rootType);
                Construct standard = YaglConstructor.super.getConstructor(node);
                Object raw = standard.construct(node);
                if (node.isTwoStepsConstruction()) standard.construct2ndStep(node, raw);
                try {
                    return yaglSerializer.deserialize(normalizeKeys(raw), type);
                } catch (SerializationException e) {
                    throw new YAMLException("Invalid YAGL data at " + node.getStartMark() + ": " + e.getMessage(), e);
                }
            } finally {
                node.setTag(originalTag);
            }
        }

        @Override
        public void construct2ndStep(final @NotNull Node node, final @NotNull Object object) {
            // nothing else to set, already covered in initialization
        }
    }

    /**
     * Since SnakeYAML also parses types in keys, this operation is mandatory for ensuring
     * compatibility with the YAGL system.
     *
     * @param object the object to normalize
     * @return the normalized object
     */
    private static @Nullable Object normalizeKeys(final @Nullable Object object) {
        if (object instanceof Map<?, ?>) {
            Map<String, Object> result = new LinkedHashMap<>();
            ((Map<?, ?>) object).forEach((k, v) -> result.put(String.valueOf(k), normalizeKeys(v)));
            return result;
        }
        if (object instanceof List<?>) {
            List<Object> result = new ArrayList<>();
            for (Object e : (List<?>) object) result.add(normalizeKeys(e));
            return result;
        }
        return object;
    }

}
