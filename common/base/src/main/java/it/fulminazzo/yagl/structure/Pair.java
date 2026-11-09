package it.fulminazzo.yagl.structure;

import lombok.*;

/**
 * A data class that stores two objects together.
 *
 * @param <F> the type of the first object
 * @param <S> the type of the second object
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public final class Pair<F, S> {
    private F first;
    private S second;

}
