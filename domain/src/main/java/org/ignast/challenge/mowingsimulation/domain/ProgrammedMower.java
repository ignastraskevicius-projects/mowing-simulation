package org.ignast.challenge.mowingsimulation.domain;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;

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
}