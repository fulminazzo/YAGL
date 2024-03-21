package it.angrybear.yagl.parsers;

import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandActionParser<C> extends YAMLParser<C> {

    public CommandActionParser(@NotNull Class<C> cClass) {
        super(cClass);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable C> getLoader() {
        return (c, s) -> {
            String a = c.getString(s);
            if (a == null) return null;
            return new Refl<>(getOClass(), a).getObject();
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable C> getDumper() {
        return (c, s, a) -> {
            c.set(s, null);
            if (a == null) return;
            c.set(s, new Refl<>(a).getFieldObject("command"));
        };
    }
}
