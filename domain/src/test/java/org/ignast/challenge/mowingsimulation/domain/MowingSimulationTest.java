package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_LEFT;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_RIGHT;
import static org.ignast.challenge.mowingsimulation.domain.Direction.EAST;
import static org.ignast.challenge.mowingsimulation.domain.Direction.NORTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.SOUTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.WEST;

import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class MowingSimulationTest {

    @Test
    public void shouldSimulateNotCollidingMowers() {
        val clockwiseHalfCircle = new Command[] { GO_FORWARD, TURN_RIGHT, GO_FORWARD, TURN_RIGHT };
        val m1 = new ProgrammedMower(new Lawn(2, 2), new Location(0, 0), NORTH, clockwiseHalfCircle);
        val m2 = new ProgrammedMower(new Lawn(2, 2), new Location(0, 1), EAST, clockwiseHalfCircle);
        val m3 = new ProgrammedMower(new Lawn(2, 2), new Location(1, 1), SOUTH, clockwiseHalfCircle);
        val m4 = new ProgrammedMower(new Lawn(2, 2), new Location(1, 0), WEST, clockwiseHalfCircle);
        List<ProgrammedMower> mowers = List.of(m1, m2, m3, m4);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(1, 1), SOUTH);
        assertPosition(m2, new Location(1, 0), WEST);
        assertPosition(m3, new Location(0, 0), NORTH);
        assertPosition(m4, new Location(0, 1), EAST);
    }

    private void assertPosition(ProgrammedMower mower, Location location, Direction direction) {
        assertThat(mower.currentLocation()).isEqualTo(location);
        assertThat(mower.currentDirection()).isEqualTo(direction);
    }
}
