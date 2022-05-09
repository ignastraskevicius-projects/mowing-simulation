package org.ignast.challenge.mowingsimulation.domain;

import java.util.ArrayList;
import java.util.List;

class MowingSimulation {

    private final List<ProgrammedMower> mowers;

    public MowingSimulation(final List<ProgrammedMower> mowers) {
        this.mowers = new ArrayList<>(mowers);
    }

    public void execute() {
        mowers
            .stream()
            .forEach(mower -> {
                while (!mower.hasFinishedProgram()) {
                    mower.performNextMove();
                }
            });
    }
}
