package org.ignast.challenge.mowingsimulation.domain;

public record Movement(Location locationFrom, Location locationTo) {
    public static Movement createInPlace(final Location location) {
        return new Movement(location, location);
    }

    public boolean hasLocationChanged() {
        return !locationFrom.equals(locationTo);
    }
}
