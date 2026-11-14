package it.fulminazzo.yagl.naming;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

/**
 * Defines a naming convention.
 * <br>
 * All the classes in this package have been imported from the blocksmith project
 * <a href="https://github.com/fulminazzo/blocksmith">Click here</a> for more.
 *
 * @see CaseConverter
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Convention {
    /**
     * The camelCase (Java naming) convention.
     */
    CAMEL_CASE(new CamelCaseConvention()),
    /**
     * The kebab-case convention.
     */
    KEBAB_CASE(new SeparatedCaseConvention("-")),
    /**
     * The snake_case convention.
     */
    SNAKE_CASE(new SeparatedCaseConvention("_")),
    /**
     * The PascalCase convention.
     */
    PASCAL_CASE(new PascalCaseConvention());

    @Getter(AccessLevel.PACKAGE)
    @NotNull NamingConvention namingConvention;

}
