package org.ignast.challenge.mowingsimulation.controller;

import static org.ignast.challenge.mowingsimulation.domain.Command.GO_FORWARD;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_LEFT;
import static org.ignast.challenge.mowingsimulation.domain.Command.TURN_RIGHT;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.ignast.challenge.mowingsimulation.domain.Command;
import org.ignast.challenge.mowingsimulation.domain.Direction;
import org.ignast.challenge.mowingsimulation.domain.Lawn;
import org.ignast.challenge.mowingsimulation.domain.Location;
import org.ignast.challenge.mowingsimulation.domain.MowingSimulator;
import org.ignast.challenge.mowingsimulation.domain.ProgrammedMower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MowingSimulationController {

    @Autowired
    private final LocalFileBasedCommunication localFileBasedCommunication;

    @Autowired
    private final MowingSimulator simulator;

    public Path simulate(final Path inputFilePath) {
        return localFileBasedCommunication.handleRequest(
            inputFilePath,
            request -> {
                final List<ProgrammedMower> mowers = parseMowers(request);
                simulator.executeSimulation(mowers);
                return mowers.stream().map(this::serialize).collect(Collectors.toUnmodifiableList());
            }
        );
    }

    private List<ProgrammedMower> parseMowers(List<String> request) {
        val lawn = parseLawn(request.get(0));
        val mowersCount = (request.size() - 1) / 2;
        return IntStream
            .range(0, mowersCount)
            .mapToObj(i -> {
                val direction = parseDirection(request.get(i * 2 + 1));
                val location = parseLocation(request.get(i * 2 + 1));
                val commands = parseCommands(request.get(i * 2 + 2));
                return new ProgrammedMower(lawn, location, direction, commands);
            })
            .collect(Collectors.toUnmodifiableList());
    }

    private Lawn parseLawn(String serializedLawn) {
        val lawnFurthestCorner = Arrays
            .stream(serializedLawn.split(" "))
            .map(Integer::parseInt)
            .collect(Collectors.toUnmodifiableList());

        return new Lawn(lawnFurthestCorner.get(0) + 1, lawnFurthestCorner.get(1) + 1);
    }

    private Direction parseDirection(String positionSerialized) {
        val directionSerialised = positionSerialized.split(" ")[2];
        return Arrays
            .stream(Direction.values())
            .filter(d -> d.toString().startsWith(directionSerialised))
            .findFirst()
            .orElseThrow();
    }

    private Location parseLocation(final String positionSerialized) {
        val positionTokenised = Arrays
            .stream(positionSerialized.split(" "))
            .limit(2)
            .map(Integer::parseInt)
            .collect(Collectors.toUnmodifiableList());
        return new Location(positionTokenised.get(0), positionTokenised.get(1));
    }

    private String serialize(final ProgrammedMower mower) {
        val directionSerialized = mower.currentDirection().toString().substring(0, 1);
        return String.format(
            "%d %d %s",
            mower.currentLocation().x(),
            mower.currentLocation().y(),
            directionSerialized
        );
    }

    private Command[] parseCommands(final String serializedCommands) {
        return serializedCommands
            .chars()
            .mapToObj(c -> (Character) (char) c)
            .map(s -> {
                if (s == 'F') {
                    return GO_FORWARD;
                } else if (s == 'L') {
                    return TURN_LEFT;
                } else if (s == 'R') {
                    return TURN_RIGHT;
                } else {
                    throw new IllegalArgumentException("Invalid command; " + s);
                }
            })
            .toArray(Command[]::new);
    }
}
