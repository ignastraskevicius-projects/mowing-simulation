package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
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
    public void shouldSimulateNotCollidingMowersFollowingEachOther1Move() {
        val forward = new Command[] { GO_FORWARD };
        val m1 = new ProgrammedMower(new Lawn(2, 2), new Location(0, 0), NORTH, forward);
        val m2 = new ProgrammedMower(new Lawn(2, 2), new Location(0, 1), EAST, forward);
        val m3 = new ProgrammedMower(new Lawn(2, 2), new Location(1, 1), SOUTH, forward);
        val m4 = new ProgrammedMower(new Lawn(2, 2), new Location(1, 0), WEST, forward);
        List<ProgrammedMower> mowers = List.of(m1, m2, m3, m4);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 1), NORTH);
        assertPosition(m2, new Location(1, 1), EAST);
        assertPosition(m3, new Location(1, 0), SOUTH);
        assertPosition(m4, new Location(0, 0), WEST);
    }

    @Test
    public void shouldDoNothingFor0MowerSimulation() {
        new MowingSimulation(List.of()).execute();
    }

    @Test
    public void shouldPreventColliding2MowersMovingToSameLocationAtOnce() {
        val forward = new Command[] { GO_FORWARD };
        val m1 = new ProgrammedMower(new Lawn(1, 3), new Location(0, 0), NORTH, forward);
        val m2 = new ProgrammedMower(new Lawn(1, 3), new Location(0, 2), SOUTH, forward);
        List<ProgrammedMower> mowers = List.of(m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 0), NORTH);
        assertPosition(m2, new Location(0, 2), SOUTH);
    }

    @Test
    public void shouldPreventMultipleUnrelatedCollisions() {
        val forward = new Command[] { GO_FORWARD };
        val m1 = new ProgrammedMower(new Lawn(2, 3), new Location(0, 0), NORTH, forward);
        val m2 = new ProgrammedMower(new Lawn(2, 3), new Location(0, 2), SOUTH, forward);
        val m3 = new ProgrammedMower(new Lawn(2, 3), new Location(1, 0), NORTH, forward);
        val m4 = new ProgrammedMower(new Lawn(2, 3), new Location(1, 2), SOUTH, forward);

        List<ProgrammedMower> mowers = List.of(m1, m2, m3, m4);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 0), NORTH);
        assertPosition(m2, new Location(0, 2), SOUTH);
        assertPosition(m3, new Location(1, 0), NORTH);
        assertPosition(m4, new Location(1, 2), SOUTH);
    }

    @Test
    public void shouldPreventColliding2MowersFirstMovingIntoSecond() {
        val forward = new Command[] { GO_FORWARD };
        val turn = new Command[] { TURN_RIGHT };
        val m1 = new ProgrammedMower(new Lawn(1, 2), new Location(0, 0), NORTH, forward);
        val m2 = new ProgrammedMower(new Lawn(1, 2), new Location(0, 1), SOUTH, turn);
        List<ProgrammedMower> mowers = List.of(m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 0), NORTH);
        assertPosition(m2, new Location(0, 1), WEST);
    }

    @Test
    public void shouldPreventColliding2MowersSecondMovingIntoFirst() {
        val forward = new Command[] { GO_FORWARD };
        val turn = new Command[] { TURN_RIGHT };
        val m1 = new ProgrammedMower(new Lawn(1, 2), new Location(0, 1), SOUTH, turn);
        val m2 = new ProgrammedMower(new Lawn(1, 2), new Location(0, 0), NORTH, forward);
        List<ProgrammedMower> mowers = List.of(m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 1), WEST);
        assertPosition(m2, new Location(0, 0), NORTH);
    }

    @Test
    public void preventCollisionShouldPreventIndirectlyAffectedMowersAsWell() {
        val forward = new Command[] { GO_FORWARD };
        val m1 = new ProgrammedMower(new Lawn(1, 5), new Location(0, 4), SOUTH, forward);
        val m2 = new ProgrammedMower(new Lawn(1, 5), new Location(0, 3), SOUTH, forward);
        val m3 = new ProgrammedMower(new Lawn(1, 5), new Location(0, 1), NORTH, forward);
        val m4 = new ProgrammedMower(new Lawn(1, 5), new Location(0, 0), NORTH, forward);
        List<ProgrammedMower> mowers = List.of(m1, m2, m3, m4);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 4), SOUTH);
        assertPosition(m2, new Location(0, 3), SOUTH);
        assertPosition(m3, new Location(0, 1), NORTH);
        assertPosition(m4, new Location(0, 0), NORTH);
    }

    @Test
    public void preventFirstCollisionWhichIsIndirectlyInvolvedInSecondCollision() {
        val forward = new Command[] { GO_FORWARD };
        val lawn = new Lawn(2, 3);
        val m1 = new ProgrammedMower(lawn, new Location(0, 1), EAST, forward);
        val m2 = new ProgrammedMower(lawn, new Location(1, 0), NORTH, forward);
        val m3 = new ProgrammedMower(lawn, new Location(1, 1), NORTH, forward);
        val m4 = new ProgrammedMower(lawn, new Location(0, 2), EAST, forward);
        List<ProgrammedMower> mowers = List.of(m1, m2, m3, m4);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 1), EAST);
        assertPosition(m2, new Location(1, 0), NORTH);
        assertPosition(m3, new Location(1, 1), NORTH);
        assertPosition(m4, new Location(0, 2), EAST);
    }

    @Test
    public void preventSecondCollisionWhichIsIndirectlyInvolvedInFirstCollision() {
        val forward = new Command[] { GO_FORWARD };
        val lawn = new Lawn(2, 3);
        val m1 = new ProgrammedMower(lawn, new Location(0, 1), EAST, forward);
        val m2 = new ProgrammedMower(lawn, new Location(1, 0), NORTH, forward);
        val m3 = new ProgrammedMower(lawn, new Location(1, 1), NORTH, forward);
        val m4 = new ProgrammedMower(lawn, new Location(0, 2), EAST, forward);
        List<ProgrammedMower> mowers = List.of(m3, m4, m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 1), EAST);
        assertPosition(m2, new Location(1, 0), NORTH);
        assertPosition(m3, new Location(1, 1), NORTH);
        assertPosition(m4, new Location(0, 2), EAST);
    }

    @Test
    public void pastMovementsShouldNotBeTakenIntoAccountWhenCalculatingCurrentPotentialCollisions() {
        val halfCircle = new Command[] { GO_FORWARD, TURN_RIGHT, GO_FORWARD, TURN_RIGHT };
        val lawn = new Lawn(2, 2);
        val m1 = new ProgrammedMower(lawn, new Location(0, 0), NORTH, halfCircle);
        val m2 = new ProgrammedMower(lawn, new Location(1, 0), WEST, halfCircle);
        List<ProgrammedMower> mowers = List.of(m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(1, 1), SOUTH);
        assertPosition(m2, new Location(0, 1), EAST);
    }

    @Test
    public void pastCollisionLocationsShouldNotBeTakenIntoAccountWhenCalculatingCurrentPotentialCollisions() {
        val lawn = new Lawn(1, 3);
        val m1 = new ProgrammedMower(
            lawn,
            new Location(0, 0),
            NORTH,
            new Command[] { GO_FORWARD, GO_FORWARD }
        );
        val m2 = new ProgrammedMower(
            lawn,
            new Location(0, 2),
            SOUTH,
            new Command[] { GO_FORWARD, TURN_RIGHT }
        );
        List<ProgrammedMower> mowers = List.of(m1, m2);

        new MowingSimulation(mowers).execute();

        assertPosition(m1, new Location(0, 1), NORTH);
        assertPosition(m2, new Location(0, 2), WEST);
    }

    private void assertPosition(ProgrammedMower mower, Location location, Direction direction) {
        assertThat(mower.currentLocation()).isEqualTo(location);
        assertThat(mower.currentDirection()).isEqualTo(direction);
    }
}
