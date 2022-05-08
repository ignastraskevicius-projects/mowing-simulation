package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
import static org.ignast.challenge.mowingsimulation.domain.Direction.EAST;
import static org.ignast.challenge.mowingsimulation.domain.Direction.NORTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.SOUTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.WEST;

import lombok.val;
import org.junit.jupiter.api.Test;

class ProgrammedMowerTest {

    @Test
    public void shouldNotAcceptNullArguments() {
        assertThatNullPointerException()
            .isThrownBy(() -> new ProgrammedMower(null, new Location(0, 0), NORTH, new Command[0]));
        assertThatNullPointerException()
            .isThrownBy(() -> new ProgrammedMower(new Lawn(1, 1), null, NORTH, new Command[0]));
        assertThatNullPointerException()
            .isThrownBy(() -> new ProgrammedMower(new Lawn(1, 1), new Location(0, 0), null, new Command[0]));
        assertThatNullPointerException()
            .isThrownBy(() -> new ProgrammedMower(new Lawn(1, 1), new Location(0, 0), NORTH, null));
        assertThatNullPointerException()
            .isThrownBy(() ->
                new ProgrammedMower(new Lawn(1, 1), new Location(0, 0), NORTH, new Command[] { null })
            );
        new ProgrammedMower(new Lawn(1, 1), new Location(0, 0), NORTH, new Command[0]);
        new ProgrammedMower(new Lawn(1, 1), new Location(0, 0), NORTH, new Command[] { GO_FORWARD });
    }

    @Test
    public void shouldNotMoveOutsideTheBoardToTheSouth() {
        Location initialLocation = new Location(1, 0);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, SOUTH, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(initialLocation);
        assertThat(mower.currentLocation()).isEqualTo(initialLocation);
    }

    @Test
    public void shouldNotMoveOutsideTheBoardToTheWest() {
        Location initialLocation = new Location(0, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, WEST, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(initialLocation);
        assertThat(mower.currentLocation()).isEqualTo(initialLocation);
    }

    @Test
    public void shouldNotMoveOutsideTheBoardToTheNorth() {
        Location initialLocation = new Location(1, 2);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, NORTH, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(initialLocation);
        assertThat(mower.currentLocation()).isEqualTo(initialLocation);
    }

    @Test
    public void shouldNotMoveOutsideTheBoardToTheEast() {
        Location initialLocation = new Location(2, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, EAST, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(initialLocation);
        assertThat(mower.currentLocation()).isEqualTo(initialLocation);
    }

    @Test
    public void shouldBeAbleToMoveToTheSouth() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, SOUTH, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(new Location(1, 0));
        assertThat(mower.currentLocation()).isEqualTo(new Location(1, 0));
    }

    @Test
    public void shouldBeAbleToMoveToTheWest() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, WEST, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(new Location(0, 1));
        assertThat(mower.currentLocation()).isEqualTo(new Location(0, 1));
    }

    @Test
    public void shouldBeAbleToMoveToTheNorth() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, NORTH, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(new Location(1, 2));
        assertThat(mower.currentLocation()).isEqualTo(new Location(1, 2));
    }

    @Test
    public void shouldBeAbleToMoveToTheEast() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(new Lawn(3, 3), initialLocation, EAST, new Command[] { GO_FORWARD });

        val movement = mower.performNextMove();

        assertThat(movement.locationFrom()).isEqualTo(initialLocation);
        assertThat(movement.locationTo()).isEqualTo(new Location(2, 1));
        assertThat(mower.currentLocation()).isEqualTo(new Location(2, 1));
    }
}
