package org.ignast.challenge.mowingsimulation.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileBasedCommunicationStub extends LocalFileBasedCommunication {

    private final List<String> readLines;

    private final List<String> processedLines;

    @Override
    public Path handleRequest(
        final Path inputFilePath,
        final Function<List<String>, List<String>> requestHandler
    ) {
        assertThat(requestHandler.apply(readLines)).isEqualTo(processedLines);
        return inputFilePath;
    }
}
