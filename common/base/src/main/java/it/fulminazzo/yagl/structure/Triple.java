package it.fulminazzo.yagl.structure;

import lombok.*;

/**
 * A data class that stores three objects together.
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 * @param <T> the type of the third object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Triple<F, S, T> {
    private F first;
    private S second;
    private T third;

}
