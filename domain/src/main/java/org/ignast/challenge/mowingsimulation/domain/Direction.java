package org.ignast.challenge.mowingsimulation.domain;

public enum Direction {
    NORTH {
        @Override
        public Location getNextLocationFrom(Location currentDirection) {
            return new Location(currentDirection.x(), currentDirection.y() + 1);
        }
    },
    EAST {
        @Override
        public Location getNextLocationFrom(Location currentDirection) {
            return new Location(currentDirection.x() + 1, currentDirection.y());
        }
    },
    SOUTH {
        @Override
        public Location getNextLocationFrom(Location currentDirection) {
            return new Location(currentDirection.x(), currentDirection.y() - 1);
        }
    },
    WEST {
        @Override
        public Location getNextLocationFrom(Location currentDirection) {
            return new Location(currentDirection.x() - 1, currentDirection.y());
        }
    };

    public abstract Location getNextLocationFrom(Location currentDirection);

    public Direction getDiretionAfterTurning90DegreesLeft() {
        return Direction.values()[(ordinal() + 4 - 1) % 4];
    }

    public Direction getDiretionAfterTurning90DegreesRight() {
        return Direction.values()[(ordinal() + 1) % 4];
    }
}
