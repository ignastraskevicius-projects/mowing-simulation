package org.ignast.challenge.mowingsimulation.domain;

import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_LEFT;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;

@ToString
public class ProgrammedMower {

    private final Lawn lawn;
    private Location currentLocation;
    private Optional<Location> previousLocation = Optional.empty();
    private Direction currentDirection;
    private final Deque<Command> pendingCommands;

    public ProgrammedMower(
        @NonNull final Lawn lawn,
        @NonNull final Location initialLocation,
        @NonNull final Direction initialDirection,
        @NonNull final Command[] commands
    ) {
        this.lawn = lawn;
        this.currentLocation = initialLocation;
        this.currentDirection = initialDirection;
        this.pendingCommands =
            Arrays
                .asList(commands)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedList::new));
        if (pendingCommands.size() != commands.length) {
            throw new NullPointerException("Individual command supplied to mover is null");
        }
    }

    public Movement performNextMove() {
        if (pendingCommands.isEmpty()) {
            throw new IllegalStateException("Mover has already finished its program");
        }
        val currentCommand = pendingCommands.poll();
        if (currentCommand.equals(GO_FORWARD)) {
            return goForward();
        } else {
            return turn(currentCommand);
        }
    }

    private Movement turn(Command currentCommand) {
        if (currentCommand.equals(TURN_LEFT)) {
            currentDirection = currentDirection.getDiretionAfterTurning90DegreesLeft();
        } else {
            currentDirection = currentDirection.getDiretionAfterTurning90DegreesRight();
        }
        previousLocation = Optional.empty();
        return new Movement(currentLocation, currentLocation);
    }

    private Movement goForward() {
        val newLocation = currentDirection.getNextLocationFrom(currentLocation);
        if (lawn.isWithinLawn(newLocation)) {
            Movement movement = new Movement(currentLocation, newLocation);
            previousLocation = Optional.of(currentLocation);
            currentLocation = newLocation;
            return movement;
        } else {
            return new Movement(currentLocation, currentLocation);
        }
    }

    public Location currentLocation() {
        return currentLocation;
    }

    public Location revertLastMove() {
        currentLocation =
            previousLocation.orElseThrow(() ->
                new IllegalStateException("Only last moving forward action can be reverted")
            );
        previousLocation = Optional.empty();
        return currentLocation;
    }

    public boolean hasFinishedProgram() {
        return pendingCommands.isEmpty();
    }

    public Direction currentDirection() {
        return currentDirection;
    }
}
