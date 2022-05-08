package org.ignast.challenge.mowingsimulation.domain;

public record Location(int x, int y) {
    public void isOutsideLawn() {}
}
