package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LawnTest {

    @Test
    public void shouldIndicateLocationIsWithinLawn() {
        assertThat(new Lawn(1, 1).isWithinLawn(new Location(0, 0))).isTrue();
    }

    @Test
    public void shouldIndicateLocationIsNotWithinLawnToAnyOf4Directions() {
        assertThat(new Lawn(1, 1).isWithinLawn(new Location(1, 0))).isFalse();
        assertThat(new Lawn(1, 1).isWithinLawn(new Location(0, 1))).isFalse();
        assertThat(new Lawn(1, 1).isWithinLawn(new Location(0, -1))).isFalse();
        assertThat(new Lawn(1, 1).isWithinLawn(new Location(-1, 0))).isFalse();
    }
}
