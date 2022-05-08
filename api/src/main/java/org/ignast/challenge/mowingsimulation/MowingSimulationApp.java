package org.ignast.challenge.mowingsimulation;

import lombok.val;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MowingSimulationApp {
    public static void main(final String[] args) throws IOException {
        String outputFilePath = args[0].substring(0, args[0].length() - 6) + ".output";
        Files.createFile(Path.of(outputFilePath));
        val fileWriter = new FileWriter(outputFilePath);
        fileWriter.write("0 1 E");
        fileWriter.flush();
        fileWriter.close();
    }
}
