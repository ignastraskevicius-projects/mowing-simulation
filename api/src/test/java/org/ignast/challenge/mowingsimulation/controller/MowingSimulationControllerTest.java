package org.ignast.challenge.mowingsimulation.controller;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import lombok.val;
import org.ignast.challenge.mowingsimulation.domain.MowingSimulator;
import org.ignast.challenge.mowingsimulation.domain.ProgrammedMower;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MowingSimulationControllerTest {

    private final MowingSimulator simulator = mock(MowingSimulator.class);

    @Captor
    private ArgumentCaptor<List<ProgrammedMower>> mowersCaptor;

    @Test
    public void shouldStartSimulationWithNoMowers() {
        val controller = new MowingSimulationController(
            new FileBasedCommunicationStub(List.of("0 0"), List.of()),
            simulator
        );

        controller.simulate(mock(Path.class));

        verify(simulator).executeSimulation(List.of());
    }

    @Test
    public void shouldNotParseNotRecognisedCommands() {
        val controller = new MowingSimulationController(
            new FileBasedCommunicationStub(List.of("0 0", "0 0 N", "E"), List.of("1 2 W")),
            simulator
        );

        assertThatIllegalArgumentException().isThrownBy(() -> controller.simulate(mock(Path.class)));
    }

    @Test
    public void shouldSerializeDirections() {
        List
            .of("S", "N", "W", "E")
            .forEach(direction -> {
                val controller = new MowingSimulationController(
                    new FileBasedCommunicationStub(
                        List.of("1 1", format("0 0 %s", direction), ""),
                        List.of(format("0 0 %s", direction))
                    ),
                    simulator
                );

                controller.simulate(mock(Path.class));
            });
    }

    @Test
    public void shouldParseLocation() {
        val controller = new MowingSimulationController(
            new FileBasedCommunicationStub(List.of("2 2", "1 2 N", ""), List.of("1 2 N")),
            simulator
        );

        controller.simulate(mock(Path.class));
    }

    @Test
    public void shouldParseCommands() {
        val commandsSerialised = "FRFLFL";
        val controller = new MowingSimulationController(
            new FileBasedCommunicationStub(List.of("1 2", "0 0 N", commandsSerialised), List.of("1 2 W")),
            simulator
        );
        doAnswer(invocation -> {
                List<ProgrammedMower> mowers = invocation.getArgument(0);
                assertThat(mowers).hasSize(1);
                val mower = mowers.get(0);
                IntStream.range(0, commandsSerialised.length()).forEach(i -> mower.performNextMove());
                return invocation;
            })
            .when(simulator)
            .executeSimulation(any());

        controller.simulate(mock(Path.class));
    }

    @Test
    public void shouldStartSimulationWithMultipleMowers() {
        val controller = new MowingSimulationController(
            new FileBasedCommunicationStub(
                List.of("1 1", "0 0 N", "FRF", "1 1 S", "FRF"),
                List.of("0 0 N", "1 1 S")
            ),
            simulator
        );

        controller.simulate(mock(Path.class));
    }
}
