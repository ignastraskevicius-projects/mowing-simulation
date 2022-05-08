package org.ignast.challenge.mowingsimulation.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MowingSimulator {

    public void executeSimulation(List<ProgrammedMower> mowers) {
        mowers
            .stream()
            .forEach(m -> {
                m.performNextMove();
                m.performNextMove();
            });
    }
}
