package org.ignast.challenge.mowingsimulation.controller;

import java.nio.file.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MowingSimulationController {

    @Autowired
    private final LocalFileBasedCommunication localFileBasedCommunication;

    public Path simulate(final Path inputFilePath) {
        return localFileBasedCommunication.handleRequest(inputFilePath, request -> List.of("0 1 E"));
    }
}
