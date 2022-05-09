package org.ignast.challenge.mowingsimulation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.val;

class MowingSimulation {

    private static final PotentialCollisions NO_COLLISION = new PotentialCollisions(
        new AtomicBoolean(false),
        new ConcurrentLinkedQueue<>()
    );

    private final Map<Location, PotentialCollisions> collisionDetectionBoard = new ConcurrentHashMap<>();

    private final Set<Location> detectedCollisionLocations = ConcurrentHashMap.newKeySet();

    private final List<ProgrammedMower> mowers;

    public MowingSimulation(final List<ProgrammedMower> mowers) {
        this.mowers = new ArrayList<>(mowers);
    }

    public void execute() {
        if (mowers.isEmpty()) {
            return;
        }
        while (!mowers.get(0).hasFinishedProgram()) {
            detectCollisions();
            preventCollisions();
            cleanUpCollisionDetector();
        }
    }

    private void cleanUpCollisionDetector() {
        mowers.parallelStream().forEach(m -> collisionDetectionBoard.remove(m.currentLocation()));
    }

    private void preventCollisions() {
        detectedCollisionLocations.forEach(location -> preventCollisionAt(location));
    }

    private void preventCollisionAt(Location location) {
        val iterator = collisionDetectionBoard
            .getOrDefault(location, NO_COLLISION)
            .mowersAttemptingToEnter()
            .iterator();
        while (iterator.hasNext()) {
            val m = iterator.next();
            m.revertLastMove();
            preventCollisionAt(m.currentLocation());
            iterator.remove();
        }
    }

    private void detectCollisions() {
        mowers
            .parallelStream()
            .forEach(mower -> {
                val movement = mower.performNextMove();
                markForCollisionDetection(mower, movement);
                val potentialCollision = collisionDetectionBoard.get(movement.locationTo());
                if (
                    potentialCollision.mowersAttemptingToEnter().size() > 1 ||
                    potentialCollision.isOccupiedAlready().get() &&
                    potentialCollision.mowersAttemptingToEnter().size() > 0
                ) {
                    detectedCollisionLocations.add(movement.locationTo());
                }
            });
    }

    private void markForCollisionDetection(ProgrammedMower mower, Movement movement) {
        collisionDetectionBoard.compute(
            movement.locationTo(),
            (Location location, PotentialCollisions collidingMowers) -> {
                if (movement.hasLocationChanged()) {
                    return addAsAttemptingToEnter(mower, collidingMowers);
                } else {
                    return addAsOccupyingAlready(collidingMowers);
                }
            }
        );
    }

    private PotentialCollisions addAsOccupyingAlready(PotentialCollisions collidingMowers) {
        if (collidingMowers == null) {
            return new PotentialCollisions(new AtomicBoolean(true), new ConcurrentLinkedQueue<>());
        } else {
            collidingMowers.isOccupiedAlready().set(true);
            return collidingMowers;
        }
    }

    private PotentialCollisions addAsAttemptingToEnter(
        ProgrammedMower mower,
        PotentialCollisions collidingMowers
    ) {
        if (collidingMowers == null) {
            return new PotentialCollisions(
                new AtomicBoolean(false),
                new ConcurrentLinkedQueue<>(List.of(mower))
            );
        } else {
            collidingMowers.mowersAttemptingToEnter().add(mower);
            return collidingMowers;
        }
    }
}
