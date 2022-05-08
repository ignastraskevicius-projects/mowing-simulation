package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ignast.challenge.mowingsimulation.domain.Direction.EAST;
import static org.ignast.challenge.mowingsimulation.domain.Direction.NORTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.SOUTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.WEST;

import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    public void shouldProvideNextLocationToTheGivenDirection() {
        assertThat(NORTH.getNextLocationFrom(new Location(0, 0))).isEqualTo(new Location(0, 1));
        assertThat(EAST.getNextLocationFrom(new Location(0, 0))).isEqualTo(new Location(1, 0));
        assertThat(SOUTH.getNextLocationFrom(new Location(0, 0))).isEqualTo(new Location(0, -1));
        assertThat(WEST.getNextLocationFrom(new Location(0, 0))).isEqualTo(new Location(-1, 0));
    }
}
