package it.angrybear.yagl.parsers;

import it.angrybear.yagl.contents.PermissionRequirement;
import it.fulminazzo.fulmicollection.interfaces.functions.BiFunctionException;
import it.fulminazzo.fulmicollection.interfaces.functions.TriConsumer;
import it.fulminazzo.yamlparser.configuration.IConfiguration;
import it.fulminazzo.yamlparser.parsers.YAMLParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PermissionRequirementParser extends YAMLParser<PermissionRequirement> {

    public PermissionRequirementParser() {
        super(PermissionRequirement.class);
    }

    @Override
    protected BiFunctionException<@NotNull IConfiguration, @NotNull String, @Nullable PermissionRequirement> getLoader() {
        return (c, s) -> {
            String p = c.getString(s);
            if (p == null) return null;
            return new PermissionRequirement(p);
        };
    }

    @Override
    protected TriConsumer<@NotNull IConfiguration, @NotNull String, @Nullable PermissionRequirement> getDumper() {
        return (c, s, p) -> c.set(s, p == null ? null : p.getPermission());
    }
}
