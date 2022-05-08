package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_LEFT;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_RIGHT;
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

    @Test
    public void shouldProvideNextDirectionAfterRuningLeft() {
        assertThat(NORTH.getDiretionAfterTurning90DegreesLeft()).isEqualTo(WEST);
        assertThat(EAST.getDiretionAfterTurning90DegreesLeft()).isEqualTo(NORTH);
        assertThat(SOUTH.getDiretionAfterTurning90DegreesLeft()).isEqualTo(EAST);
        assertThat(WEST.getDiretionAfterTurning90DegreesLeft()).isEqualTo(SOUTH);
    }

    @Test
    public void shouldProvideNextDirectionAfterRuningRight() {
        assertThat(NORTH.getDiretionAfterTurning90DegreesRight()).isEqualTo(EAST);
        assertThat(EAST.getDiretionAfterTurning90DegreesRight()).isEqualTo(SOUTH);
        assertThat(SOUTH.getDiretionAfterTurning90DegreesRight()).isEqualTo(WEST);
        assertThat(WEST.getDiretionAfterTurning90DegreesRight()).isEqualTo(NORTH);
    }
}
