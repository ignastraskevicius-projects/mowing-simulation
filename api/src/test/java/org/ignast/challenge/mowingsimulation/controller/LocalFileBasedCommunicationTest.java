package org.ignast.challenge.mowingsimulation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalFileBasedCommunicationTest {

    private final LocalFileBasedCommunication fileBasedComms = new LocalFileBasedCommunication();

    private final String fileName = "mowingSimulation" + System.currentTimeMillis();

    private final File tempDir = new File(System.getProperty("java.io.tmpdir"));

    private File inputFile;

    private Path outputFilePath;

    @BeforeEach
    public void createInputFileAndOutputPath() throws IOException {
        inputFile =
            Files
                .createFile(Path.of(tempDir.getAbsolutePath() + File.separator + fileName + ".input"))
                .toAbsolutePath()
                .toFile();
        inputFile.deleteOnExit();
        outputFilePath = Path.of(tempDir.getAbsolutePath() + File.separator + fileName + ".output");
    }

    @AfterEach
    public void remoteCreatedFiles() {
        outputFilePath.toFile().deleteOnExit();
    }

    @Test
    public void shouldFailIfInputFileIsNotFound() {
        assertThatIllegalArgumentException()
            .isThrownBy(() ->
                fileBasedComms.handleRequest(Path.of("non-existent-file.input"), inputLines -> List.of())
            );
    }

    @Test
    public void shouldFailIfInputFileDoesNotEndWithInputLiteral() throws IOException {
        val file = Files
            .createFile(Path.of(tempDir.getAbsolutePath() + File.separator + fileName))
            .toAbsolutePath()
            .toFile();
        file.deleteOnExit();

        assertThatIllegalArgumentException()
            .isThrownBy(() -> fileBasedComms.handleRequest(file.toPath(), inputLines -> List.of()));
    }

    @Test
    public void shouldReadInputFile() throws IOException {
        writeFile(inputFile.getPath(), """
                1
                2""");

        fileBasedComms.handleRequest(
            inputFile.toPath(),
            inputLines -> {
                assertThat(inputLines).hasSize(2);
                assertThat(inputLines.get(0)).isEqualTo("1");
                assertThat(inputLines.get(1)).isEqualTo("2");
                return List.of();
            }
        );
    }

    @Test
    public void shouldWriteToOutputFile() throws IOException {
        writeFile(inputFile.getPath(), "");
        fileBasedComms.handleRequest(inputFile.toPath(), inputLines -> List.of("1", "2"));

        val result = Files.lines(outputFilePath).collect(Collectors.toUnmodifiableList());
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("1");
        assertThat(result.get(1)).isEqualTo("2");
    }

    private void writeFile(String path, String content) throws IOException {
        FileWriter inputFileWriter = new FileWriter(path);
        inputFileWriter.write(content);
        inputFileWriter.flush();
        inputFileWriter.close();
    }
}
