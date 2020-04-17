package ru.nsu.g.amaseevskii.tetris;

public class S_piece implements Tetromino {
    private final Integer[][][] shape = {
            {{0, 0, 1, 1}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {0, 0, 1, 1}, {0, 0, 0, 1}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {0, 0, 1, 1}, {0, 1, 1, 0}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}
    };

    @Override
    public Integer[][] turn(int state) {
        switch (state % 4) {
            case 1:
                return shape[1];
            case 2:
                return shape[2];
            case 3:
                return shape[3];
            default:
                return shape[0];
        }
    }
}
