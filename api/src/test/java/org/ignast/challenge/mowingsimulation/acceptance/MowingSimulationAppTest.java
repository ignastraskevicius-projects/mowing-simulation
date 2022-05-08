package org.ignast.challenge.mowingsimulation.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import lombok.val;
import org.ignast.challenge.mowingsimulation.MowingSimulationApp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MowingSimulationAppTest {

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
    public void mowerShouldFollowInstructions() throws IOException {
        writeFile(
            inputFile.getPath(),
            """
                1 1
                0 0 N
                FR
                """
        );

        MowingSimulationApp.main(new String[] { inputFile.getPath() });

        val result = Files.lines(outputFilePath).collect(Collectors.toUnmodifiableList());
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("0 1 E");
    }

    @Test
    public void applicationShouldRequireInputFile() throws IOException {
        MowingSimulationApp.main(new String[] {});

        assertThat(outputFilePath.toFile().exists()).isFalse();
    }

    private void writeFile(String path, String content) throws IOException {
        FileWriter inputFileWriter = new FileWriter(path);
        inputFileWriter.write(content);
        inputFileWriter.flush();
        inputFileWriter.close();
    }
}
