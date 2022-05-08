package org.ignast.challenge.mowingsimulation.domain;

public record Lawn(int length, int width) {
    boolean isWithinLawn(final Location location) {
        return location.x() >= 0 && location.x() < length && location.y() >= 0 && location.y() < width;
    }
}
