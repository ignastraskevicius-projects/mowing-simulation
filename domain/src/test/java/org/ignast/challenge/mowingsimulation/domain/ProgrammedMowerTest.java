package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_LEFT;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_RIGHT;
import static org.ignast.challenge.mowingsimulation.domain.Direction.EAST;
import static org.ignast.challenge.mowingsimulation.domain.Direction.NORTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.SOUTH;
import static org.ignast.challenge.mowingsimulation.domain.Direction.WEST;

import java.util.Arrays;
import java.util.stream.Stream;
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
    public void shouldIndicateNumberOfCommandsLeftToExecute() {
        val anyLocation = new Location(0, 0);
        val anyLawn = new Lawn(3, 3);
        assertThat(
            new ProgrammedMower(anyLawn, anyLocation, SOUTH, new Command[] { GO_FORWARD })
                .pendingCommandsCount()
        )
            .isEqualTo(1);
        assertThat(new ProgrammedMower(anyLawn, anyLocation, SOUTH, new Command[0]).pendingCommandsCount())
            .isEqualTo(0);
        assertThat(
            new ProgrammedMower(anyLawn, anyLocation, SOUTH, new Command[] { TURN_LEFT, TURN_RIGHT })
                .pendingCommandsCount()
        )
            .isEqualTo(2);
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

    @Test
    public void shouldBeAbleToRevertAfterTurningLeft() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(
            new Lawn(3, 3),
            initialLocation,
            EAST,
            new Command[] { GO_FORWARD, TURN_LEFT }
        );

        mower.performNextMove();
        mower.performNextMove();
        assertThatIllegalStateException().isThrownBy(() -> mower.revertLastMove());
    }

    @Test
    public void shouldBeAbleToRevertAfterTurningRight() {
        Location initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(
            new Lawn(3, 3),
            initialLocation,
            EAST,
            new Command[] { GO_FORWARD, TURN_LEFT }
        );

        mower.performNextMove();
        mower.performNextMove();
        assertThatIllegalStateException().isThrownBy(() -> mower.revertLastMove());
    }

    @Test
    public void shouldIndicateThatMowersProgramHasFinished() {
        val mower = new ProgrammedMower(new Lawn(0, 0), new Location(0, 0), EAST, new Command[0]);

        assertThat(mower.hasFinishedProgram()).isTrue();
    }

    @Test
    public void mowerWithFinishedProgramShouldFailToPerformNextMove() {
        val mower = new ProgrammedMower(new Lawn(0, 0), new Location(0, 0), EAST, new Command[0]);

        assertThatIllegalStateException().isThrownBy(() -> mower.performNextMove());
    }

    @Test
    public void shouldIndicateThatMoversProgramHasNotYetFinished() {
        val mower = new ProgrammedMower(
            new Lawn(0, 0),
            new Location(0, 0),
            EAST,
            new Command[] { GO_FORWARD }
        );

        assertThat(mower.hasFinishedProgram()).isFalse();
    }

    @Test
    public void shouldBeAbleToTurnLeft() {
        val initialLocation = new Location(2, 1);
        val mower = new ProgrammedMower(
            new Lawn(4, 4),
            initialLocation,
            EAST,
            new Command[] {
                TURN_LEFT,
                GO_FORWARD,
                TURN_LEFT,
                GO_FORWARD,
                TURN_LEFT,
                GO_FORWARD,
                TURN_LEFT,
                GO_FORWARD,
            }
        );

        Stream
            .of(new Location(2, 2), new Location(1, 2), new Location(1, 1), new Location(2, 1))
            .forEach(expectedLocation -> {
                mower.performNextMove();
                val movement = mower.performNextMove();

                assertThat(movement.locationTo()).isEqualTo(expectedLocation);
            });
    }

    @Test
    public void shouldIndicateDirection() {
        val initialLocation = new Location(0, 0);
        val mower = new ProgrammedMower(
            new Lawn(0, 0),
            initialLocation,
            EAST,
            new Command[] { TURN_LEFT, TURN_LEFT, TURN_LEFT, TURN_LEFT }
        );

        Stream
            .of(NORTH, WEST, SOUTH, EAST)
            .forEach(expectedDirection -> {
                mower.performNextMove();

                assertThat(mower.currentDirection()).isEqualTo(expectedDirection);
            });
    }

    @Test
    public void shouldBeAbleToTurnRight() {
        val initialLocation = new Location(1, 1);
        val mower = new ProgrammedMower(
            new Lawn(4, 4),
            initialLocation,
            WEST,
            new Command[] {
                TURN_RIGHT,
                GO_FORWARD,
                TURN_RIGHT,
                GO_FORWARD,
                TURN_RIGHT,
                GO_FORWARD,
                TURN_RIGHT,
                GO_FORWARD,
            }
        );

        Stream
            .of(new Location(1, 2), new Location(2, 2), new Location(2, 1), new Location(1, 1))
            .forEach(expectedLocation -> {
                mower.performNextMove();
                val movement = mower.performNextMove();

                assertThat(movement.locationTo()).isEqualTo(expectedLocation);
            });
    }

    @Test
    public void shouldNotBeAbleToRevertLastMoveForward() {
        Arrays
            .stream(Direction.values())
            .forEach(direction -> {
                Location initialLocation = new Location(1, 1);
                val mower = new ProgrammedMower(
                    new Lawn(3, 3),
                    initialLocation,
                    direction,
                    new Command[] { GO_FORWARD }
                );

                mower.performNextMove();
                val location = mower.revertLastMove();

                assertThat(location).isEqualTo(initialLocation);
                assertThat(mower.currentLocation()).isEqualTo(initialLocation);
            });
    }

    @Test
    public void shouldNotNotBeAbleToRevert2MovesForward() {
        Arrays
            .stream(Direction.values())
            .forEach(direction -> {
                Location initialLocation = new Location(1, 1);
                val mower = new ProgrammedMower(
                    new Lawn(3, 3),
                    initialLocation,
                    direction,
                    new Command[] { GO_FORWARD, GO_FORWARD }
                );

                mower.performNextMove();
                mower.revertLastMove();

                assertThatIllegalStateException().isThrownBy(() -> mower.revertLastMove());
            });
    }

    @Test
    public void shouldNotRevertBeforeExecutingAnyCommand() {
        Arrays
            .stream(Direction.values())
            .forEach(direction -> {
                Location initialLocation = new Location(1, 1);
                val mower = new ProgrammedMower(
                    new Lawn(3, 3),
                    initialLocation,
                    direction,
                    new Command[] { GO_FORWARD }
                );

                assertThatIllegalStateException().isThrownBy(() -> mower.revertLastMove());
            });
    }
}
