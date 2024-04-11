package it.angrybear.yagl.parsers;

import it.angrybear.yagl.guis.GUI;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.fulmicollection.objects.Refl;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GUIParser extends TypedParser<GUI> {

    public GUIParser() {
        super(GUI.class);
    }

    @Override
    protected @NotNull BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable GUI> getLoader() {
        return (c, s) -> {
            GUI g = super.getLoader().apply(c, s);
            Integer size = c.getInteger(s + ".size");
            if (size == null) throw new IllegalArgumentException("'size' cannot be null");
            Refl<GUI> gui = new Refl<>(g);
            gui.setFieldObject("contents", gui.invokeMethod("createContents",
                    new Class[]{int.class, List.class},
                    size, gui.getFieldObject("contents")));
            return g;
        };
    }

    @Override
    protected @NotNull TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable GUI> getDumper() {
        return (c, s, g) -> {
            super.getDumper().accept(c, s, g);
            if (g == null) return;
            c.set(s + ".size", g.getSize());
        };
    }

}
