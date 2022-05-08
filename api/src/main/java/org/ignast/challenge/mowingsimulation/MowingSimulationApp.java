package org.ignast.challenge.mowingsimulation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import lombok.val;
import org.ignast.challenge.mowingsimulation.controller.MowingSimulationController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MowingSimulationApp {

    public static void main(final String[] args) throws IOException {
        val controller = getController();

        getFilePath(args)
            .map(controller::simulate)
            .map(Path::toString)
            .ifPresentOrElse(
                System.out::println,
                () ->
                    System.out.println("Input file required to be provided as a first command line argument")
            );
    }

    private static Optional<Path> getFilePath(String[] args) {
        if (args.length > 0) {
            return Optional.of(Path.of(args[0]));
        } else {
            return Optional.empty();
        }
    }

    private static MowingSimulationController getController() {
        val context = new AnnotationConfigApplicationContext();
        context.scan(MowingSimulationApp.class.getPackageName());
        context.refresh();
        MowingSimulationController controller = context.getBean(MowingSimulationController.class);
        return controller;
    }
}
