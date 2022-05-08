package org.ignast.challenge.mowingsimulation.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class LocalFileBasedCommunication {

    private static final String INPUT_FILE_EXTENSION = ".input";

    public Path handleRequest(
        final Path inputFilePath,
        final Function<List<String>, List<String>> requestHandler
    ) {
        validateInputFilePath(inputFilePath);
        List<String> outputFileLines = requestHandler.apply(readLinesFrom(inputFilePath));
        final Path outputFilePath = getOutputFilePath(inputFilePath);
        writeLinesTo(outputFilePath, outputFileLines);
        return outputFilePath;
    }

    private Path getOutputFilePath(Path inputFilePath) {
        val outputFilePath = Path.of(
            inputFilePath
                .toString()
                .substring(0, inputFilePath.toString().length() - INPUT_FILE_EXTENSION.length()) +
            ".output"
        );
        return outputFilePath;
    }

    private void validateInputFilePath(Path inputFilePath) {
        if (!inputFilePath.toString().endsWith(INPUT_FILE_EXTENSION)) {
            throw new IllegalArgumentException("Input file name expected to end with '.input' extension");
        }
    }

    private void writeLinesTo(Path outputFilePath, List<String> outputFileLines) {
        try {
            Files.createFile(outputFilePath);
            val fileWriter = outputFileLines
                .stream()
                .reduce(new FileWriter(outputFilePath.toFile()), this::writeLine, combinationNotSupported());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readLinesFrom(Path inputFilePath) {
        try {
            return Files.readAllLines(inputFilePath);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private FileWriter writeLine(final FileWriter writer, final String line) {
        try {
            writer.write(line + System.lineSeparator());
            return writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> BinaryOperator<T> combinationNotSupported() {
        return (a, b) -> {
            throw new UnsupportedOperationException();
        };
    }
}
