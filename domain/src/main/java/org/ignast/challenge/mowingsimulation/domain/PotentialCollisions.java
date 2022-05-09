package org.ignast.challenge.mowingsimulation.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public record PotentialCollisions(
    AtomicBoolean isOccupiedAlready,
    ConcurrentLinkedQueue<ProgrammedMower> mowersAttemptingToEnter
) {}
