package org.ignast.challenge.mowingsimulation.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    private final List<ProgrammedMower> mowersWithPendingPrograms;

    public MowingSimulation(final List<ProgrammedMower> mowers) {
        this.mowersWithPendingPrograms = new ArrayList<>(mowers);
        Collections.sort(
            mowersWithPendingPrograms,
            Comparator.comparingInt(ProgrammedMower::pendingCommandsCount)
        );
    }

    public void execute() {
        while (!mowersWithPendingPrograms.isEmpty()) {
            executeNextTimeFrame();
        }
    }

    private void executeNextTimeFrame() {
        markMowersWithFinishedPrograms();
        val detectedCollisionLocations = detectCollisions();
        preventCollisions(detectedCollisionLocations);
        cleanUpCollisionDetector();
    }

    private void markMowersWithFinishedPrograms() {
        val iterator = mowersWithPendingPrograms.iterator();
        while (iterator.hasNext()) {
            val mower = iterator.next();
            if (mower.hasFinishedProgram()) {
                markForCollisionDetection(mower, Movement.createInPlace(mower.currentLocation()));
                iterator.remove();
            } else {
                break;
            }
        }
    }

    private void cleanUpCollisionDetector() {
        mowersWithPendingPrograms
            .parallelStream()
            .forEach(m -> collisionDetectionBoard.remove(m.currentLocation()));
    }

    private void preventCollisions(final Set<Location> detectedCollisionLocations) {
        val iterator = detectedCollisionLocations.iterator();
        while (iterator.hasNext()) {
            preventCollisionAt(iterator.next());
            iterator.remove();
        }
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

    private Set<Location> detectCollisions() {
        return mowersWithPendingPrograms
            .parallelStream()
            .reduce(
                ConcurrentHashMap.newKeySet(),
                (detectedCollisionLocations, mower) -> {
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
                    return detectedCollisionLocations;
                },
                (a, b) -> {
                    a.addAll(b);
                    return a;
                }
            );
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
