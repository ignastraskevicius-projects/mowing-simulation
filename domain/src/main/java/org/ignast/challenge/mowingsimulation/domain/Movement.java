package org.ignast.challenge.mowingsimulation.domain;

public record Movement(Location locationFrom, Location locationTo) {
    public boolean hasLocationChanged() {
        return !locationFrom.equals(locationTo);
    }
}
