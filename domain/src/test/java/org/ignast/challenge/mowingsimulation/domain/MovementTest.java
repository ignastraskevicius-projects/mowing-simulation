package org.ignast.challenge.mowingsimulation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MovementTest {

    @Test
    public void shouldIndicateLocationChange() {
        assertThat(new Movement(new Location(0, 0), new Location(0, 1)).hasLocationChanged()).isTrue();
        assertThat(new Movement(new Location(1, 1), new Location(1, 0)).hasLocationChanged()).isTrue();
    }

    @Test
    public void shouldNotIndicateLocationChangeWhenThereIsNo() {
        assertThat(new Movement(new Location(0, 0), new Location(0, 0)).hasLocationChanged()).isFalse();
    }

    @Test
    public void shouldCreateInPlaceMovement() {
        Movement inPlace = Movement.createInPlace(new Location(1, 2));

        assertThat(inPlace.locationFrom()).isEqualTo(inPlace.locationTo());
        assertThat(inPlace.locationFrom()).isEqualTo(new Location(1, 2));
    }
}
