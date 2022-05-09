package org.ignast.challenge.mowingsimulation.domain;

import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

class MowingSimulation {

    private final Map<Location, List<ProgrammedMower>> collisionDetectionBoard = new ConcurrentHashMap<>();

    private final List<ProgrammedMower> mowers;

    public MowingSimulation(final List<ProgrammedMower> mowers) {
        this.mowers = new ArrayList<>(mowers);
    }

    public void execute() {
        if (mowers.isEmpty()) {
            return;
        } 
        while (!mowers.get(0).hasFinishedProgram()) {
            AtomicReference<Location> collision = new AtomicReference<>();
            mowers
                    .parallelStream()
                    .forEach(mower -> {
                        val movement = mower.performNextMove();
                        collisionDetectionBoard.compute(movement.locationTo(), (location, collidingMowers) -> {
                            if (collidingMowers == null) {
                                return new ArrayList<>(List.of(mower));
                            } else {
                                collidingMowers.add(mower);
                                return collidingMowers;
                            }
                        });
                        if (collisionDetectionBoard.get(movement.locationTo()).size() > 1) {
                            collision.set(movement.locationTo());
                        }
                    });
            if (collision.get() != null) {
                collisionDetectionBoard.get(collision.get()).forEach(m -> m.revertLastMove());
            }
        }

    }
}
