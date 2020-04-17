package ru.nsu.g.amaseevskii.tetris;

class I_piece implements Tetromino {
    private final Integer[][][] shape = {
            {{1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}},
            {{0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}},
            {{0, 0, 0, 0}, {0, 0, 0, 0}, {1, 1, 1, 1}, {0, 0, 0, 0}},
            {{0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}, {0, 1, 0, 0}}
    };


    @Override
    public Integer[][] turn(int state) {
        if (state == -1) {
            return shape[0];
        }
        switch (state%4) {
            case 0:
                return shape[1];
            case 1:
                return shape[2];
            case 2:
                return shape[3];
            default:
                return shape[4];
        }
    }
}
